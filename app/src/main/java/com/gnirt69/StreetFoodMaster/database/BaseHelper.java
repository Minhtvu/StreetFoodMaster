package com.gnirt69.StreetFoodMaster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Connor on 4/3/2017.
 */

public class BaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public BaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creates the database
        db.execSQL("create table " + DbSchema.StoreTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                DbSchema.StoreTable.Cols.UUID + ", " +
                DbSchema.StoreTable.Cols.NAME + ", " +
                DbSchema.StoreTable.Cols.LOCATION + ", " +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}