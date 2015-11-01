package com.sohu.dreamplan.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sohu.dreamplan.R;
import com.sohu.dreamplan.bean.Dream;
import com.sohu.dreamplan.broadcastreceiver.AlarmReceiver;
import com.sohu.dreamplan.utils.Constant;
import com.sohu.dreamplan.utils.LoadImageUtils;
import com.sohu.dreamplan.utils.Utils;
import com.sohu.dreamplan.views.MyButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by lizha on 2015/8/24.
 */
public class AddNewDreamActivity extends Activity implements View.OnClickListener {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    int picNum;
    private MyButton startTimeBtn,endTimeBtn;
    private EditText inputDreamEt;
    private TextView chooseCover;
    private Calendar calendar;


    private String startTime,endTime,createTime,alarmTime,picName;
    private Switch isOpenAlarm;
    private ImageButton ok,cancle;
    //闹钟配置
    private Button setAlarmBtn;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    private SeekBar seekBar;
    private int percent;
    private TextView prograss_percent;
    private int coverPic;
    private ImageView coverPicIv;


    int[] picsNames = {R.drawable.cover_pic_1080_01,R.drawable.cover_pic_1080_02,
            R.drawable.cover_pic_1080_03,R.drawable.cover_pic_1080_04,
            R.drawable.cover_pic_1080_05,R.drawable.cover_pic_1080_06,
            R.drawable.cover_pic_1080_07,R.drawable.cover_pic_1080_08,
            R.drawable.cover_pic_1080_09,R.drawable.cover_pic_1080_10

    };

    private String[] picsInRes = {"cover_pic1.jpg","cover_pic2.jpg",
            "cover_pic3.jpg","cover_pic4.jpg",
            "cover_pic5.jpg","cover_pic6.jpg",
            "cover_pic7.jpg","cover_pic8.jpg",
            "cover_pic9.jpg","cover_pic10.jpg"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewdream);
        findViewById(R.id.addnew_rootview).setOnClickListener(this);


        coverPicIv = (ImageView) findViewById(R.id.add_cover_pic);



        startTimeBtn = (MyButton) findViewById(R.id.starttime_btn);
        startTimeBtn.setMyText("开始时间");
        endTimeBtn = (MyButton) findViewById(R.id.endtime_btn);
        endTimeBtn.setMyText("完成时间");
        startTimeBtn.setOnClickListener(this);
        endTimeBtn.setOnClickListener(this);
        inputDreamEt =(EditText)findViewById(R.id.inputdream_et);
        chooseCover = (TextView) findViewById(R.id.choosecover_tv);
        chooseCover.setOnClickListener(this);

        ok = (ImageButton) findViewById(R.id.ok_adddream);
        ok.setOnClickListener(this);
        cancle = (ImageButton) findViewById(R.id.cancel_adddream);
        cancle.setOnClickListener(this);
        setAlarmBtn = (Button) findViewById(R.id.setalarm);
        setAlarmBtn.setOnClickListener(this);
        prograss_percent = (TextView) findViewById(R.id.prograss_percent);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prograss_percent.setText(String.valueOf(progress));
                percent = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        imageLoader=ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(R.drawable.test)
                .showImageOnFail(R.drawable.test)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        //获得系统日期
        calendar =Calendar.getInstance();
        createTime = calToString("yyyy.MM.dd",calendar);
        //更新UI,开始时间默认为当前时间,
        startTime = createTime;
        startTimeBtn.setText(startTime);


