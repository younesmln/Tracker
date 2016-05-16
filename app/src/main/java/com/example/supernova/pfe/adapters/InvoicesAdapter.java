package com.example.supernova.pfe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.supernova.pfe.R;
import com.example.supernova.pfe.data.models.Invoice;
import com.example.supernova.pfe.data.models.Invoice;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.MyClass> {
    public static String TAG = "ADAPTER";
    public static LayoutInflater mInflater;
    OnItemClickListener mItemClickListener;
    List<Invoice> data = Collections.emptyList();

    public InvoicesAdapter(Context context, List<Invoice> data) {
        mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    public Invoice getItem(int id){
        return this.data.get(id);
    }

    @Override
    public MyClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.recycle_row_invoice, parent, false);
        MyClass holder = new MyClass(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyClass holder, final int position) {
        Invoice current = data.get(position);
        holder.setValues(current.getCreatedAt(), current.getTotal(), current.getRemaining());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.textview_date)
        public TextView textViewDate;
        @Bind(R.id.textview_total)
        public TextView textViewTotal;
        @Bind(R.id.textview_remaining)
        public TextView textViewRemaining;

        public MyClass(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //imageView.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setValues(String date, double total, double remaining) {
            try {
                DateTime dt = new DateTime(date);
                this.textViewDate.setText(String.format(Locale.getDefault(),"%d/%d/%d", dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth()));
            }catch (Exception e){
                this.textViewDate.setText(date);
            }
            this.textViewTotal.setText(String.valueOf(total));
            this.textViewRemaining.setText(String.valueOf(remaining));
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