package com.example.supernova.pfe.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.supernova.pfe.R;
import com.example.supernova.pfe.adapters.InvoicesAdapter;
import com.example.supernova.pfe.data.models.Client;
import com.example.supernova.pfe.data.models.Invoice;
import com.example.supernova.pfe.refactor.Util;
import com.example.supernova.pfe.tasks.ApiAccess;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static String ITEM_ID = "item_id";

    private Client client;
    @Bind(R.id.client_fulll_name)
    TextView full_name;
    @Bind(R.id.client_phone)
    TextView phone;
    @Bind(R.id.invoices_recycle_view)
    RecyclerView invoiceRecycle;

    public ClientDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().getString(ARG_ITEM_ID) != null) {
             ITEM_ID = getArguments().getString(ARG_ITEM_ID);
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
        getClientAndInvoices(ITEM_ID);
        // Show the dummy content as text in a TextView.
        /*if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.client_detail)).setText(mItem.details);
        }*/

        return rootView;
    }

    public void getClientAndInvoices(String id){
        String result = null;
        Uri uri = Uri.parse(Util.host).buildUpon()
                .appendPath("clients").appendPath(id).appendPath("include_invoices.json").build();
        try {
            result = new ApiAccess().setMethod("get").setUri(uri).execute().get();
        } catch (Exception e) { e.printStackTrace(); }
        Log.w("detailsFrag", ""+result);
        client = Client.fromJson(result);
        setupActionBarWithValues();
        setupInvoiceRecycleView();

    }

    public void setupActionBarWithValues(){
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(client.getFullName());
        }
    }

    public void setupInvoiceRecycleView(){
        InvoicesAdapter invoicesAdapter = new InvoicesAdapter(getContext(), client.getInvoices());
        invoiceRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        invoiceRecycle.setAdapter(invoicesAdapter);
        invoicesAdapter.SetOnItemClickListener(new InvoicesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.v(InvoicesAdapter.TAG, "oui from "+position);
            }
        });
    }
}
