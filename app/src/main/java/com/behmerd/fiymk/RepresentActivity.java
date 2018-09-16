package com.behmerd.fiymk;

        import android.app.Activity;
        import android.graphics.Typeface;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import ir.magnet.sdk.MagnetMobileBannerAd;

public class RepresentActivity extends Activity {

    private MediaPlayer player;
    private int i;
    String[] items;
    SPLayer sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_represent);

        //get premium access status from shared preferences
        sp=new SPLayer(getApplicationContext());
        boolean premiumAccessStatus = sp.iabGetStatus();
        if(!premiumAccessStatus) {
            MagnetMobileBannerAd bannerAd = MagnetMobileBannerAd.create(getApplicationContext());
            FrameLayout adLayout = (FrameLayout) findViewById(R.id.mobileBanner);
            bannerAd.load("22079ad8988f4828a70e4ab4804c2de3", adLayout); // Myket.ir unit
            //bannerAd.load("650d3f3617674c54968f5ecd7e9d8935", adLayout); // iranapps.ir unit
            //bannerAd.load("8e7e6a34f291481ba6ef3f116ccccc0d", adLayout); // bazaar.ir unit
            //bannerAd.load("99ff420e5ab04e98be140290f12e672f", adLayout); // avalmarket.ir unit
        }

        TextView txtIdiom = (TextView)findViewById(R.id.txtIdi);
        TextView txtMean = (TextView)findViewById(R.id.txtMean);
        TextView txtEg = (TextView)findViewById(R.id.txtEg);
        final TextView txtEg2 = (TextView)findViewById(R.id.txtEg2);
        TextView txtPmean = (TextView)findViewById(R.id.txtPmean);
        TextView txtPeg = (TextView)findViewById(R.id.txtPeg);
        ImageView btnN = (ImageView)findViewById(R.id.btnNext);
        ImageView btnP = (ImageView)findViewById(R.id.btnPrev);
        final ImageView btnFavor = (ImageView) findViewById(R.id.ivFavor);

        try{
            Typeface fontPB = Typeface.createFromAsset(getAssets(),"font/BNaznnBd.ttf");
            Typeface fontP = Typeface.createFromAsset(getAssets(),"font/BNazanin.ttf");
            Typeface fontEB = Typeface.createFromAsset(getAssets(),"font/timesbd_0.ttf");
            Typeface fontE = Typeface.createFromAsset(getAssets(),"font/times_0.ttf");
            Typeface fontEI = Typeface.createFromAsset(getAssets(),"font/timesi_0.ttf");
            txtIdiom.setTypeface(fontEB);
            txtPmean.setTypeface(fontPB);
            txtMean.setTypeface(fontEI);
            txtEg.setTypeface(fontE);
            txtEg2.setTypeface(fontE);
            txtPeg.setTypeface(fontP);
        }
        catch(Exception e){
            Log.e("50s", "Typeface setting error.");
        }

        items = getResources().getStringArray(R.array.idiomList);
        if(getIntent().getExtras() != null) {
            i=Integer.parseInt(getIntent().getExtras().getString("key"));
        }

        setContent(i);

        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < 49)
                    setContent(++i);
            }
        });

        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i>0)
                    setContent(--i);
            }
        });

        txtEg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (player != null) {
                        player.release();
                        player = null;
                    }
                    player = MediaPlayer.create(RepresentActivity.this, getSoundId(i, false));
                    player.setLooping(false);
                    player.setVolume(100, 100);
                    player.start();
                }
                catch(Exception e){
                    Log.e("50s", "Player stop error.");
                }
            }
        });

        txtEg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (player != null) {
                        player.release();
                        player = null;
                    }
                    player = MediaPlayer.create(RepresentActivity.this, getSoundId(i, true));
                    player.setLooping(false);
                    player.setVolume(100, 100);
                    player.start();
                }
                catch(Exception e){
                    Log.e("50s", "Player stop error.");
                }
            }
        });

        btnFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.isFavorite(i)) {
                    if(sp.unsetAsFavorite(String.valueOf(i))){
                        Toast.makeText(getApplicationContext(), "Idiom removed from favorites!", Toast.LENGTH_SHORT).show();
                        btnFavor.setImageResource(R.drawable.star_off);
                    }
                }
                else
                {
                    if(sp.setAsFavorite(String.valueOf(i))){
                        Toast.makeText(getApplicationContext(), "Idiom added to favorites!", Toast.LENGTH_SHORT).show();
                        btnFavor.setImageResource(R.drawable.star_on);
                    }
                }
            }
        });

    }

    public void onBackPressed(){
        try {
            player.stop();
            if(player != null){
                player.release();
                player=null;
            }
        }
        catch(Exception e) {
            Log.e("50s", "Player stop error.");
        }
        finally {
            finish();
        }
    }

    private int getSoundId(int position, boolean b2p) {
        int id = R.raw.i1;
        try{
            switch (position){
                case 0:
                    id = R.raw.i1;
                    break;
                case 1:
                    id = R.raw.i2;
                    break;
                case 2:
                    if(b2p)
                        id = R.raw.i3_2;
                    else
                        id = R.raw.i3_1;
                    break;
                case 3:
                    id = R.raw.i4;
                    break;
                case 4:
                    id = R.raw.i5;
                    break;
                case 5:
                    id = R.raw.i6;
                    break;
                case 6:
                    id = R.raw.i7;
                    break;
                case 7:
                    id = R.raw.i8;
                    break;
                case 8:
                    id = R.raw.i9;
                    break;
                case 9:
                    id = R.raw.i10;
                    break;
                case 10:
                    id = R.raw.i11;
                    break;
                case 11:
                    id = R.raw.i12;
                    break;
                case 12:
                    id = R.raw.i13;
                    break;
                case 13:
                    id = R.raw.i14;
                    break;
                case 14:
                    id = R.raw.i15;
                    break;
                case 15:
                    id = R.raw.i16;
                    break;
                case 16:
                    id = R.raw.i17;
                    break;
                case 17:
                    id = R.raw.i18;
                    break;
                case 18:
                    id = R.raw.i19;
                    break;
                case 19:
                    id = R.raw.i20;
                    break;
                case 20:
                    id = R.raw.i21;
                    break;
                case 21:
                    if(b2p)
                        id = R.raw.i22_2;
                    else
                        id = R.raw.i22_1;
                    break;
                case 22:
                    if(b2p)
                        id = R.raw.i23_2;
                    else
                        id = R.raw.i23_1;
                    break;
                case 23:
                    id = R.raw.i24;
                    break;
                case 24:
                    id = R.raw.i25;
                    break;
                case 25:
                    id = R.raw.i26;
                    break;
                case 26:
                    id = R.raw.i27;
                    break;
                case 27:
                    id = R.raw.i28;
                    break;
                case 28:
                    if(b2p)
                        id = R.raw.i29_2;
                    else
                        id = R.raw.i29_1;
                    break;
                case 29:
                    if(b2p)
                        id = R.raw.i30_2;
                    else
                        id = R.raw.i30_1;
                    break;
                case 30:
                    id = R.raw.i31;
                    break;
                case 31:
                    id = R.raw.i32;
                    break;
                case 32:
                    id = R.raw.i33;
                    break;
                case 33:
                    id = R.raw.i34;
                    break;
                case 34:
                    id = R.raw.i35;
                    break;
                case 35:
                    id = R.raw.i36;
                    break;
                case 36:
                    id = R.raw.i37;
                    break;
                case 37:
                    id = R.raw.i38;
                    break;
                case 38:
                    id = R.raw.i39;
                    break;
                case 39:
                    id = R.raw.i40;
                    break;
                case 40:
                    id = R.raw.i41;
                    break;
                case 41:
                    id = R.raw.i42;
                    break;
                case 42:
                    id = R.raw.i43;
                    break;
                case 43:
                    id = R.raw.i44;
                    break;
                case 44:
                    id = R.raw.i45;
                    break;
                case 45:
                    id = R.raw.i46;
                    break;
                case 46:
                    id = R.raw.i47;
                    break;
                case 47:
                    id = R.raw.i48;
                    break;
                case 48:
                    id = R.raw.i49;
                    break;
                case 49:
                    id = R.raw.i50;
                    break;
                default:
                    id = R.raw.i1;
                    break;
            }
        }
        catch(Exception e){
            Log.e("50s", "Sound resource ID not found.");
        }
        finally {
            return id;
        }
    }

    private void setContent(int res){
        String dat = items[res];
        String[] item = dat.split(";");

        TextView txtIdiom = (TextView)findViewById(R.id.txtIdi);
        TextView txtMean = (TextView)findViewById(R.id.txtMean);
        TextView txtEg = (TextView)findViewById(R.id.txtEg);
        TextView txtEg2 = (TextView)findViewById(R.id.txtEg2);
        TextView txtPmean = (TextView)findViewById(R.id.txtPmean);
        TextView txtPeg = (TextView)findViewById(R.id.txtPeg);
        TextView txtPos=(TextView)findViewById(R.id.txtPos);
        ImageView btnFavor = (ImageView) findViewById(R.id.ivFavor);

        txtIdiom.setText(item[0]);
        txtMean.setText(item[1]);
        txtEg.setText(item[2]);
        if (isDubbleEg(i)){
            txtEg2.setText(item[3]);
            txtPmean.setText(item[4]);
            txtPeg.setText(item[5]);
        }
        else{
            txtPmean.setText(item[3]);
            txtPeg.setText(item[4]);
        }

        txtPos.setText("Idiom " + (res + 1));
        if (isDubbleEg(res))
            txtEg2.setVisibility(View.VISIBLE);
        else
            txtEg2.setVisibility(View.INVISIBLE);

        if(sp.isFavorite(res))
            btnFavor.setImageResource(R.drawable.star_on);
        else
            btnFavor.setImageResource(R.drawable.star_off);
    }

    private boolean isDubbleEg(int idiom){
        return (idiom == 2) || (idiom == 21) || (idiom == 22) || (idiom == 28) || (idiom == 29);
    }

}