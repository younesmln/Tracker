package com.pfe.projet.tracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pfe.projet.tracker.data.LocationContract.LocationEntry;

public class LocationDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "location.db";
    public static final int DATABASE_VERSION = 1;

    public LocationDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                LocationEntry.COLUMN_UUID + " TEXT NOT NULL, "+
                LocationEntry.COLUMN_LATITUDE + " REAL NOT NULL, "+
                LocationEntry.COLUMN_LONGITUDE + " REAL NOT NULL, "+
                LocationEntry.COLUMN_TIME + " INTEGER NOT NULL );";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        this.onCreate(db);
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }
}
