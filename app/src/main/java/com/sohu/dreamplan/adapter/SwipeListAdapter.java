package com.sohu.dreamplan.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.sohu.dreamplan.R;
import com.sohu.dreamplan.activity.DetailActivity;
import com.sohu.dreamplan.activity.MainActivity;
import com.sohu.dreamplan.activity.ModifyDreamActivity;
import com.sohu.dreamplan.activity.PublishActivity;
import com.sohu.dreamplan.bean.Dream;
import com.sohu.dreamplan.utils.Constant;
import com.sohu.dreamplan.utils.DateUtil;
import com.sohu.dreamplan.utils.Utils;
import com.sohu.dreamplan.views.SwipeLayout;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 自定义适配器
 */
public class SwipeListAdapter extends BaseAdapter {

	private MainActivity context;
	private ArrayList<Dream> dreamsList;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

	public SwipeListAdapter(MainActivity context, ArrayList<Dream> dreamsList,ImageLoader imageLoader,DisplayImageOptions options) {
		this.context = context;
		this.dreamsList = dreamsList;
		this.imageLoader = imageLoader;
		this.options = options;

	}

	@Override
	public int getCount() {
		return dreamsList.size();
	}

	@Override
	public Object getItem(int position) {
		return dreamsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = ViewGroup.inflate(context, R.layout.mainlistview_item, null);
			viewHolder.coverImage = (ImageView) convertView.findViewById(R.id.cover_image);
			viewHolder.cameraIcon = (ImageView) convertView.findViewById(R.id.camera_image);
			viewHolder.shareIcon = (ImageView) convertView.findViewById(R.id.share_image);
			viewHolder.dreamTopic = (TextView) convertView.findViewById(R.id.dream_title);
			viewHolder.remainingTime = (TextView) convertView.findViewById(R.id.remaining_time);
			viewHolder.percent = (TextView) convertView.findViewById(R.id.percent);
			viewHolder.set = (TextView) convertView.findViewById(R.id.tv_set);
			viewHolder.delete = (TextView) convertView.findViewById(R.id.tv_del);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();

		}
		//两种方法，设置带有位置信息的监听器
		viewHolder.cameraIcon.setTag(position);
		viewHolder.cameraIcon.setOnClickListener(new IconOnClickLisener());
		viewHolder.shareIcon.setTag(position);
		viewHolder.shareIcon.setOnClickListener(new IconOnClickLisener());
		viewHolder.set.setOnClickListener(new OnActionClickListener(position));
		viewHolder.delete.setOnClickListener(new OnActionClickListener(position));


		SwipeLayout view = (SwipeLayout) convertView;

		//关闭横向拉动（非平滑）
		view.close(false, false);

		//点击Item进入详细页，并添加本dream的ID
		view.getFrontView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DetailActivity.class);
				Dream dream = dreamsList.get(position);
				intent.putExtra("dream_id",dream.getId());
				context.startActivity(intent);
			}
		});

		view.setSwipeListener(mSwipeListener);
		Dream dream = dreamsList.get(position);
		viewHolder.dreamTopic.setText(dream.getDreamName());
		int days = DateUtil.getRemainingDays(dream.getEndTime());
		viewHolder.remainingTime.setText(String.valueOf(days));
		String assetsUrl = ImageDownloader.Scheme.ASSETS.wrap(dream.getCoverPic());
		imageLoader.displayImage(assetsUrl, viewHolder.coverImage, options);
		viewHolder.percent.setText(String.valueOf(dream.getPercent()));


		return view;
	}

	SwipeLayout.SwipeListener mSwipeListener = new SwipeLayout.SwipeListener() {
		@Override
		public void onOpen(SwipeLayout swipeLayout) {
			mUnClosedLayouts.add(swipeLayout);
		}

		@Override
		public void onClose(SwipeLayout swipeLayout) {
			mUnClosedLayouts.remove(swipeLayout);
		}

		@Override
		public void onStartClose(SwipeLayout swipeLayout) {
		}

		@Override
		public void onStartOpen(SwipeLayout swipeLayout) {
			closeAllLayout();
			mUnClosedLayouts.add(swipeLayout);
		}

	};

	
	public void closeAllLayout() {
		if(mUnClosedLayouts.size() == 0)
			return;
		
		for (SwipeLayout l : mUnClosedLayouts) {
			l.close(true, false);
		}
		mUnClosedLayouts.clear();
	}
	public void closeAllLayoutRightNow() {
		if(mUnClosedLayouts.size() == 0)
			return;

		for (SwipeLayout l : mUnClosedLayouts) {
			l.close(false, false);
		}
		mUnClosedLayouts.clear();
	}
	
	class ViewHolder {
		public ImageView coverImage;
		public ImageView shareIcon;
		public ImageView cameraIcon;
		public TextView dreamTopic;
		public TextView remainingTime;
		public TextView percent;
		public TextView set;
		public TextView delete;
	}

	//以下两种监听器可以合并
	class IconOnClickLisener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			Integer position = (Integer)v.getTag();
			switch (v.getId()){
				//点击照相机按钮，发布新内容，传入dream的ID和来自哪个Activity
				case R.id.camera_image:
					Intent intent = new Intent(context, PublishActivity.class);
					intent.putExtra("dream_id",dreamsList.get(position).getId());
					intent.putExtra("from","main");
					context.startActivity(intent);
					break;
				//点击分享按钮
				case R.id.share_image:
					Utils.showToast(context,"touch share " + position);
					break;
			}
		}
	}
	class OnActionClickListener implements View.OnClickListener{

		private int position;

		public OnActionClickListener(int position) {
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.tv_set:
					int dreamId = dreamsList.get(position).getId();
					final Intent intent = new Intent(context, ModifyDreamActivity.class);
					intent.putExtra("dream_id", dreamId);
					context.startActivityForResult(intent, Constant.PUBLISH_NEW_ITEM_REQUEST_CODE);
					closeAllLayoutRightNow();
					break;
				case R.id.tv_del:
					closeAllLayout();
					AlertDialog.Builder normalDia=new AlertDialog.Builder(context);
					//normalDia.setIcon(R.drawable.ic_launcher);
					normalDia.setTitle("警告");
					normalDia.setMessage("确定删除梦想吗？\n"+dreamsList.get(position).getDreamName());
					normalDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dreamsList.get(position).deleteDreamById(context);
							dreamsList.remove(position);
							notifyDataSetChanged();
							dialog.dismiss();
						}
					});
					normalDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					normalDia.create().show();
					break;
			}
		}
	}

}