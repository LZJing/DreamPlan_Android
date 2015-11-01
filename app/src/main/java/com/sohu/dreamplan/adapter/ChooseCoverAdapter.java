package com.sohu.dreamplan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.sohu.dreamplan.R;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by lizha on 2015/8/29.
 */
public class ChooseCoverAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list = new ArrayList<String>();
    private Vector<Boolean> isChecked = new Vector<Boolean>();
    private String[] picsInRes = {"cover_pic1.jpg","cover_pic2.jpg",
            "cover_pic3.jpg","cover_pic4.jpg",
            "cover_pic5.jpg","cover_pic6.jpg",
            "cover_pic7.jpg","cover_pic8.jpg",
            "cover_pic9.jpg","cover_pic10.jpg"};
    private String[] picsInResThum = {"cover_pic_540_01.jpg","cover_pic_540_02.jpg",
            "cover_pic_540_03.jpg","cover_pic_540_04.jpg",
            "cover_pic_540_05.jpg","cover_pic_540_06.jpg",
            "cover_pic_540_07.jpg","cover_pic_540_08.jpg",
            "cover_pic_540_09.jpg","cover_pic_540_10.jpg"};



    private int lastPostition = -1;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public ChooseCoverAdapter(Context context, ArrayList<String> list,ImageLoader imageLoader,DisplayImageOptions options) {
        this.context = context;
        this.list = list;
        this.imageLoader = imageLoader;
        this.options = options;

        for (int i =0;i<10;i++){
            isChecked.add(false);
        }
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = ViewGroup.inflate(context, R.layout.covergridview_item, null);
            viewHolder.choose = (ImageView) convertView.findViewById(R.id.choose_this);
            viewHolder.mask = (TextView) convertView.findViewById(R.id.blackmask30);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.choose_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }
        //viewHolder.image.setImageResource(picsNames[position]);
        //Log.e("load","加载。。。"+position);
        String assetsUrl = ImageDownloader.Scheme.ASSETS.wrap(picsInResThum[position]);
        imageLoader.displayImage(assetsUrl,viewHolder.image,options );
        if(isChecked.elementAt(position)){
            viewHolder.choose.setVisibility(View.VISIBLE);
            viewHolder.mask.setVisibility(View.VISIBLE);
        }else{
            viewHolder.choose.setVisibility(View.GONE);
            viewHolder.mask.setVisibility(View.GONE);

    }

        return convertView;
    }
    class ViewHolder{
        public ImageView image;
        public TextView mask;
        public ImageView choose;
    }

    //刷新UI状态，当点击Item是调用，更新选中状态
    public void changeState(int position){
        if(lastPostition !=-1)
            isChecked.setElementAt(false,lastPostition);//如果之前未选中，则选中
        isChecked.setElementAt(!isChecked.elementAt(position),position);//选中反转
        lastPostition = position;
        Log.e("load","update"+position);
        notifyDataSetChanged();  //通知适配器进行更新

    }

}
