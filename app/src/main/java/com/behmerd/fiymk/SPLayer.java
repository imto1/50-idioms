package com.behmerd.fiymk;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Arrays;

public class SPLayer {
    private static final String FavoritsPrefs = "IdiomPrefs";
    private static final String FavoritsKey = "idiomKey";
    private static final String IABPrefs = "IABPrefs";
    private static final String IABKey = "PremiumAccessStatus";
    private static SharedPreferences sp;
    private static Context context;

    public SPLayer(Context cntx){
        context = cntx;
    }

    //Favorit idioms SP methods
        public static boolean isFavorite(int idiomNo){
            String existData = getFavorites();
            String[] dataArray;
            if(existData != null)
                dataArray = existData.split("~");
            else
                return  false;

            if(dataArray.length>0)
                if(Arrays.asList(dataArray).contains(String.valueOf(idiomNo)))
                    return true;
            return false;
        }

        public static boolean setAsFavorite(String idiomIndex){
            String existData = getFavorites();
            if(existData == null)
                return setFavorites(idiomIndex);
            else
                return setFavorites((existData + "~" + idiomIndex));
        }

        public static boolean unsetAsFavorite(String idiomIndex){
            String existData = getFavorites();
            String[] dataArray = existData.split("~");
            String newData = null;

            if(dataArray.length>0)
                for (String currentIdiom : dataArray)
                    if (Integer.parseInt(currentIdiom) != Integer.parseInt(idiomIndex))
                        if(newData == null)
                            newData = currentIdiom;
                        else
                            newData += ("~" + currentIdiom);
            return setFavorites(newData);
        }

        private static boolean setFavorites(String data){
            sp = context.getSharedPreferences(FavoritsPrefs, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(FavoritsKey, data);
            return editor.commit();
        }

        public static String getFavorites(){
            sp = context.getSharedPreferences(FavoritsPrefs, Context.MODE_PRIVATE);
            return sp.getString(FavoritsKey, null);
        }

        public static boolean clearFavorites(){
            sp = context.getSharedPreferences(FavoritsPrefs, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            return editor.commit();
        }


    //IAB SP methods
        public static boolean iabGetStatus(){
            sp = context.getSharedPreferences(IABPrefs, Context.MODE_PRIVATE);
            return sp.getBoolean(IABKey, false);
        }

        public static boolean iabSetStatus(boolean status){
            sp = context.getSharedPreferences(IABPrefs, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(IABKey, status);
            return editor.commit();
        }
}
