package com.example.supernova.pfe.retrofit;

import com.example.supernova.pfe.data.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Clients {
    final String BASE_URL = "http://10.0.3.2:3000";

    @GET("clients.json")
    Call<List<Client>> getClients();

    @GET("clients/{client_id}.json")
    Call<Client> getClient(@Path("client_id") String client_id);

    @POST("clients")
    Call<Client> newClient(@Body Client client);
}
