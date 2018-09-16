package com.behmerd.fiymk;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.vending.billing.IInAppBillingService;
import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
import com.example.android.trivialdrivesample.util.Purchase;

public class IABActivity extends Activity {
    //IAB definitions
    final String TAG = "50i";
    /*final String  SKU = "50Idioms_Myket_PremiumAccess";
    final String  SKU = "50Idioms_Bazaar_PremuimAccess";
    final String  SKU = "50Idioms_Avvalmarket_PremiumAccess";
    final String  SKU = "50Idioms_Iranapps_PremiumAccess";*/
    final String  SKU = "50Idioms_Cando_PremiumAccess";
    /*final String payLoad = "Behmerd_Myket_PremiumAccess_Purchase";
    final String payLoad = "Behmerd_Bazaar_PremiumAccess_Purchase";
    final String payLoad = "Behmerd_Avvalmarket_PremiumAccess_Purchase";
    final String payLoad = "Behmerd_Iranapps_PremiumAccess_Purchase";*/
    final String payLoad = "Behmerd_Cando_PremiumAccess_Purchase";
    boolean mIsPremium = false;             // Does the user have the premium upgrade?
    static final int RC_REQUEST = 5692;     // (arbitrary) request code for the purchase flow
    IabHelper mHelper;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iab);
        final SPLayer IABsp = new SPLayer(getApplicationContext());
        //IAB Deployment
        try {
            /*Intent serviceIntent = new Intent("ir.mservices.market.InAppBillingService.BIND");
            serviceIntent.setPackage("ir.mservices.market");
            Intent serviceIntent = new Intent("ir.cafebazaar.pardakht.InAppBillingService.BIND");
            serviceIntent.setPackage("com.farsitel.bazaar");
            Intent serviceIntent = new Intent("com.hrm.android.market.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("com.hrm.android.market");
            Intent serviceIntent = new Intent("ir.tgbs.iranapps.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("ir.tgbs.android.iranapp");*/
            Intent serviceIntent = new Intent("com.ada.market.service.payment.BIND");
            serviceIntent.setPackage("com.ada.market");
            bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
            String base64EncodedPublicKey = getResources().getString(R.string.bsfepk);
            mHelper = new IabHelper(this, base64EncodedPublicKey);

            mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
                public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                    Log.d(TAG, "Query inventory finished.");
                    if (result.isFailure()) {
                        Log.d(TAG, "Failed to query inventory: " + result);
                        return;
                    } else {
                        Log.d(TAG, "Query inventory was successful.");
                        // does the user have the premium upgrade?
                        mIsPremium = inventory.hasPurchase(SKU);
                        // update UI accordingly

                        Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
                    }

                    Log.d(TAG, "Initial inventory query finished; enabling main UI.");
                }
            };

            mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
                public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                    if (result.isFailure()) {
                        Log.d(TAG, "Error purchasing: " + result);
                        return;
                    } else if (purchase.getSku().equals(SKU)) {
                        // give user access to premium content and update the UI
                        Toast.makeText(IABActivity.this, "Ads now disabled!", Toast.LENGTH_SHORT).show();
                        if (IABsp.iabSetStatus(true)) {
                            Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(i);
                        }
                    }
                }
            };

            Log.d(TAG, "Starting setup.");
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(TAG, "Setup finished.");

                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    }
                    // Hooray, IAB is fully set up!
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            });

            Button btnBuy = (Button) findViewById(R.id.btnPurchase);
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHelper.launchPurchaseFlow(IABActivity.this, SKU, RC_REQUEST, mPurchaseFinishedListener,payLoad);
                }
            });
        }
        catch(NullPointerException npe){
            Log.e(TAG, "NullPointerException occurred! Maybe the target market is not exist on your device...");
            Toast.makeText(IABActivity.this,"Target market wasn't found...",Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            Log.e(TAG, "Error on IAB deployment...");
        }
    }

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            if (mHelper != null) mHelper.dispose();
            if (mServiceConn != null) unbindService(mServiceConn);
        mHelper = null;}catch (Exception e){
            Log.e(TAG, "Error on disposing mHelper...");
        }
    }

}
