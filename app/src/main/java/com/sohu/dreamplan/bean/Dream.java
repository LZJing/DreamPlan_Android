package com.sohu.dreamplan.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sohu.dreamplan.helper.MyOpenHelper;

import java.util.ArrayList;

/**
 * Created by lizha on 2015/8/26.
 */
public class Dream {

    int id;

    String dreamName;
    String startTime;
    String endTime;
    boolean hasAlarm;
    String alarmTime;//null代表没有闹钟
    String coverPic;
    int percent;
    ArrayList<DreamItem> dreamItems;
    String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDreamName() {
        return dreamName;
    }

    public void setDreamName(String dreamName) {
        this.dreamName = dreamName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public ArrayList<DreamItem> getDreamItems() {
        return dreamItems;
    }

    public void setDreamItems(ArrayList<DreamItem> dreamItems) {
        this.dreamItems = dreamItems;
    }

    public void saveToSQLite(Context context){
        MyOpenHelper myOpenHelper = new MyOpenHelper(context,"dream_plan.db",null,1);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dream_name",dreamName);
        contentValues.put("create_time",createTime);
        contentValues.put("start_time", startTime);
        contentValues.put("end_time", endTime);
        contentValues.put("has_alarm",hasAlarm?1:0);
        contentValues.put("alarm", alarmTime);
        contentValues.put("cover_pic", coverPic);
        contentValues.put("percent",percent);
        db.insert("dreams",null,contentValues);
        db.close();
    }
    public void updateToSQLite(Context context){
        MyOpenHelper myOpenHelper = new MyOpenHelper(context,"dream_plan.db",null,1);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dream_name",dreamName);
        contentValues.put("start_time", startTime);
        contentValues.put("end_time", endTime);
        contentValues.put("has_alarm",hasAlarm?1:0);
        contentValues.put("alarm", alarmTime);
        contentValues.put("cover_pic", coverPic);
        contentValues.put("percent",percent);
        db.update("dreams",contentValues,"_id = ?", new String[]{String.valueOf(id)});
        db.close();

    }
    public void deleteDreamById(Context context){
        MyOpenHelper myOpenHelper = new MyOpenHelper(context,"dream_plan.db",null,1);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        String dream_id = String.valueOf(id);
        String[] s = {dream_id};
        db.delete("dreams","_id = ?",s);
        db.delete("dreamitems","dream_id = ?",s);
        db.close();
    }

    @Override
    public String toString() {

        String s = dreamName+" " + createTime +" "+startTime+" "+endTime+" "+hasAlarm+" "+alarmTime+percent;
        return s;
    }
}
