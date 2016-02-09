package com.pfe.projet.tracker.background;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.pfe.projet.tracker.MainActivity;
import com.pfe.projet.tracker.R;
import com.pfe.projet.tracker.data.CRUD;
import com.pfe.projet.tracker.data.LocationDB;
import com.pfe.projet.tracker.data.LocationInfo;
import com.pfe.projet.tracker.tasks.SendLocation;

import java.util.Date;


public class LocationService extends IntentService {

    private int mId = 1;
    private int mIdd = 1;
    NotificationCompat.Builder mNotification;
    private LocationInfo mLocationInfo;
    private Location mLocation;

    private SQLiteDatabase mDb;

    public LocationService() {
        super("locationUpdates");
        mNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha)
                .setContentTitle("Location change !")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{400, 600, 900, 1100 })
                .setAutoCancel(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mLocation = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
        if (mLocation != null) {
            String uuid = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            long time = new Date().getTime()/1000;
            mLocationInfo = new LocationInfo()
                    .setUuid(uuid)
                    .setLg(mLocation.getLongitude())
                    .setLt(mLocation.getLatitude())
                    .setTime(time);
            if (mDb == null) mDb = new LocationDB(this).getWritableDatabase();
            long idd = new CRUD(mDb).insert(mLocationInfo);
            ++mIdd;
            String test;
            if (idd > 0){
                test = "insert with id : "+idd;
            }
            else{
                test = "bad news";
            }
            Intent ClickIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(ClickIntent);
            PendingIntent clickPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mNotification.setContentText(test)
                    .setContentIntent(clickPendingIntent)
                    .setNumber(mIdd);

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(mId, mNotification.build());
            new SendLocation(mLocationInfo).startSending();
        }
    }
}
