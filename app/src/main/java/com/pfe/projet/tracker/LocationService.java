package com.pfe.projet.tracker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.location.FusedLocationProviderApi;

import java.util.Date;


public class LocationService extends IntentService {

    private int mId = 1;
    private int mIdd = 1;
    NotificationCompat.Builder mNotification;

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
        ++mIdd;
        Location l = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
        String test;
        if (l == null){
            test = "false";
        }
        else{
            test = "true";
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

        mNotification.setContentText("your loction : " + test)
                .setContentIntent(clickPendingIntent)
                .setNumber(mIdd);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mId, mNotification.build());
    }
}
