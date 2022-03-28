package com.example.mybrowser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import java.util.List;

public class myDbHandlerBook extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="bookmarks.db";
    public static final String TABLE_BOOKMARK="bookmarks";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_NAME="url";
    private List<String> dbstring;

    public myDbHandlerBook(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + TABLE_BOOKMARK + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT)";

        db.execSQL ( query );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL ("DROP TABLE IF EXISTS " +TABLE_BOOKMARK );
        onCreate ( db );
    }

    public void addUrl (WebView websites){
        ContentValues values=new ContentValues();
        values.put(COLUMN_NAME ,websites.getUrl());
        SQLiteDatabase db=getWritableDatabase();
        db.insert ( TABLE_BOOKMARK, null, values );
        db.close();
    }

    public void deleteUrl(String urlName){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL( "DELETE FROM "+ TABLE_BOOKMARK +"WHERE "+COLUMN_NAME+"=\""+urlName +"\";");
    }

    public List<String> databaseToString()
    {
        SQLiteDatabase db=getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKMARK;

        Cursor c=db.rawQuery(query, null);
        c.moveToFirst();
        int i=0;
        if(c.moveToNext()) {
            do {
                if (c.getString(c.getColumnIndexOrThrow(COLUMN_NAME)) != null) {

                    String bstring = "";
                    bstring += c.getString(c.getColumnIndexOrThrow("url"));
                    dbstring.add(bstring);
                }
            }while (c.moveToNext());
        }
        return dbstring;
    }
}
