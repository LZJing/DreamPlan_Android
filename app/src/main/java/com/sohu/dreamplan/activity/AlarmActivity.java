package com.sohu.dreamplan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;

import java.io.IOException;

/**
 * Created by lizha on 2015/8/26.
 */
public class AlarmActivity extends Activity {

    MediaPlayer mp=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示对话框
        new AlertDialog.Builder(AlarmActivity.this).
                setTitle(this.getIntent().getStringExtra("Title")).//设置标题
                setMessage(this.getIntent().getStringExtra("Sentence")).//设置内容
                setPositiveButton("看看梦想", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mp!=null){
                            mp.stop();
                        }
                        Intent intent = new Intent(AlarmActivity.this,MainActivity.class);
                        AlarmActivity.this.startActivity(intent);
                        AlarmActivity.this.finish();
                    }
                }).
                setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlarmActivity.this.finish();//关闭Activity
                    }
                }).create().show();


        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getRingerMode()==AudioManager.RINGER_MODE_NORMAL){
            ring();
            vibrate();
        }else if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
            vibrate();
        }



    }


    public void ring(){

            mp = new MediaPlayer();
            mp.reset();
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            try {
                mp.setDataSource(this,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();

    }
    public void vibrate(){
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);//震动0.5s

        //第一个参数，指代一个震动的频率数组。每两个为一组，每组的第一个为等待时间，第二个为震动时间。
        // 比如 [2000,500,100,400],会先等待2000毫秒，震动500，再等待100，震动400
        //第二个参数，repest指代从 第几个索引（第一个数组参数） 的位置开始循环震动。
        //会一直保持循环，我们需要用 vibrator.cancel()主动终止
        //vibrator.vibrate(new long[]{300,500},0);
    }




}
