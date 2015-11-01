package com.sohu.dreamplan.views;



import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;


public class FrontLayout extends FrameLayout {

	private SwipeLayoutInterface mISwipeLayout;

	public FrontLayout(Context context) {
		super(context);
	}

	public FrontLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FrontLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setSwipeLayout(SwipeLayoutInterface mSwipeLayout){
		this.mISwipeLayout = mSwipeLayout;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.i("touch", "FrontLayout onInterceptTouchEvent-- action=" + ev.getAction());
		if(mISwipeLayout.getCurrentStatus() == SwipeLayout.Status.Close){
			return super.onInterceptTouchEvent(ev);
		}else {
			return true;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("touch", "FrontLayout onTouchEvent-- action=" + event.getAction());
		if(mISwipeLayout.getCurrentStatus() == SwipeLayout.Status.Close){
			return super.onTouchEvent(event);
		}else {
			if(event.getActionMasked() == MotionEvent.ACTION_UP){
				mISwipeLayout.close();
			}
			return true;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.i("touch", "FrontLayout dispatchTouchEvent-- action=" + ev.getAction());

		return super.dispatchTouchEvent(ev);
	}
}
