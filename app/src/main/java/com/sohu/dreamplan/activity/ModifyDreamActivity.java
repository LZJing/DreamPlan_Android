package com.sohu.dreamplan.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.sohu.dreamplan.R;
import com.sohu.dreamplan.bean.Dream;
import com.sohu.dreamplan.broadcastreceiver.AlarmReceiver;
import com.sohu.dreamplan.helper.MyOpenHelper;
import com.sohu.dreamplan.utils.LoadImageUtils;
import com.sohu.dreamplan.utils.Utils;
import com.sohu.dreamplan.views.MyButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by lizha on 2015/8/24.
 */
public class ModifyDreamActivity extends Activity implements View.OnClickListener {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    //显示的梦想的信息
    private int dreamId,percent,hasAlarm;
    private String dreamName,startTime,endTime,createTime,alarmTime,picName;

    int picNum;
    private MyButton startTimeBtn,endTimeBtn;
    private EditText inputDreamEt;
    private TextView chooseCover;
    private Calendar calendar;
    private Switch isOpenAlarm;
    private ImageButton ok,cancle;
    private Button setAlarmBtn;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    private SeekBar seekBar;
    private TextView prograss_percent;
    private ImageView coverPicIv;
    TextView modifyTitle;


    int[] picsNames = {R.drawable.cover_pic_1080_01,R.drawable.cover_pic_1080_02,
            R.drawable.cover_pic_1080_03,R.drawable.cover_pic_1080_04,
            R.drawable.cover_pic_1080_05,R.drawable.cover_pic_1080_06,
            R.drawable.cover_pic_1080_07,R.drawable.cover_pic_1080_08,
            R.drawable.cover_pic_1080_09,R.drawable.cover_pic_1080_10

    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDate();
        setContentView(R.layout.activity_addnewdream);
        coverPicIv = (ImageView) findViewById(R.id.add_cover_pic);
        modifyTitle = (TextView) findViewById(R.id.modify_title);
        modifyTitle.setText("设置梦想");
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
                .showImageOnLoading(R.drawable.test)
                .showImageForEmptyUri(R.drawable.test)
                .showImageOnFail(R.drawable.test)
                .cacheInMemory(true)
                .cacheOnDisk(true)
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
                if (isChecked) {
                    setAlarmBtn.setClickable(true);
                    setAlarmBtn.setTextColor(Color.BLACK);
                } else {
                    setAlarmBtn.setClickable(false);
                    setAlarmBtn.setTextColor(Color.GRAY);
                }

            }
        });

        //picNum = new Random().nextInt(10);

        inputDreamEt.setText(dreamName);
        startTimeBtn.setText(startTime);
        endTimeBtn.setText(endTime);
        if(hasAlarm == 1){
            isOpenAlarm.setVisibility(View.VISIBLE);
            isOpenAlarm.setChecked(hasAlarm==1?true:false);
            setAlarmBtn.setText(alarmTime);
        }
        LoadImageUtils.loadImageAsyn(picName, coverPicIv, imageLoader, options);
        seekBar.setProgress(percent);
    }

    private void initDate() {
        Intent intent = getIntent();
        dreamId = intent.getIntExtra("dream_id",0);
        //从数据库获得该dream的信息；
        String dream_id = String.valueOf(dreamId);
        String[] s = {dream_id};
        MyOpenHelper myOpenHelper = new MyOpenHelper(ModifyDreamActivity.this,"dream_plan.db",null,1);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("dreams", null, "_id = ?", s, null, null, "_id DESC");
        while(cursor.moveToNext()){
            dreamName = cursor.getString(cursor.getColumnIndex("dream_name"));
            startTime =cursor.getString(cursor.getColumnIndex("start_time"));
            endTime = cursor.getString(cursor.getColumnIndex("end_time"));
            alarmTime = cursor.getString(cursor.getColumnIndex("alarm"));
            picName = cursor.getString(cursor.getColumnIndex("cover_pic"));
            hasAlarm = cursor.getInt(cursor.getColumnIndex("has_alarm"));
            percent = cursor.getInt(cursor.getColumnIndex("percent"));
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.starttime_btn:
                //点击后显示出日期选择器，默认的日期为caledar，当前日期
                DatePickerDialog startDatePicker = new DatePickerDialog(ModifyDreamActivity.this,onStartDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                startDatePicker.setTitle("选择梦想的开始时间");
                startDatePicker.show();
                break;
            case R.id.endtime_btn:
                DatePickerDialog endDatePicker = new DatePickerDialog(ModifyDreamActivity.this,onEndDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                endDatePicker.setTitle("选择梦想的计划完成时间");
                endDatePicker.show();
                break;
            case R.id.choosecover_tv:

                Intent intent = new Intent(ModifyDreamActivity.this, ChooseCoverActivity.class);
                startActivityForResult(intent,1);

                //startActivity(intent);
                break;
            case R.id.setalarm:

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ModifyDreamActivity.this,
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
                Log.e("update", inputDreamEt.getText().toString());

                myDream.setId(dreamId);
                myDream.setStartTime(startTime);
                myDream.setEndTime(endTime);
                myDream.setHasAlarm(isOpenAlarm.isChecked());
                myDream.setAlarmTime(alarmTime);
//                myDream.setCreateTime(createTime);
                myDream.setCoverPic(picName);
                myDream.setPercent(percent);
                myDream.updateToSQLite(this);
                Utils.showToast(this,"更新"+dreamId);
                Intent intentBack = new Intent();
                setResult(2, intentBack);
                ModifyDreamActivity.this.finish();

                break;
            case R.id.cancel_adddream:
                Intent intentCancle = new Intent();
                setResult(-1, intentCancle);
                ModifyDreamActivity.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            picName = data.getStringExtra("pic_name");
            LoadImageUtils.loadImageAsyn(picName,coverPicIv,imageLoader,options);
            //coverPicIv.setImageResource(picsNames[picNum]);
        }
    }

    private void setAlarm(Calendar calendar){
        //Step1.注册AlarmReceiver广播接收器
        Intent intent = new Intent(ModifyDreamActivity.this, AlarmReceiver.class);    //创建Intent对象
        intent.putExtra("Title","梦想记");
        intent.putExtra("Sentence","梦想的名字+自定义");
        //Step2.设定触发时的操作
        pendingIntent = PendingIntent.getBroadcast(ModifyDreamActivity.this, 0, intent, 0);    //创建PendingIntent

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
        Utils.showToast(ModifyDreamActivity.this, "距离提醒还有" + waitShow);//提示用户
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
