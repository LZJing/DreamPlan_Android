package com.sohu.dreamplan;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.sohu.dreamplan.helper.MyOpenHelper;

/**
 * Created by lizha on 2015/8/27.
 */
public class TestCase extends AndroidTestCase {

    public void test(){

        //getContext() 获取虚拟上下文

        MyOpenHelper myOpenHelper = new MyOpenHelper(getContext(),"dream_plan.db",null,1);
        //如果数据库不存在，先创建一个数据库
        //获取一个可读可写的数据库对象
        SQLiteDatabase sqLiteDatabase = myOpenHelper.getWritableDatabase();
        //如果存储空间满了，那么返回只读数据库对象
        //数据库默认存储在内部存储空间上
        //SQLiteDatabase database1 = myOpenHelper.getReadableDatabase();


    }
}
