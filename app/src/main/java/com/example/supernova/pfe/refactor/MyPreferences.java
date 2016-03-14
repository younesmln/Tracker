package com.example.supernova.pfe.refactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyPreferences {
    private static MyPreferences instance = null;
    private SharedPreferences mSharedPrefrences;

    public static MyPreferences getInstance(Context context){
        if ( instance == null){
            instance = new MyPreferences(context);
        }
        return instance;
    }

    private MyPreferences(Context context){
        mSharedPrefrences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveDataBool(String key, boolean value){
        SharedPreferences.Editor editor = mSharedPrefrences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean retrieveDataBool(String key){
        if (mSharedPrefrences != null){
            return mSharedPrefrences.getBoolean(key, false);
        }
        return false;
    }
}
