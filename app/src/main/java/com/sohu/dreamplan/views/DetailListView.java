package com.sohu.dreamplan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sohu.dreamplan.R;
import com.sohu.dreamplan.utils.LoadImageUtils;

/**
 * Created by lizha on 2015/8/31.
 */
public class DetailListView extends ListView{
    private View headerView;
    public DetailListView(Context context) {
        this(context, null);
    }

    public DetailListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        headerView = View.inflate(getContext(), R.layout.detaillistview_header, null);

        addHeaderView(headerView);
    }

    public void initHeaderView(String title, String rem, String per,String pic,ImageLoader imageLoader,DisplayImageOptions options){
        TextView dreamTitle = (TextView) headerView.findViewById(R.id.detail_dreamname);
        TextView remainingDays = (TextView) headerView.findViewById(R.id.detail_remaining_time);
        TextView percent = (TextView) headerView.findViewById(R.id.detail_percent);
        ImageView picture = (ImageView) headerView.findViewById(R.id.detail_picture);
        dreamTitle.setText(title);
        remainingDays.setText(rem);
        percent.setText(per);

        LoadImageUtils.loadImageAsyn(pic,picture,imageLoader,options);





    }





}
