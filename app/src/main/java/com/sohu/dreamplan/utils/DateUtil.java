package com.sohu.dreamplan.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lizha on 2015/8/31.
 */
public class DateUtil {

    //字符串转日期
    public static Date ConverToDate(String strDate)
    {
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    //计算获得剩余天数
    public static int getRemainingDays(String endTime){
        Date today = new Date();
        Date endDay = ConverToDate(endTime);
        long remainingDays = (endDay.getTime()-today.getTime())/(24 * 60 * 60 * 1000);
        if(remainingDays<0){
            remainingDays = 0;
        }
        return (int)remainingDays;
    }

    //绝对时间字符转某格式时间字符串
    public static String MillionToDate(String format,String millis){
        //"yyyy-MM-dd hh:mm:ss"
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date(Long.parseLong(millis));

        return formatter.format(date);
    }
}
