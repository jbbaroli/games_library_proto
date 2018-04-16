package com.jeanoliveira.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "demo";
    private static final String COL0 = "Id";
    private static final String COL1 = "name";
    private static final String COL2 = "image";
    private static final String COL3 = "url";
    private static final String COL4 = "description";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, " + COL1 + " TEXT, " + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String id, String game, String image, String url, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, id);
        contentValues.put(COL1, game);
        contentValues.put(COL2, image);
        contentValues.put(COL3, url);
        contentValues.put(COL4, description);

        Log.d(TAG, "addData: Adding " + game + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean removeGame(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL0 + "='" + id + "'");

        long result = db.delete(TABLE_NAME, COL0 +" = " + id, null);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getGameId(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL0 + " FROM " + TABLE_NAME +
                    " WHERE " + COL1 + "= '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getGame(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COL0 + "='" + id + "'";
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        return data;
    }

    public boolean findGame(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL0 + "='" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data.moveToFirst();
    }
}
