package com.example.supernova.pfe.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.supernova.pfe.R;
import com.example.supernova.pfe.activities.ClientListActivity;
import com.example.supernova.pfe.data.models.Client;
import com.example.supernova.pfe.refactor.Util;
import com.example.supernova.pfe.tasks.ApiAccess;
import com.example.supernova.pfe.tasks.ApiAccess2;
import com.example.supernova.pfe.tasks.Response;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientFragmentOperation extends DialogFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static String TAG = ClientFragmentOperation.class.getSimpleName();
    private static final int RESULT_PICK_CONTACT = 3333;
    public static String IS_EDIT_ARG = "edit";
    private boolean isEdit;

    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.f_name)
    EditText f_name;
    @Bind(R.id.l_name)
    EditText l_name;
    @Bind(R.id.position)
    CheckBox positionCheckBox;
    @Bind(R.id.add_client_button)
    Button submitButton;
    @Bind(R.id.import_from_contact_button)
    Button importFromContact;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    public ClientFragmentOperation() {
    }

    public static ClientFragmentOperation newInstance(boolean isEdit){
        ClientFragmentOperation fragment = new ClientFragmentOperation();
        Bundle args = new Bundle();
        args.putBoolean(IS_EDIT_ARG,isEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            isEdit = getArguments().getBoolean(IS_EDIT_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_new_client, container, false);
        ButterKnife.bind(this, rootView);
        importFromContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newClient();
            }
        });
        positionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return rootView;
    }

    public void newClient(){
        //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Response response = null;
        Client c = new Client().setF_name(f_name.getText().toString())
                .setL_name(l_name.getText().toString())
                .setPhone(phone.getText().toString());
        if (mGoogleApiClient.isConnected() && mLastLocation != null && positionCheckBox.isChecked()){
            c.setLocation(new double[]{ mLastLocation.getLongitude(), mLastLocation.getLatitude() });
        }
        try {
            Log.v(TAG, c.toJson().toString(4));
        } catch (JSONException e) {e.printStackTrace();}
        Uri uri = Uri.parse(Util.host).buildUpon().appendPath("clients.json").build();
        try {
            response = new ApiAccess2().setUri(uri).setMethod("post").setBody(c.toJson().toString()).execute().get();
        }catch (Exception e){
            Toast.makeText(getContext(), R.string.connection_problem, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Log.v(TAG, response.getCode()+"  "+response.getBody());
        if (response.isSuccess()){
            //true mean that an operation has been made and successfully finished
            ((ClientListActivity) getActivity()).onDismissAddFragment(true);
            this.dismiss();
        }else{
            Toast.makeText(getContext(), getString(R.string.client_failure), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case RESULT_PICK_CONTACT:
                    Log.e(TAG, "picking a contact successfully");
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
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int contactId = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            //phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
            while (phones.moveToNext())
            {
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
            }
            f_name.setText(name);
            //Log.i(TAG, phoneNo+" "+name);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Util.checkLocationPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                Toast.makeText(getContext(), mLastLocation.getLatitude() +" "+mLastLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                positionCheckBox.setEnabled(true);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        positionCheckBox.setEnabled(false);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        positionCheckBox.setEnabled(false);
    }
}
