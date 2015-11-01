package com.sohu.dreamplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sohu.dreamplan.R;
import com.sohu.dreamplan.adapter.SwipeListAdapter;
import com.sohu.dreamplan.bean.Dream;
import com.sohu.dreamplan.helper.MyOpenHelper;
import com.sohu.dreamplan.utils.Constant;
import com.sohu.dreamplan.utils.Utils;
import com.sohu.dreamplan.views.MainListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ArrayList<Dream> list = new ArrayList<Dream>();
    private MainListView mainListView;
    private SwipeListAdapter swipeListAdapter;


//    public MainListView getMainListView() {
//        return mainListView;
//    }
//
//    public void setMainListView(MainListView mainListView) {
//        this.mainListView = mainListView;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUIL();
        initView();
        initDate();
    }

    /**
     * 初始化Universal-Image-Loader第三方图片加载框架,使用之前应先配置Application类
     */
    private void initUIL() {
        imageLoader=ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(R.drawable.test)
                .showImageOnFail(R.drawable.test)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(500))// 淡入
                .build();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mainListView = (MainListView) findViewById(R.id.main_lv);
    }
    /**
     * 初始化数据
     *
     */
    private void initDate() {
        //从SQLite中拿到dreams的数据，装载到list中。
        MyOpenHelper myOpenHelper = new MyOpenHelper(this,"dream_plan.db",null,1);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("dreams", null, null, null, null, null, "_id DESC");
        while(cursor.moveToNext()){
            Dream dream = new Dream();
            dream.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            dream.setDreamName(cursor.getString(cursor.getColumnIndex("dream_name")));
            dream.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
            dream.setStartTime(cursor.getString(cursor.getColumnIndex("start_time")));
            dream.setEndTime(cursor.getString(cursor.getColumnIndex("end_time")));
            dream.setHasAlarm(cursor.getInt(cursor.getColumnIndex("has_alarm")) == 1);
            dream.setAlarmTime(cursor.getString(cursor.getColumnIndex("alarm")));
            dream.setCoverPic(cursor.getString(cursor.getColumnIndex("cover_pic")));
            dream.setPercent(cursor.getInt(cursor.getColumnIndex("percent")));
            list.add(dream);
        }
        cursor.close();
        db.close();
        //实例化适配器，list为数据源，传入两个UIL参数。
        swipeListAdapter = new SwipeListAdapter(MainActivity.this,list,imageLoader,options);
        mainListView.setAdapter(swipeListAdapter);
        mainListView.setOnRefreshListener(new MainListView.OnRefreshListener() {
            @Override
            public void onPullAddNew() {
                Utils.showToast(MainActivity.this,"Add new");
                Intent intent = new Intent(MainActivity.this, AddNewDreamActivity.class);
                startActivityForResult(intent, Constant.ADD_NEW_DREAM_REQUEST_CODE);
            }
        });
        mainListView.setOnItemClickListener(new MyOnItemClickListener());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //添加新梦想成功返回到此，刷新列表，不成功的resultCode是-1，不处理
        if(requestCode == Constant.ADD_NEW_DREAM_REQUEST_CODE && resultCode == Constant.ADD_NEW_DREAM_RESULT_CODE){
            list.clear();
            initDate();
        }else if(requestCode ==Constant.PUBLISH_NEW_ITEM_REQUEST_CODE && resultCode == Constant.PUBLISH_NEW_ITEM_RESULT_CODE){
            list.clear();
            initDate();
        }
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //只监听ListView的头项，其他项在Adapter中监听（为了解决左滑删除与点击的冲突）
            if(position == 0){
                Intent intent = new Intent(MainActivity.this, AddNewDreamActivity.class);
                startActivityForResult(intent, Constant.ADD_NEW_DREAM_REQUEST_CODE);
            }
        }
    }




}
