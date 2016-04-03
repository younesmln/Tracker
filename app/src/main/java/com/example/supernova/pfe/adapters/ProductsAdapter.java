package com.example.supernova.pfe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supernova.pfe.R;
import com.example.supernova.pfe.data.models.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyClass> {
    public static String TAG = "ADAPTER";
    public static LayoutInflater mInflater;
    OnItemClickListener mItemClickListener;
    ArrayList<Product> data;

    public ProductsAdapter(Context context, ArrayList<Product> data) {
        mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    public List<Product> getData(){
        return this.data;
    }

    public Product getItem(int id){
        return this.data.get(id);
    }

    public double getTotal(){
        double total = 0;
        for (Product product : this.data) total += (product.getPrice() * product.getCountToBuy());
        return total;
    }

    public void add(Product p){
        this.data.add(p);
        notifyItemInserted(this.data.size()-1);
    }

    @Override
    public MyClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.recycle_row_product, parent, false);
        MyClass holder = new MyClass(v);
        return holder;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyClass holder, final int position) {
        Product current = data.get(position);
        holder.setValues(current.getLabel(), current.getCountToBuy(), current.getPrice(),
                current.getPrice()*current.getCountToBuy());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.prodcut_name_textview)
        public TextView textViewLabel;
        @Bind(R.id.product_count_textview)
        public TextView textViewCount;
        @Bind(R.id.product_price_textview)
        public TextView textViewPrice;
        @Bind(R.id.product_totalprice_textview)
        public TextView textViewTotal;

        public MyClass(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //imageView.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        public void setValues(String label, int count, double price, double total) {
            this.textViewLabel.setText(label);
            this.textViewCount.setText(String.valueOf(count));
            this.textViewPrice.setText(String.valueOf(price));
            this.textViewTotal.setText(String.valueOf(total));
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        /*
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Item remove")
                    .setMessage("Do you really want to remove this Item! ")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete(getAdapterPosition());
                        }
                    }).show();
        }*/
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}