        isOpenAlarm = (Switch) findViewById(R.id.isOpenAlarm);
        isOpenAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setAlarmBtn.setClickable(true);
                    setAlarmBtn.setTextColor(Color.BLACK);
                }else{
                    setAlarmBtn.setClickable(false);
                    setAlarmBtn.setTextColor(Color.GRAY);
                }

            }
        });

        //启动时随机加载一张封面
        picNum = new Random().nextInt(10);
        //coverPicIv.setImageResource(picsNames[picNum]);//同步加载方式，会比较慢一些
        picName = picsInRes[picNum];
        LoadImageUtils.loadImageAsyn(picName,coverPicIv,imageLoader,options);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.starttime_btn:
                hideKeyboard(v);//隐藏键盘
                //点击后显示出日期选择器，默认的日期为caledar，当前日期
                DatePickerDialog startDatePicker = new DatePickerDialog(AddNewDreamActivity.this,onStartDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                startDatePicker.setTitle("选择梦想的开始时间");
                startDatePicker.show();
                break;
            case R.id.endtime_btn:
                hideKeyboard(v);//隐藏键盘
                DatePickerDialog endDatePicker = new DatePickerDialog(AddNewDreamActivity.this,onEndDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                endDatePicker.setTitle("选择梦想的计划完成时间");
                endDatePicker.show();
                break;
            case R.id.choosecover_tv:

                Intent intent = new Intent(AddNewDreamActivity.this, ChooseCoverActivity.class);
                startActivityForResult(intent,1);

                break;
            case R.id.setalarm:

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddNewDreamActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);        //设置闹钟小时数
                                calendar.set(Calendar.MINUTE, minute);            //设置闹钟的分钟数
                                calendar.set(Calendar.SECOND, 0);                //设置闹钟的秒数
                                calendar.set(Calendar.MILLISECOND, 0);            //设置闹钟的毫秒数
                                alarmTime = calToString("yyyy-MM-dd HH:mm:ss",calendar);
                                updateAlarmUI(calendar);
                            }
                        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
                timePickerDialog.show();
                break;
            case R.id.ok_adddream:

                if(TextUtils.isEmpty(inputDreamEt.getText())){
                    Utils.showToast(this,"给梦想起个名字吧！");
                    break;
                }
                if(endTime == null){
                    Utils.showToast(this,"请填写完成时间");
                    break;
                }
                if(isOpenAlarm.isChecked()){
                    setAlarm(calendar);
                }

                Dream myDream = new Dream();
                myDream.setDreamName(inputDreamEt.getText().toString());
                myDream.setStartTime(startTime);
                myDream.setEndTime(endTime);
                myDream.setHasAlarm(isOpenAlarm.isChecked());
                myDream.setAlarmTime(alarmTime);
                myDream.setCreateTime(createTime);
                myDream.setCoverPic(picName);
                myDream.setPercent(percent);
                myDream.saveToSQLite(this);
                Intent intentBack = new Intent();
                setResult(Constant.ADD_NEW_DREAM_RESULT_CODE, intentBack);
                AddNewDreamActivity.this.finish();

                break;
            case R.id.cancel_adddream:
                Intent intentCancle = new Intent();
                setResult(Constant.ADD_NEW_DREAM_CANCLE_CODE, intentCancle);
                AddNewDreamActivity.this.finish();
                break;
            case R.id.addnew_rootview:
                hideKeyboard(v);
            default:
                break;
        }
    }

    public void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            picName = data.getStringExtra("pic_name");
            LoadImageUtils.loadImageAsyn(picName, coverPicIv, imageLoader,options);
            //coverPicIv.setImageResource(picsNames[picNum]);
        }
    }

    private void setAlarm(Calendar calendar){
        //Step1.注册AlarmReceiver广播接收器
        Intent intent = new Intent(AddNewDreamActivity.this, AlarmReceiver.class);    //创建Intent对象
        intent.putExtra("Title","梦想记");
        intent.putExtra("Sentence","梦想的名字+自定义");
        //Step2.设定触发时的操作
        pendingIntent = PendingIntent.getBroadcast(AddNewDreamActivity.this, 0, intent, 0);    //创建PendingIntent

        //Step3.获取全局定时器的服务管理器
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Step4.设定定时模式/触发时间
        //alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);        //设置闹钟
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+3000, pendingIntent);        //设置闹钟，当前时间就唤醒
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
    }

    private void updateAlarmUI(Calendar calendar){
        long getedTimeMills = calendar.getTimeInMillis();
        long sysTimeMills = System.currentTimeMillis();
        if(getedTimeMills<sysTimeMills){
            getedTimeMills = getedTimeMills+24*60*60*1000;
        }
        String waitShow = null;
        long waitMinute = (getedTimeMills - sysTimeMills)/1000/60;
        waitShow =waitMinute+ "分钟";
        if(waitMinute>60){
            waitShow = waitMinute/60+"小时";
        }
        //设置完成后更新UI，显示一个Toast，Button上显示提醒时间，显示打开开关
        Utils.showToast(AddNewDreamActivity.this, "距离提醒还有" + waitShow);//提示用户
        setAlarmBtn.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "每天");
        isOpenAlarm.setVisibility(View.VISIBLE);
        isOpenAlarm.setChecked(true);
    }

    //开始时间选择完成之后触发
    private DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener(){


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String f2 = "%02d";
            startTime = String.valueOf(year)+"."+String.format(f2,monthOfYear+1)+"."+String.format(f2, dayOfMonth);
            startTimeBtn.setText(startTime);
        }
    };
    //完成时间选择完成之后触发
    private DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String f2 = "%02d";
            endTime = String.valueOf(year)+"."+String.format(f2,monthOfYear+1)+"."+String.format(f2,dayOfMonth);
            endTimeBtn.setText(endTime);
        }
    };

    private String calToString(String format,Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }





}
