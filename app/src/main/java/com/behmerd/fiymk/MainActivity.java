package com.behmerd.fiymk;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.FrameLayout;
        import android.widget.TextView;
        import android.widget.Toast;
        import ir.magnet.sdk.MagnetMobileBannerAd;


public class MainActivity extends Activity {

    private boolean premiumAccessStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get premium access status from shared preferences
        final SPLayer IABsp = new SPLayer(getApplicationContext());
        premiumAccessStatus = IABsp.iabGetStatus();
        if(!premiumAccessStatus)
        {
            Button btnBuy = (Button) findViewById(R.id.btnBuy);
            btnBuy.setVisibility(View.VISIBLE);
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,IABActivity.class));
                }
            });
                // show ads
                MagnetMobileBannerAd bannerAd = MagnetMobileBannerAd.create(getApplicationContext());
                FrameLayout adLayout = (FrameLayout) findViewById(R.id.mobileBanner);
                bannerAd.load("22079ad8988f4828a70e4ab4804c2de3", adLayout);
        }

        TextView lblQ = (TextView)findViewById(R.id.lblQuit);
        lblQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exit();
            }
        });

        TextView lblL = (TextView)findViewById(R.id.lblLesson);
        lblL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LessonsActivity.class));
            }
        });

        TextView lblH = (TextView)findViewById(R.id.lblHelp);
        lblH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HelpActivity.class));
            }
        });

        TextView lblA = (TextView)findViewById(R.id.lblAbout);
        lblA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
            }
        });

        TextView lblF = (TextView)findViewById(R.id.lblFavorites);
        lblF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPLayer sp=new SPLayer(getApplicationContext());
                if(sp.getFavorites() != null) {
                    Intent la = new Intent(getApplicationContext(), LessonsActivity.class);
                    la.putExtra("key", "true");
                    startActivity(la);
                }
                else
                    Toast.makeText(getApplicationContext(), "There is no favorite item!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onBackPressed(){
        Exit();
    }
}
