package com.sohu.dreamplan.views;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.sohu.dreamplan.R;

/**
 * Created by lizha on 2015/8/27.
 */
public class MainListView extends ListView {
    private int headerViewHeight;
    private View headerView,footerView;
    int downY;
    private final int PULL_REFRESH=0;
    private final int RELEASE_REFRESH=1;
    private final int REFRESHING=2;
    private int curState = 1;


    private GestureDetectorCompat mGestureDetector;
    private OnRefreshListener listener;
    public void setOnRefreshListener(OnRefreshListener listener){
        this.listener = listener;
    }
    public interface OnRefreshListener{
        void onPullAddNew();
    }

    public MainListView(Context context) {
        this(context, null);
    }

    public MainListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init


        headerView = View.inflate(getContext(), R.layout.mainlistview_header, null);
        footerView = View.inflate(getContext(), R.layout.mainlistview_footer, null);
        mGestureDetector = new GestureDetectorCompat(context, mOnGestureListener);

        addFirstView();
        addLastView();

    }

    private GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // 当横向移动距离小于纵向时，返回true
            return Math.abs(distanceX) < Math.abs(distanceY);
        }
    };



    

    public void addFirstView(){
        addHeaderView(headerView);
    }

    public void addLastView(){
        addFooterView(footerView);
    }

    public void removeLastView(){
        removeFooterView(footerView);
    }



}
