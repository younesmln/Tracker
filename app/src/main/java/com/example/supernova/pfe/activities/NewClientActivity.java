package com.example.supernova.pfe.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

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

public class NewClientActivity extends AppCompatActivity  {
    public static String TAG = NewClientActivity.class.getSimpleName();
    private static final int RESULT_PICK_CONTACT = 3333;

    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.f_name)
    EditText f_name;
    @Bind(R.id.l_name)
    EditText l_name;
    @Bind(R.id.position)
    CheckBox position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);
        ButterKnife.bind(this);
    }

    public void newClient(View v){
        Client c = new Client().setF_name(f_name.getText().toString())
                .setL_name(l_name.getText().toString())
                .setPhone(phone.getText().toString());
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Clients.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<Client> createClient = retrofit.create(Clients.class).newClient(c);
        createClient.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                Log.v("creation S", "   "+response.body().getId());
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.v("creation", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void importClient(View v){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case RESULT_PICK_CONTACT:
                    pickContact(data);
                    break;
            }
        } else {
            Log.e(TAG, "error picking a contact ");
        }
    }

    public void pickContact(Intent data){
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int contactId = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            //phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
            while (phones.moveToNext())
            {
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
            }
            //Log.i(TAG, phoneNo+" "+name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
