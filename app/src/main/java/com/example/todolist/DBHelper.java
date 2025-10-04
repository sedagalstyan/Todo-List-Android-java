package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo_list";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAMES = "list";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "text";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAMES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES);
        onCreate(db);
    }

    public long insertName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        long id = db.insert(TABLE_NAMES, null, values);
        db.close();
        return id;
    }

    public ArrayList<String> getAllNames() {
        ArrayList<String> namesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAMES, null);

        if (cursor.moveToFirst()) {
            do {
                namesList.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return namesList;
    }

    public void deleteAllNames() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAMES, null, null);
        db.close();
    }
}
