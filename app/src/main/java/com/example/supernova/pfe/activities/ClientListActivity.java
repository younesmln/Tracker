package com.example.supernova.pfe.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import com.example.supernova.pfe.R;
import com.example.supernova.pfe.adapters.ClientsAdapter;
import com.example.supernova.pfe.data.models.Client;
import com.example.supernova.pfe.fragments.ClientDetailFragment;
import com.example.supernova.pfe.fragments.ClientFragmentOperation;
import com.example.supernova.pfe.refactor.Util;
import com.example.supernova.pfe.retrofit.Clients;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    @Bind(R.id.client_list)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    ClientsAdapter clientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        if (!Util.isConnected()){
            fab.setEnabled(false);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("clients");
                if (fragment != null) fTransaction.remove(fragment);
                fTransaction.addToBackStack(null);
                ClientFragmentOperation resultFragment = ClientFragmentOperation.newInstance(false);
                resultFragment.show(fTransaction, "clients");
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupRecyclerView();

        if (findViewById(R.id.client_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        if (getIntent().getBooleanExtra("from_invoice", false)){
            Snackbar.make(fab, getString(R.string.invoice_success), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        clientsAdapter = new ClientsAdapter(this, new ArrayList<Client>());
        refreshData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(clientsAdapter);
        clientsAdapter.SetOnItemClickListener(new ClientsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.v("Master", "ouiiiiii from " + position);
                String id = clientsAdapter.getItem(position).getId();
                Log.v("listClients", id + " ");
                if (mTwoPane) {
                    // In two-pane mode, show the detail view in this activity by
                    // adding or replacing the detail fragment using a
                    // fragment transaction.
                    Bundle arguments = new Bundle();
                    arguments.putString(ClientDetailFragment.ARG_ITEM_ID, String.valueOf(id));
                    ClientDetailFragment fragment = new ClientDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction().replace(R.id.client_detail_container, fragment).commit();
                } else {
                    // In single-pane mode, simply start the detail activity
                    // for the selected item ID.
                    Intent detailIntent = new Intent(ClientListActivity.this, ClientDetailActivity.class);
                    detailIntent.putExtra(ClientDetailFragment.ARG_ITEM_ID, String.valueOf(id));
                    startActivity(detailIntent);
                }
            }
        });
    }

    public void refreshData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Clients.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Call<List<Client>> clients2 = retrofit.create(Clients.class).getClients();
        clients2.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                clientsAdapter.getData().clear();
                clientsAdapter.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Snackbar.make(fab, getString(R.string.error_fetching), Snackbar.LENGTH_LONG).show();
                Log.v("tag", "error from on failure");
                t.printStackTrace();
            }
        });
    }

    public void onDismissAddFragment(boolean state){
        if (state){
            refreshData();
            Snackbar.make(fab, getString(R.string.client_success), Snackbar.LENGTH_LONG).show();
        }
    }

}
////////////////////////////////////////////////////////



