package com.sohu.dreamplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sohu.dreamplan.R;

/**
 * 欢迎页面
 * Created by lizha on 2015/8/24.
 */
public class WelcomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //启动页面，加载启动图，停留2s自动转到MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                //淡入淡出动画效果，不同Android版本支持不同。测试中4.0支持而5.1不支持
                //WelcomeActivity.this.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                finish();
            }
        }, 2000);




    }



}
