package com.example.supernova.pfe.tasks;

import android.net.Uri;
import android.util.Log;

import com.example.supernova.pfe.data.LocationInfo;
import com.example.supernova.pfe.refactor.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SendLocation {

    private JsonElement toSend;

    public SendLocation(LocationInfo location){
        this.toSend = buildJson(location);
    }

    public Response startSending(){
        Log.v("SendLocation : ", " start sending *****************************");
        if(this.toSend != null && !this.toSend.isJsonNull()){
            Uri uri = Uri.parse(Util.host).buildUpon()
                    .appendPath("api")
                    .appendPath("insertPosition").build();
            try {
                return new ApiAccess()
                        .setUri(uri).setMethod("POST")
                        .setBody(this.toSend).execute()
                        .get();
            } catch (Exception e) {
                e.printStackTrace(); }
        }
        return null;
    }

    public JsonElement buildJson(LocationInfo location){
        JsonArray jsonArray = new JsonArray();
        JsonObject json = new JsonObject();
        json.addProperty("imei", location.getUuid());
        json.addProperty("time", location.getTime());
        JsonArray array = new JsonArray();
        array.add(location.getLg());
        array.add(location.getLt());
        json.add("position", array);
        jsonArray.add(json);
        return jsonArray;
    }
}
