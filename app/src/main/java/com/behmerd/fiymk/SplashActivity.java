package com.behmerd.fiymk;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.CountDownTimer;
        import android.os.Bundle;
        import android.util.Log;

        import ir.magnet.sdk.MagnetSDK;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            MagnetSDK.initialize(getApplicationContext());
            MagnetSDK.getSettings().setTestMode(false);
        }
        catch(Exception e){
            Log.e("50I", "Error on Magnet intializing!");
        }

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        }.start();
    }

    protected void onStop(){
        super.onStop();
        finish();
    }
}
