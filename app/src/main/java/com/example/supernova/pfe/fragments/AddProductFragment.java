package com.example.supernova.pfe.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.Toast;

import com.example.supernova.pfe.R;
import com.example.supernova.pfe.activities.NewInvoiceActivity;
import com.example.supernova.pfe.data.models.Product;


import butterknife.Bind;
import butterknife.ButterKnife;

public class AddProductFragment extends DialogFragment {
    public static String TAG = AddProductFragment.class.getSimpleName();
    @Bind(R.id.productlist_spinner)
    Spinner productsspinner;
    @Bind(R.id.productlist_price_textview)
    TextView priceTextView;
    @Bind(R.id.productlist_count_textview)
    EditText countTextView;
    @Bind(R.id.add_product_button)
    Button addButton;

    public AddProductFragment() {
    }

    public static AddProductFragment newInstance(){
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        //args.putBoolean(IS_EDIT_ARG,isEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            //isEdit = getArguments().getBoolean(IS_EDIT_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_product_dialog, container, false);
        ButterKnife.bind(this, rootView);
        this.getDialog().setTitle("Add product");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof AddFragmentFinish ){
                    Product p = (Product) NewInvoiceActivity.productsList.get(productsspinner.getSelectedItemPosition()).clone();
                    p.setCountToBuy(Integer.parseInt(countTextView.getText().toString()));
                    ((AddFragmentFinish) activity).onDismissAddFragment(p);
                    AddProductFragment.this.dismiss();

                }else{
                    throw new ClassCastException("hosting activity must implements AddFragmentFinish");
                }
            }
        });
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, NewInvoiceActivity.productsListLabel);

        productsspinner.setAdapter(spinnerAdapter);
        productsspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double price = NewInvoiceActivity.productsList.get(position).getPrice();
                priceTextView.setText("price : "+price);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    public interface AddFragmentFinish {
        void onDismissAddFragment(Product p);
    }
}
