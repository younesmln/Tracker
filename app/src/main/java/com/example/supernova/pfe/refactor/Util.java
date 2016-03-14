package com.example.supernova.pfe.refactor;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Callable;

public class Util {
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
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
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
}
