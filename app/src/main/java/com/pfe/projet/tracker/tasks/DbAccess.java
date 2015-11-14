package com.pfe.projet.tracker.tasks;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.pfe.projet.tracker.data.LocationDB;

import java.lang.ref.WeakReference;

public class DbAccess extends AsyncTask<Void, Void, SQLiteDatabase> {

    WeakReference<OnTaskCompleteListener> call;

    public DbAccess(OnTaskCompleteListener call){
        this.call  = new WeakReference<>(call);
    }

    @Override
    protected void onPostExecute(SQLiteDatabase db) {
        this.call.get().onTaskComplete(db);
    }

    @Override
    protected SQLiteDatabase doInBackground(Void...params) {
        LocationDB db = new LocationDB((Context) call.get());
        return db.getWritableDatabase();
    }

    public interface OnTaskCompleteListener {

        void onTaskComplete(SQLiteDatabase db);
    }
}
