package com.pfe.projet.tracker;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .add(R.id.placeholder_main, new MainFragment()).commit();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("**********************", "GoogleApiClient connected");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);


    }

    private void locationM(Location location) {
        String s = "Lt:" + location.getLatitude() + "  -  Lg:" + location.getLongitude();

        ((TextView) findViewById(R.id.placeholder_main).findViewById(R.id.yesy)).setText(s);
    }

    private void location(Location l) {
        ((TextView) findViewById(R.id.placeholder_main).findViewById(R.id.yesy)).setText(l.toString());
        String uuid = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        Log.i("**********************", uuid);
        ((TextView) findViewById(R.id.placeholder_main).findViewById(R.id.uuid)).setText(uuid);
    }

    @Override
    public void onLocationChanged(Location location) {
        location(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("**********************", "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("**********************", "GoogleApiClient connection has failed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
