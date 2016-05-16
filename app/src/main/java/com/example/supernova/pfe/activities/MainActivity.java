package com.example.supernova.pfe.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.supernova.pfe.R;
import com.example.supernova.pfe.background.BroadcastResult;
import com.example.supernova.pfe.data.models.Client;
import com.example.supernova.pfe.fragments.ClientDetailFragment;
import com.example.supernova.pfe.refactor.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.supernova.pfe.data.LocationDB;
import com.example.supernova.pfe.refactor.MyPreferences;
import com.example.supernova.pfe.background.LocationService;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.WeakHashMap;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        OnMapReadyCallback {

    private final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location location;
    private GoogleMap mMap;
    private Marker mMarker;
    private HashMap<Marker, String> markersReference;
    private PendingIntent mPendingIntent;
    private BroadcastResult mBroadcast;
    private boolean connectedToInternet = false;
    private boolean mRegisitredReciever = false;
    private boolean mActivated;
    private SwitchCompat activator;
    private final int LocationUpdateInterval = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*getFragmentManager().beginTransaction()
                .replace(R.id.placeholder, new MainFragment()).commit();*/
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(this.LocationUpdateInterval * 1000)
                .setFastestInterval(this.LocationUpdateInterval * 1000);
        mPendingIntent = PendingIntent
                .getService(this, 0, new Intent(this, LocationService.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
        MyPreferences.getInstance(getBaseContext())
                .saveDataBool(getString(R.string.first_time_launch_key), true);
        this.deleteDatabase(LocationDB.DATABASE_NAME);
        mActivated = MyPreferences.getInstance(getBaseContext())
                .retrieveDataBool(getString(R.string.activated));
        mGoogleApiClient.connect();
        mBroadcast = new BroadcastResult(this);
        connectedToInternet = Util.isConnected();
        if(connectedToInternet){
            Log.i(TAG, "connected without any probleme :)");
        }else{
            Log.i(TAG, "not-connected man !!!!!!!!!");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.custom_menu);
        MenuItemCompat.setActionView(item, R.layout.action_bar);
        //item.setActionView(R.layout.action_bar);
        activator = ((SwitchCompat) item.getActionView().findViewById(R.id.location_state_btn));
        if (mActivated) {
            activator.setChecked(true);
        }
        activator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "checked ...................");
                    startLocationUpdates();
                    startLocationService();
                    MyPreferences.getInstance(getBaseContext())
                            .saveDataBool(getString(R.string.activated), true);
                } else {
                    Log.v(TAG, "unchecked .................");
                    stopLocationUpdates();
                    stopLocationService();
                    MyPreferences.getInstance(getBaseContext())
                            .saveDataBool(getString(R.string.activated), false);
                }
            }
        });
        return true;//return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_clients:
                //before startActivity(new Intent(this, ClientsActivity.class));
                startActivity(new Intent(this, ClientListActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        String uuid = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        long time = new Date().getTime() / 1000;
        setUpMap();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v(TAG, "ready ***************************");
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.zoomBy(13));
    }
    public void setUpMap() {
        LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMarker == null) {
            mMarker = mMap.addMarker(new MarkerOptions().position(myPos).title("you are here !"));
        }
        else mMarker.setPosition(myPos);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(myPos));
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!mRegisitredReciever) {
            registerReceiver(mBroadcast, new IntentFilter(BroadcastResult.IntentFilterString));
            mRegisitredReciever = true;
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        if (mRegisitredReciever) {
            unregisterReceiver(mBroadcast);
            mRegisitredReciever = false;
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG, "GoogleApiClient connected");
        if (activator != null) {
            activator.setEnabled(true);
            if (mActivated) {
                startLocationUpdates();
            }
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");

        if (activator != null) {activator.setEnabled(false);}
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (activator != null ) activator.setEnabled(false);
        Log.i(TAG, "GoogleApiClient connection has failed");
    }


    protected void startLocationUpdates() {
        if (Util.checkLocationPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void startLocationService() {
        if (Util.checkLocationPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, mPendingIntent);
        }
    }
    protected void stopLocationService() {
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, mPendingIntent);
    }
    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
    public void refreshPositions(String result){
        Client[] clients = new Gson().fromJson(result, Client[].class);
        if (clients != null) {
            if (markersReference != null && markersReference.size() > 0) {
                for (Marker m : markersReference.keySet()){
                    m.remove();
                }
            }
            markersReference = new HashMap<>();
            Toast.makeText(this, clients.length +" elements(s)",Toast.LENGTH_SHORT).show();
            for (Client c: clients) {
                if (c.getLocation() == null) continue;
                String title = c.getFullName();
                LatLng location = new LatLng(c.getLocation()[1], c.getLocation()[0]);
                MarkerOptions markerOption = new MarkerOptions().position(location).title(title)
                        .icon(Util.convertHsvRgbColor("#1122ec"));
                Marker marker = mMap.addMarker(markerOption);
                markersReference.put(marker, c.getId());
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //Toast.makeText(MainActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                        String client_id = markersReference.get(marker);
                        if (client_id == null || client_id.equals("-1")) return false;
                        Intent detailIntent = new Intent(MainActivity.this, ClientDetailActivity.class);
                        detailIntent.putExtra(ClientDetailFragment.ARG_ITEM_ID, client_id);
                        startActivity(detailIntent);
                        return true;
                    }
                });
            }
        }
    }
}
