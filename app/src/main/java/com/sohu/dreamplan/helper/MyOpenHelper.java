package com.sohu.dreamplan.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lizha on 2015/8/27.
 */
public class MyOpenHelper  extends SQLiteOpenHelper{
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        System.out.println("--->构造函数1");
    }

    //Cursor = ResultSet
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        //init;
        System.out.println("--->构造函数2");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists dreams(_id integer primary key autoincrement,dream_name text not null,create_time text not null, start_time text not null,end_time text not null,has_alarm integer not null,alarm text, cover_pic text not null,percent integer not null)");
        db.execSQL("create table if not exists dreamitems(_id integer primary key autoincrement,talk text , picture_name text, publish_time text,dream_id integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("--->onUpgrade");
    }
}
