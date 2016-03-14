package com.example.supernova.pfe.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class CRUD {
    private final static String TAG = CRUD.class.getSimpleName();

    private SQLiteDatabase mDb;

    public CRUD(SQLiteDatabase db){
        this.mDb = db;
    }

    public long insert(LocationInfo l) {
        ContentValues values = new ContentValues();
        long status = 0;
        values.put(LocationContract.LocationEntry.COLUMN_UUID, l.getUuid());
        values.put(LocationContract.LocationEntry.COLUMN_LONGITUDE, l.getLg());
        values.put(LocationContract.LocationEntry.COLUMN_LATITUDE, l.getLt());
        values.put(LocationContract.LocationEntry.COLUMN_TIME, l.getTime());
        status = mDb.insert(LocationContract.LocationEntry.TABLE_NAME, null, values);
        return status;
    }

}
