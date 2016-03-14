package com.example.supernova.pfe.data;


import android.net.Uri;
import android.util.Log;

import com.example.supernova.pfe.tasks.ApiAccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Client {
    String id;
    String f_name;
    String l_name;
    String phone;
    double remaining;
    double[] location;

    public Client() {
    }

    public String getId() {
        return id;
    }

    public Client setId(String id) {
        this.id = id;
        return this;
    }

    public String getF_name() {
        return f_name;
    }

    public Client setF_name(String f_name) {
        this.f_name = f_name;
        return this;
    }

    public String getL_name() {
        return l_name;
    }

    public Client setL_name(String l_name) {
        this.l_name = l_name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Client setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public double[] getLocation() {
        return location;
    }

    public Client setLocation(double[] location) {
        this.location = location;
        return this;
    }

    public double getRemaining() {
        return remaining;
    }

    public Client setRemaining(double remaining) {
        this.remaining = remaining;
        return this;
    }

    public String getFullName(){
        return this.f_name+" "+l_name;
    }

    public String toJson(){
        return null;
    }

    public static ArrayList<Client> fromJsonAll(String json){
        JSONObject client = null;
        ArrayList<Client> clientsArray = null;
        Client c = null;
        Log.v("testtttttttt", json+" ");
        try {
            JSONArray clients = new JSONArray(json);
            clientsArray = new ArrayList<>(clients.length());
            for(int i = 0; i < clients.length(); ++i){
                client = clients.getJSONObject(i);
                JSONArray location = client.getJSONArray("location");
                c = new Client().setF_name(client.getString("f_name"))
                        .setL_name(client.getString("l_name"))
                        .setPhone(client.getString("phone"))
                        .setLocation(new double[]{location.getDouble(0), location.getDouble(1)})
                        .setRemaining(client.getDouble("remaining"));
                clientsArray.add(c);
            }

        } catch (JSONException e) {e.printStackTrace();}
        return clientsArray;
    }


    public static ArrayList<Client> fetchClients(){
        String jsonResult = new ClientApi().fetchAllClients();
        return fromJsonAll(jsonResult);
    }


    public static Client fromJson(String json){
        return null;                                            
    }

    public static class ClientApi implements ApiAccess.ApiAccessWork {
        public static String CLIENTS_URL = "http://10.0.3.2:3000/clients.json";

        public ClientApi(){
        }

        public String fetchAllClients(){
            try {
                return new ApiAccess().setMethod("GET")
                        .setUri(Uri.parse(CLIENTS_URL)).setCaller(this).execute().get();
            } catch (Exception e) {e.printStackTrace();}
            return null;
        }

        @Override
        public void doStuffWithResult(String result) {

        }
    }
}
