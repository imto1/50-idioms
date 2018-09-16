package com.behmerd.fiymk;

        import android.app.AlertDialog;
        import android.app.ListActivity;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;
        import java.util.Arrays;
        import ir.magnet.sdk.MagnetMobileBannerAd;

public class LessonsActivity extends ListActivity {

    String[] items,listItems;
    boolean isFavoriteList = false;
    SPLayer sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

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

        TextView tvTitle = (TextView)findViewById(R.id.tvListTitle);
        try{
            items = getResources().getStringArray(R.array.idiomTitle);
            if(getIntent().getExtras() != null) {
                isFavoriteList = Boolean.valueOf(getIntent().getExtras().getString("key"));
                if(isFavoriteList) {
                    String tempList = null;
                    int i =0;
                    for (String item : items)
                        if(sp.isFavorite(i++))
                            if (tempList == null)
                                tempList = item;
                            else
                                tempList += ("~" + item);

                    listItems = tempList.split("~");
                    tvTitle.setText("Favorite Lessons");
                }
                else {
                    listItems = items;
                    tvTitle.setText("All Lessons");
                }
            }
            else {
                listItems = items;
                tvTitle.setText("All Lessons");
            }
        }
        catch(Exception e){
            Log.e("50's","Error on generating idiom list!");
        }
        finally {
            if(listItems.length<1){
                Toast.makeText(getApplicationContext(), "There is no favorite item!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        final ListView idiomlist = (ListView) findViewById(android.R.id.list);
        idiomlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent rai = new Intent(getApplicationContext(), RepresentActivity.class);
                    if (isFavoriteList) {
                        String idiom = listItems[position];
                        rai.putExtra("key", String.valueOf(Arrays.asList(items).indexOf(idiom)));
                    } else
                        rai.putExtra("key", String.valueOf(position));

                    startActivity(rai);
                } catch (Exception e) {
                    Log.e("50s", "Intent putExtra error.");
                }
            }
        });

        ImageView clearAll = (ImageView) findViewById(R.id.ivBin);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LessonsActivity.this);
                builder.setMessage("Clear all favorites?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp.clearFavorites();
                        Toast.makeText(getApplicationContext(), "All favorite items removed!", Toast.LENGTH_SHORT).show();
                        if (isFavoriteList) {
                            finish();
                        }
                        setListAdapter(new IdiomAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, R.id.txtIdi, listItems));
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
        });

        setListAdapter(new IdiomAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, R.id.txtIdi, listItems));
    }

    public void onResume(){
        super.onResume();
        try {
            if (isFavoriteList) {
                String tempList = null;
                int i = 0;
                for (String item : items)
                    if (sp.isFavorite(i++))
                        if (tempList == null)
                            tempList = item;
                        else
                            tempList += ("~" + item);

                listItems = tempList.split("~");
            }
        }
        catch (Exception e){
            Log.e("50's","Error on generating idiom list when resuming to layout!");
        }
        finally {
            if (listItems.length < 1) {
                Toast.makeText(getApplicationContext(), "There is no favorite item!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        setListAdapter(new IdiomAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, R.id.txtIdi, listItems));
    }

    private class IdiomAdapter extends ArrayAdapter<String> {

        public IdiomAdapter(Context context, int resource, int textViewResourceId, String[] strings) {
            super(context, resource, textViewResourceId, strings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.listview_layout, parent, false);
            try {
                TextView tvp = (TextView) row.findViewById(R.id.txtPos);
                TextView tvi = (TextView) row.findViewById(R.id.txtIdi);
                ImageView ivf = (ImageView) row.findViewById(R.id.ivFI);

                tvi.setText(listItems[position]);
                tvp.setText(String.valueOf(position + 1) + ".");
                if (isFavoriteList)
                    ivf.setImageResource(R.drawable.star_on);
                else if (sp.isFavorite(position))
                    ivf.setImageResource(R.drawable.star_on);
                else
                    ivf.setImageResource(R.drawable.star_off);
            }
            catch(Exception e){
                Log.e("50s", "ListView data filling error.");
            }
            finally {
                return row;
            }
        }
    }
}
