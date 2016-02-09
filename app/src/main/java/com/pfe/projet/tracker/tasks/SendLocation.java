package com.pfe.projet.tracker.tasks;

import android.net.Uri;
import android.util.Log;

import com.pfe.projet.tracker.data.LocationInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendLocation implements ApiAccess.ApiAccessWork{

    private String toSend;

    public SendLocation(LocationInfo location){
        this.toSend = buildJson(location);
    }

    public void startSending(){
        Log.v("SendLocation : ", " start sending *****************************");
        if(this.toSend != null && this.toSend.length() > 0){
            Uri uri = Uri.parse("http://10.0.3.2:3000").buildUpon()
                    .appendPath("api")
                    .appendPath("insertPosition").build();
            new ApiAccess().setCaller(this)
                    .setUri(uri).setMethod("POST")
                    .setBody(this.toSend).execute();
        }else{
            return;
        }
    }

    public String buildJson(LocationInfo location){
        JSONObject json = new JSONObject();
        String toSend = null;
        try {
            json.put("imei", location.getUuid());
            json.put("time", location.getTime());
            JSONArray array = new JSONArray();
            array.put(0, location.getLt());
            array.put(1, location.getLg());
            json.put("position",array);
            toSend = json.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return toSend;
    }

    @Override
    public void doStuffWithResult(String result) {
        Log.v("TEST", "************************************");
        Log.v("TEST", "********"+result+"*********");
        Log.v("TEST", "************************************");
    }
}
