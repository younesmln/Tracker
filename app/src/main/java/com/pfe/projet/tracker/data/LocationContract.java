package com.pfe.projet.tracker.data;

import android.provider.BaseColumns;

public class LocationContract {

    public class LocationEntry implements BaseColumns {
        public static final String COLUMN_UUID = "uuid";
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_TIME = "date";
    }
}
