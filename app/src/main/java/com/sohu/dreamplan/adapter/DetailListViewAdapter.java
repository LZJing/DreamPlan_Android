package com.sohu.dreamplan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sohu.dreamplan.R;
import com.sohu.dreamplan.bean.DreamItem;
import com.sohu.dreamplan.utils.DateUtil;
import com.sohu.dreamplan.utils.LoadImageUtils;

import java.util.ArrayList;

/**
 * Created by lizha on 2015/8/29.
 */
public class DetailListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DreamItem> detailList;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    public DetailListViewAdapter(Context context, ArrayList<DreamItem> detailList,ImageLoader imageLoader,DisplayImageOptions options) {
        this.context = context;
        this.detailList = detailList;
        this.imageLoader = imageLoader;
        this.options = options;
    }

    @Override
    public int getCount() {
        return detailList.size();
    }

    @Override
    public Object getItem(int position) {
        return detailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = ViewGroup.inflate(context, R.layout.detaillistview_item, null);
            viewHolder.time = (TextView) convertView.findViewById(R.id.publish_time);
            viewHolder.talk = (TextView) convertView.findViewById(R.id.publish_talk);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.publish_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }

        DreamItem dreamItem = detailList.get(position);
        String time = DateUtil.MillionToDate("HH:mm yyyy/MM/dd", dreamItem.getPublishTime());
        viewHolder.time.setText(time);

        if(dreamItem.getTalk() == null ||dreamItem.getTalk().equals("")){

            viewHolder.talk.setVisibility(View.GONE);
        }else{
            viewHolder.talk.setText(dreamItem.getTalk());
        }

        if(dreamItem.getImageURI() == null ||dreamItem.getImageURI().equals("")){

            viewHolder.image.setVisibility(View.GONE);
        }else{
            LoadImageUtils.loadImageAsyn(dreamItem.getImageURI(), viewHolder.image, imageLoader, options);
        }



        return convertView;
    }
    class ViewHolder{
        public TextView time;
        public TextView talk;
        public ImageView image;
    }
}
