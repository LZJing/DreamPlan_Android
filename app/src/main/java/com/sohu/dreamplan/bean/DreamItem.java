package com.sohu.dreamplan.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sohu.dreamplan.helper.MyOpenHelper;

/**
 * Created by lizha on 2015/8/26.
 */
public class DreamItem {

    int id;
    String talk;
    String imageURI;
    String publishTime;
    int dreamId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getDreamId() {
        return dreamId;
    }

    public void setDreamId(int dreamId) {
        this.dreamId = dreamId;
    }

    public void saveToSQLite(Context context){

        MyOpenHelper myOpenHelper = new MyOpenHelper(context,"dream_plan.db",null,1);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("talk", talk);
        contentValues.put("picture_name", imageURI);
        contentValues.put("publish_time", publishTime);
        contentValues.put("dream_id", dreamId);

        db.insert("dreamitems",null,contentValues);
        db.close();

    }

    @Override
    public String toString() {

        return "id:"+id +"talk:"+talk + "imageURI:" +imageURI + "PublishTime:"+publishTime + "dreamId:"+dreamId;
    }
}
