package com.sohu.dreamplan.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by lizha on 2015/8/25.
 */
public class MyButton extends Button {

    String myText;
    Paint paint;
    public int cellWidth;
    int cellHeight;
    float percent;

    public String getMyText() {
        return myText;
    }

    public void setMyText(String myText) {
        this.myText = myText;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public MyButton(Context context) {
        this(context, null);
    }

    public MyButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        myText = "设置文本";
        percent = 0.4f;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //文本居中
        int x = (int) (cellWidth / 2.0f - paint.measureText(myText) / 2.0f);
        // 获取文本的高度
        Rect bounds = new Rect();// 矩形
        paint.getTextBounds(myText, 0, myText.length(), bounds);
        int textHeight = bounds.height();
        //高度可调
        int y = (int)(cellHeight*percent);
        paint.setTextSize(cellHeight*0.25f);



        canvas.drawText(myText,x,y,paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cellWidth = getMeasuredWidth();
        cellHeight = getMeasuredHeight();
    }
}
