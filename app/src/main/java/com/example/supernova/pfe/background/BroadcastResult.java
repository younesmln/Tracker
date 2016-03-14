package com.example.supernova.pfe.background;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.supernova.pfe.activities.MainActivity;
import com.example.supernova.pfe.data.Client;
import com.google.gson.Gson;

public class BroadcastResult extends BroadcastReceiver {
    private MainActivity mActivity;
    public static String IntentFilterString = "com.example.supernova.pfe.Result";

    public BroadcastResult(Activity a){
        mActivity = (MainActivity) a;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String result = intent.getStringExtra("result");
        Client[] clients = new Gson().fromJson(result, Client[].class);
        mActivity.refreshPositions(clients);
    }
}
