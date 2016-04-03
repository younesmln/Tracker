package com.example.supernova.pfe.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.supernova.pfe.R;
import com.example.supernova.pfe.adapters.ProductsAdapter;
import com.example.supernova.pfe.data.models.Product;
import com.example.supernova.pfe.fragments.AddProductFragment;
import com.example.supernova.pfe.fragments.ClientDetailFragment;
import com.example.supernova.pfe.refactor.Util;
import com.example.supernova.pfe.tasks.ApiAccess;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class NewInvoiceActivity extends AppCompatActivity implements AddProductFragment.AddFragmentFinish {
    public static String TAG = NewInvoiceActivity.class.getSimpleName();

    public String client_id;
    @Bind(R.id.product_recycle)
    RecyclerView productRecycler;
    @Bind(R.id.total_text)
    TextView totalTextView;
    public static ArrayList<Product> productsList;
    public static ArrayList<CharSequence> productsListLabel;
    ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_invoice);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        client_id = getIntent().getStringExtra(ClientDetailFragment.ARG_ITEM_ID);
        Log.i(TAG, client_id+"   !");
        productsList = getProducts();
        if (productsList != null && productsList.size() > 0){
            productsListLabel = new ArrayList<>();
            for (Product p : productsList) {
                productsListLabel.add(p.getLabel());
            }
        }
        setupProductRecyclerView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
                if (fragment != null) fTransaction.remove(fragment);
                fTransaction.addToBackStack(null);
                AddProductFragment resultFragment = AddProductFragment.newInstance();
                resultFragment.show(fTransaction, "dialog");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setupProductRecyclerView(){
        ArrayList<Product> products = new ArrayList<>();
        adapter = new ProductsAdapter(this, products);
        productRecycler.setLayoutManager(new LinearLayoutManager(this));
        productRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(NewInvoiceActivity.this, adapter.getData().get(position).getCountToBuy()+" !", Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){// | ItemTouchHelper.RIGHT
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.delete(viewHolder.getAdapterPosition());
                totalTextView.setText(String.format(getString(R.string.total_products_display), adapter.getTotal()));
            }
        });
        touchHelper.attachToRecyclerView(productRecycler);
    }

    @Override
    public void onDismissAddFragment(Product p) {
        Toast.makeText(NewInvoiceActivity.this, p.getLabel() + " " + p.getCountToBuy(), Toast.LENGTH_SHORT).show();
        adapter.add(p);
        totalTextView.setText(String.format(getString(R.string.total_products_display), adapter.getTotal()));
    }

    public ArrayList<Product> getProducts(){
        ArrayList<Product> products;
        Uri uri = Uri.parse(Util.host).buildUpon().appendPath("products.json").build();
        String result = null;
        try {
            result = new ApiAccess().setMethod("get").setUri(uri).execute().get();
        }catch (Exception e){e.printStackTrace();}
        products = Product.fromJsonArray(result);
        Log.i(TAG, products.size()+" !!!");
        return products;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_invoice_menu, menu);
        return true;//return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_done:
                Toast.makeText(NewInvoiceActivity.this, "done with "+adapter.getItemCount(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
