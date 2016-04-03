package com.example.supernova.pfe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.supernova.pfe.R;
import com.example.supernova.pfe.data.models.Client;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.MyClass> {
    public static String TAG = "ADAPTER";
    public static LayoutInflater mInflater;
    boolean mTwoPane;
    OnItemClickListener mItemClickListener;
    List<Client> data = Collections.emptyList();

    public ClientsAdapter(Context context, List<Client> data) {
        mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    public Client getItem(int id){
        return this.data.get(id);
    }

    public List<Client> getData(){
        return this.data;
    }

    @Override
    public MyClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.recycle_row, parent, false);
        MyClass holder = new MyClass(v);
        return holder;
    }

    public void addAll(List<Client> clients){
        this.data.addAll(clients);
        notifyItemRangeInserted(this.data.size(), clients.size());
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MyClass holder, final int position) {
        Client current = data.get(position);
        holder.setValues(R.drawable.common_full_open_on_phone, current.getFullName(),
                current.getPhone(), "right text");
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class MyClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.image)
        public ImageView imageView;
        @Bind(R.id.text_top)
        public TextView topText;
        @Bind(R.id.text_bottom)
        public TextView bottomText;
        @Bind(R.id.text_right)
        public TextView rightText;

        public MyClass(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //imageView.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setValues(int resId, String topText, String bottomText, String rightText) {
            this.imageView.setImageResource(resId);
            this.topText.setText(topText);
            this.bottomText.setText(bottomText);
            this.rightText.setText(rightText);
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
        void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}