package com.behmerd.fiymk;
        import android.app.Activity;
        import android.graphics.Typeface;
        import android.os.Bundle;
        import android.widget.FrameLayout;
        import android.widget.TextView;

        import ir.magnet.sdk.MagnetMobileBannerAd;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //get premium access status from shared preferences
        SPLayer IABsp = new SPLayer(getApplicationContext());
        boolean premiumAccessStatus = IABsp.iabGetStatus();
        if(!premiumAccessStatus) {
            MagnetMobileBannerAd bannerAd = MagnetMobileBannerAd.create(getApplicationContext());
            FrameLayout adLayout = (FrameLayout) findViewById(R.id.mobileBanner);
            bannerAd.load("22079ad8988f4828a70e4ab4804c2de3", adLayout); // Myket.ir unit
            //bannerAd.load("650d3f3617674c54968f5ecd7e9d8935", adLayout); // iranapps.ir unit
            //bannerAd.load("8e7e6a34f291481ba6ef3f116ccccc0d", adLayout); // bazaar.ir unit
            //bannerAd.load("99ff420e5ab04e98be140290f12e672f", adLayout); // avalmarket.ir unit
        }
        Typeface font = Typeface.createFromAsset(getAssets(),"font/BNaznnBd.ttf");
        TextView tv = (TextView) findViewById(R.id.txtCU);
        TextView tvh1 = (TextView) findViewById(R.id.tvH1);
        TextView tvh2 = (TextView) findViewById(R.id.tvH2);
        tv.setTypeface(font);
        tvh1.setTypeface(font);
        tvh2.setTypeface(font);
    }
}
