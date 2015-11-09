package com.pfe.projet.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pfe.projet.tracker.data.CRUD;
import com.pfe.projet.tracker.data.LocationContract;
import com.pfe.projet.tracker.data.LocationInfo;
import com.pfe.projet.tracker.tasks.DbAccess;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            DbAccess.OnTaskCompleteListener, LocationListener{

    private final String TAG = MainActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location location;

    TextView location_textView, uuid_textView, time_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .add(R.id.placeholder, new MainFragment()).commit();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(2 * 1000)
                .setInterval(9 * 1000);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("**********************", "GoogleApiClient connected");
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (this.location == null) {
            Log.i(TAG, "*************************************************************************");
            Log.i(TAG, "********************  ERROR-ERROR-ERROR-ERROR-ERROR  ********************");
            Log.i(TAG, "*************************************************************************");

        } else {
            new DbAccess(this).execute();
        }
    }

    @Override
    public void onTaskComplete(SQLiteDatabase db){
        //Location l = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        String uuid = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        long time = new Date().getTime()/1000;

        location_textView.setText(location.getLatitude() + " | " + location.getLongitude());
        uuid_textView.setText(uuid);
        time_textView.setText(time + "");
        LocationInfo l = new LocationInfo()
                .setUuid(uuid)
                .setLg(location.getLongitude())
                .setLt(location.getLatitude())
                .setTime(time);
        long idd = new CRUD(db).insert(l);
        if (idd > 0) Toast.makeText(this, "c'est bon  -  id : " + idd, Toast.LENGTH_LONG).show();
        else Toast.makeText(this, "c'est pas bon", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        location_textView = (TextView) findViewById(R.id.location);
        uuid_textView = (TextView) findViewById(R.id.uuid);
        time_textView = (TextView) findViewById(R.id.time);
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //locationUpdate(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("**********************", "GoogleApiClient connection has been suspend");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("**********************", "GoogleApiClient connection has failed");
    }
}
