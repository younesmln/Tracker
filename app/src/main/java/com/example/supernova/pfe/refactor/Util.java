package com.example.supernova.pfe.refactor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

public class Util {
    public static String host = "http://10.0.3.2:3000";
    public static String ping_host = "localhost";

    public static BitmapDescriptor convertHsvRgbColor(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    public static boolean isConnected(){
        boolean connected = false;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> result = executor.submit( new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 "+ping_host);
                    int exitValue = ipProcess.waitFor();
                    return (exitValue == 0);
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
        });
        try {
            connected = result.get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        executor.shutdown();
        return connected;
    }

    public static boolean checkLocationPermission(Context c, String permission){
        return !(ActivityCompat.checkSelfPermission(c, permission)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }
}
