package com.example.supernova.pfe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.supernova.pfe.R;

import com.example.supernova.pfe.adapters.ClientsAdapter;
import com.example.supernova.pfe.data.Client;
import com.example.supernova.pfe.fragments.ClientDetailFragment;
import com.example.supernova.pfe.retrofit.Clients;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An activity representing a list of Clients. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ClientDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ClientListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClientListActivity.this, NewClientActivity.class));
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.client_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.client_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        final List<Client> clients = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Clients.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<List<Client>> clients2 = retrofit.create(Clients.class).getClients();
        final ClientsAdapter clientsAdapter = new ClientsAdapter(this, clients);
        //clientsAdapter.notifyItemInserted();
        clients2.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                clients.addAll(response.body());
                clientsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Log.v("tag", "error from on failure");
                t.printStackTrace();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(clientsAdapter);
        clientsAdapter.SetOnItemClickListener(new ClientsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.v("Master", "ouiiiiii from " + position);
                String id = clientsAdapter.getItem(position).getId();
                Log.v("listClients", id+" ");
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
}
////////////////////////////////////////////////////////



