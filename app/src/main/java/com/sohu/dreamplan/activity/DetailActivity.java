package com.sohu.dreamplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sohu.dreamplan.R;
import com.sohu.dreamplan.adapter.DetailListViewAdapter;
import com.sohu.dreamplan.bean.DreamItem;
import com.sohu.dreamplan.helper.MyOpenHelper;
import com.sohu.dreamplan.utils.DateUtil;
import com.sohu.dreamplan.views.DetailListView;

import java.util.ArrayList;

/**
 * Created by lizha on 2015/8/29.
 */
public class DetailActivity extends Activity {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    DetailListView listView;
    ArrayList<DreamItem> dreamItems= new ArrayList<DreamItem>();
    int id,percent;
    String endTime,picName,name;
    private ImageButton publishNewBt,backBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getIntentDate();
        getSQLDate();
        initView();
        initDate();
    }
    public void initView(){
        listView = (DetailListView) findViewById(R.id.detail_listview);
        imageLoader=ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.test)
                .showImageForEmptyUri(R.drawable.test)
                .showImageOnFail(R.drawable.test)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        publishNewBt = (ImageButton) findViewById(R.id.detail_publish_new);
        publishNewBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, PublishActivity.class);
                intent.putExtra("dream_id", id);
                intent.putExtra("from", "detail");
                //startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
        backBt = (ImageButton) findViewById(R.id.detail_back);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.finish();
            }
        });
    }

    public void getIntentDate(){
        Intent intent = getIntent();
        id = intent.getIntExtra("dream_id", 0);

    }
    public void getSQLDate(){
        //从数据库获得该dream的信息；
        String dream_id = String.valueOf(id);
        String[] s = {dream_id};
        MyOpenHelper myOpenHelper = new MyOpenHelper(DetailActivity.this,"dream_plan.db",null,1);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("dreams", null, "_id = ?", s, null, null, "_id DESC");
        while(cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndex("dream_name"));
            endTime = cursor.getString(cursor.getColumnIndex("end_time"));
            picName = cursor.getString(cursor.getColumnIndex("cover_pic"));
            percent = cursor.getInt(cursor.getColumnIndex("percent"));
        }
        cursor.close();
        //从数据库获得dreamItems的数据

        Cursor cursor2 = db.query("dreamitems", null, "dream_id = ?", s, null, null, "_id DESC");
        while(cursor2.moveToNext()){
            DreamItem dreamItem = new DreamItem();
            dreamItem.setId(cursor2.getInt(cursor2.getColumnIndex("_id")));
            dreamItem.setTalk(cursor2.getString(cursor2.getColumnIndex("talk")));
            dreamItem.setPublishTime(cursor2.getString(cursor2.getColumnIndex("publish_time")));
            dreamItem.setImageURI(cursor2.getString(cursor2.getColumnIndex("picture_name")));
            dreamItem.setDreamId(cursor2.getInt(cursor2.getColumnIndex("dream_id")));
            Log.e("dreamItem", dreamItem.toString());
            dreamItems.add(dreamItem);
        }
        cursor2.close();
        db.close();
    }
    public void initDate(){
        listView.setAdapter(new DetailListViewAdapter(this, dreamItems,imageLoader,options));
        int days = DateUtil.getRemainingDays(endTime);
        listView.initHeaderView(name, String.valueOf(days), String.valueOf(percent),picName,imageLoader,options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1&& resultCode == 1){
            dreamItems.clear();
            getSQLDate();
            initDate();
        }
    }
}
