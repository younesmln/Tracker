package com.example.supernova.pfe.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.supernova.pfe.R;
import com.example.supernova.pfe.data.Client;
import com.example.supernova.pfe.retrofit.Clients;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Client client;
    @Bind(R.id.client_fulll_name)
    TextView full_name;
    @Bind(R.id.client_phone)
    TextView phone;

    public ClientDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().getString(ARG_ITEM_ID) != null) {
            String id = getArguments().getString(ARG_ITEM_ID);
            getClient(id);
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.client_detail, container, false);
        ButterKnife.bind(this, rootView);
        // Show the dummy content as text in a TextView.
        /*if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.client_detail)).setText(mItem.details);
        }*/

        return rootView;
    }

    public void getClient(String id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Clients.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.v("Master", "inside the setup ............");
        Call<Client> clientCall = null;
        clientCall = retrofit.create(Clients.class).getClient(id);
        clientCall.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                Log.v("details", response.body().getId()+" "+response.body().getFullName());
                client = response.body();
                //getActivity().findViewById()
                setupActionBarWithValues();
                full_name.setText(client.getFullName());
                phone.setText(client.getPhone());
            }
            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.v("details", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void setupActionBarWithValues(){
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(client.getFullName());
        }
    }
}
