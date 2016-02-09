package com.pfe.projet.tracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
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
import com.pfe.projet.tracker.data.LocationDB;
import com.pfe.projet.tracker.refactor.MyPreferences;
import com.pfe.projet.tracker.tasks.DbAccess;
import com.pfe.projet.tracker.background.LocationService;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            DbAccess.OnTaskCompleteListener, LocationListener,
            OnMapReadyCallback {

    private final String TAG = MainActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location location;
    private GoogleMap mMap;
    private Marker mMarker;
    private PendingIntent mPendingIntent;
    private boolean mActivated;

    private TextView location_textView, uuid_textView, time_textView;
    private Switch activator;

    private final int LocationUpdateInterval = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .replace(R.id.placeholder, new MainFragment()).commit();

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
    }

    @Override
    public void onTaskComplete(SQLiteDatabase db){
        String uuid = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        long time = new Date().getTime()/1000;
        updateUi(time, uuid);
        setUpMap();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        new DbAccess(this).execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.zoomBy(13));
    }

    public void setUpMap(){
        LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMarker == null )
            mMarker = mMap.addMarker(new MarkerOptions().position(myPos).title("you are here !"));
        else mMarker.setPosition(myPos);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(myPos));
    }

    public void updateUi(Long time, String uuid) {
        location_textView.setText(location.getLatitude() + " | " + location.getLongitude());
        uuid_textView.setText(uuid);
        time_textView.setText(time + "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        location_textView = (TextView) findViewById(R.id.location);
        uuid_textView = (TextView) findViewById(R.id.uuid);
        time_textView = (TextView) findViewById(R.id.time);
        activator = ((Switch) findViewById(R.id.location_state_btn));

        mActivated = MyPreferences.getInstance(getBaseContext())
                .retrieveDataBool(getString(R.string.activated));

        mGoogleApiClient.connect();
        activator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startLocationUpdates();
                    startLocationService();
                    MyPreferences.getInstance(getBaseContext())
                            .saveDataBool(getString(R.string.activated), true);
                } else {
                    stopLocationUpdates();
                    stopLocationService();
                    MyPreferences.getInstance(getBaseContext())
                            .saveDataBool(getString(R.string.activated), false);
                }
            }
        });
        if (mActivated){
            activator.setChecked(true);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void startLocationService() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, mPendingIntent);
        }
    }

    protected void stopLocationService() {
        LocationServices.FusedLocationApi
                .removeLocationUpdates(mGoogleApiClient, mPendingIntent);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG, "GoogleApiClient connected");
        if (mActivated){
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }
}
