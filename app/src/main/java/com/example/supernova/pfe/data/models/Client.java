package com.example.supernova.pfe.data.models;


import android.net.Uri;
import android.util.Log;

import com.example.supernova.pfe.refactor.Util;
import com.example.supernova.pfe.tasks.ApiAccess;
import com.example.supernova.pfe.tasks.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    ArrayList<Invoice> invoices;

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

    public ArrayList<Invoice> getInvoices() {
        invoices = (invoices == null) ? new ArrayList<Invoice>() : invoices;
        return invoices;
    }

    public void setInvoices(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }

    public JSONObject toJson(){
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("f_name", f_name);
            object.put("l_name", l_name);
            object.put("phone", phone);
            if (location != null)
                object.put("location", new JSONArray().put(location[0]).put(location[1]));
        }catch (JSONException e){

        }
        return object;
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
                c = new Client().setId(client.getString("id"))
                        .setF_name(client.getString("f_name"))
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
        String jsonResult = new ClientApi().fetchAllClients().getBody();
        return fromJsonAll(jsonResult);
    }


    public static Client fromJson(String json){
        if (json == null || json.length() == 0) return null;
        JSONObject client;
        Client c;
        try {
            client = new JSONObject(json);
            c = new Client().setF_name(client.getString("f_name"))
                    .setL_name(client.getString("l_name"))
                    .setPhone(client.getString("phone"));
            if (!client.isNull("location")){
                JSONArray location = client.getJSONArray("location");
                c.setLocation(new double[]{location.getDouble(0), location.getDouble(1)});
            }
            if (client.has("remaining")){
                c.setRemaining(client.getDouble("remaining"));}
            if (client.has("invoices")){
                c.setInvoices(Invoice.fromJsonArray(client.getJSONArray("invoices").toString()));
            }
            return c;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class ClientApi {
        public static Uri CLIENTS_URL = Uri.parse(Util.host).buildUpon()
                                        .appendEncodedPath("clients.json").build();

        public ClientApi(){
        }

        public Response fetchAllClients(){
            try {
                return new ApiAccess().setMethod("GET")
                        .setUri(CLIENTS_URL).execute().get();
            } catch (Exception e) {e.printStackTrace();}
            return null;
        }
    }
}
