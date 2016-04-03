package com.example.supernova.pfe.data.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Invoice {
    String id;
    String createdAt;
    double total;
    double remaining;
    ArrayList<Product> products;

    public ArrayList<Product> getProducts() {
        products = (products == null) ? new ArrayList<Product>() : products;
        return products;
    }

    public Invoice setProducts(ArrayList<Product> products) {
        this.products = products;
        return this;
    }

    public String getId() {
        return id;

    }

    public Invoice setId(String id) {
        this.id = id;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Invoice setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public double getTotal() {
        return total;
    }

    public Invoice setTotal(double total) {
        this.total = total;
        return this;
    }

    public double getRemaining() {
        return remaining;
    }

    public Invoice setRemaining(double remaining) {
        this.remaining = remaining;
        return this;
    }

    public static Invoice fromJson(String json){
        Invoice invoice = null;
        try {
            JSONObject object = new JSONObject(json);
            invoice = new Invoice().setId(object.getString("id"))
                    .setCreatedAt(object.getString("created_at"))
                    .setRemaining(object.getDouble("remaining"))
                    .setTotal(object.getDouble("total"));
            if (object.has("products")) invoice.setProducts(new ArrayList<Product>());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return invoice;
    }

    public static ArrayList<Invoice> fromJsonArray(String json){
        ArrayList<Invoice> invoices = null;
        Invoice invoice;
        try {
            JSONArray array = new JSONArray(json);
            invoices = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++) {
                // TODO: I know I know this somehow stupid hhhhhhhhhhhhh but I need some time ....
                invoice = fromJson(array.getJSONObject(i).toString());
                invoices.add(invoice);

            }
        } catch (JSONException e) {e.printStackTrace();}
        return invoices;
    }
}
