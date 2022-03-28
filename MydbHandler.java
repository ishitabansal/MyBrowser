package com.example.mybrowser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import java.util.List;

public class MydbHandler extends SQLiteOpenHelper {
    
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="sites.db";
    public static final String TABLE_SITES="sites";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_NAME="url";
    private List<String> dbstring;

    public MydbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + TABLE_SITES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT)";

        db.execSQL ( query );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL ("DROP TABLE IF EXISTS " +TABLE_SITES );
        onCreate ( db );

    }
    
    public void addUrl (Websites websites){
        ContentValues values=new ContentValues();
        values.put(COLUMN_NAME ,websites.get_url());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert ( TABLE_SITES, null, values );
        db.close();
    }
    
    public void deleteUrl(String urlName){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL( "DELETE FROM "+ TABLE_SITES +"WHERE "+COLUMN_NAME+"=\""+urlName +"\";");
    }
    
    public List<String>  databaseToString()
    {
        SQLiteDatabase db=getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SITES;

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
