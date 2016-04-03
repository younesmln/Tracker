package com.example.supernova.pfe.data.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Product implements Cloneable{
    String id;
    String label;
    String desc;
    int count;
    int countToBuy;
    double price;

    public String getId() {
        return id;
    }

    public Product setId(String id) {
        this.id = id;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Product setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public Product setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public int getCountToBuy() {
        return countToBuy;
    }

    public Product setCountToBuy(int countToBuy) {
        this.countToBuy = countToBuy;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Product setCount(int count) {
        this.count = count;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Product setPrice(double price) {
        this.price = price;
        return this;
    }

    public static Product fromJson(String json){
        Product product = null;
        try {
            JSONObject object = new JSONObject(json);
            product = new Product().setId(object.getString("id"))
                    .setPrice(object.getDouble("price"))
                    .setLabel(object.getString("label"))
                    .setDesc(object.getString("desc"))
                    .setCount(object.getInt("count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return product;
    }

    public static ArrayList<Product> fromJsonArray(String json){
        ArrayList<Product> products = null;
        Product product;
        try {
            JSONArray array = new JSONArray(json);
            products = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++) {
                // TODO: I know I know this somehow stupid hhhhhhhhhhhhh but I need some time ....
                product = fromJson(array.getJSONObject(i).toString());
                products.add(product);

            }
        } catch (JSONException e) {e.printStackTrace();}
        return products;
    }

    public Object clone() {
        Product tmp = null;
        try {
            tmp = (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return tmp;
    }
}
