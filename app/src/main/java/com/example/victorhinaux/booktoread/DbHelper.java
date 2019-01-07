package com.example.victorhinaux.booktoread;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="Database";
    private static final int DB_VER=2;
    public static final String DB_TABLE="Book";
    public static final String DB_TABLE2="BookRead";
    public static final String DB_COLUMN = "BookName";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);", DB_TABLE, DB_COLUMN);
        String query2 = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);", DB_TABLE2, DB_COLUMN);
        db.execSQL(query1);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewBook(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task);
        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void insertNewReadBook(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task);
        db.insertWithOnConflict(DB_TABLE2,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteBook(String book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN + " = ?", new String[]{book});
        db.close();
    }

    public void deleteReadBook(String book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE2, DB_COLUMN + " = ?", new String[]{book});
        db.close();
    }

    public ArrayList<String> getBookList() {
        ArrayList<String> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN},null,null,null,null,null);
        while(cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DB_COLUMN);
            bookList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return bookList;
    }

    public ArrayList<String> getReadBookList() {
        ArrayList<String> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE2, new String[]{DB_COLUMN},null,null,null,null,null);
        while(cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DB_COLUMN);
            bookList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return bookList;
    }
}
