package com.sohu.dreamplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sohu.dreamplan.R;
import com.sohu.dreamplan.adapter.ChooseCoverAdapter;
import com.sohu.dreamplan.utils.Utils;

import java.util.ArrayList;

/**
 * Created by lizha on 2015/8/25.
 */
public class ChooseCoverActivity extends Activity{
    private String[] picsInRes = {"cover_pic1.jpg","cover_pic2.jpg",
            "cover_pic3.jpg","cover_pic4.jpg",
            "cover_pic5.jpg","cover_pic6.jpg",
            "cover_pic7.jpg","cover_pic8.jpg",
            "cover_pic9.jpg","cover_pic10.jpg"};
    private int picLocation;
    GridView gridView;
    ArrayList<String> list;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ImageView cancle;

    private int[] icon = {R.drawable.cover_pic1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosecover);
        gridView = (GridView) findViewById(R.id.gridview);
        cancle = (ImageView) findViewById(R.id.choose_cancle);
        list = new ArrayList<String>();

        for(int i = 0;i<10;i++){
            list.add("Hello");
        }
        imageLoader=ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(R.drawable.camera_publish)
                .showImageOnFail(R.drawable.camera_publish)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(200))// 淡入
                .build();
        final ChooseCoverAdapter chooseCoverAdapter = new ChooseCoverAdapter(ChooseCoverActivity.this,list,imageLoader,options);


        gridView.setAdapter(chooseCoverAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.showToast(ChooseCoverActivity.this, "" + position);
                view.findViewById(R.id.blackmask30).setVisibility(View.VISIBLE);
                view.findViewById(R.id.choose_this).setVisibility(View.VISIBLE);
                //chooseCoverAdapter.changeState(position);
                picLocation = position;
                Intent intent = new Intent();
                intent.putExtra("pic_name", picsInRes[position]);
                setResult(1, intent);
                finish();
                //百度查询 gridview单选
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(-1, intent);
                ChooseCoverActivity.this.finish();
            }
        });




    }
}
