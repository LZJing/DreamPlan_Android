package com.sohu.dreamplan.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.sohu.dreamplan.R;
import com.sohu.dreamplan.bean.DreamItem;
import com.sohu.dreamplan.utils.Constant;
import com.sohu.dreamplan.utils.ImageOrientUtil;
import com.sohu.dreamplan.utils.ImageTools;
import com.sohu.dreamplan.views.SelectPicPopupWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by lizha on 2015/8/29.
 */
public class PublishActivity extends Activity {

    int dreamId;
    String fromActivity;
    private EditText publishText;
    private ImageView publishCamera;
    private ImageView publishImage;
    private ImageButton ok,cancle;
    //自定义的弹出框类
    SelectPicPopupWindow menuWindow;
    String picName,talkContent;

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDate();
    }

    private void initDate() {
        Intent intent = getIntent();
        dreamId = intent.getIntExtra("dream_id", 0);
        fromActivity = intent.getStringExtra("from");
    }

    private void initView() {
        setContentView(R.layout.activity_publish);
        publishCamera = (ImageView) findViewById(R.id.publish_camera);
        publishImage = (ImageView) findViewById(R.id.returnedPic);
        ok = (ImageButton) findViewById(R.id.ok_publish);
        cancle = (ImageButton) findViewById(R.id.cancel_publish);
        publishText = (EditText) findViewById(R.id.text_et);
        publishCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(PublishActivity.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(PublishActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talkContent = publishText.getText().toString();
                DreamItem dreamItem = new DreamItem();
                dreamItem.setTalk(talkContent);
                dreamItem.setImageURI(picName);
                dreamItem.setDreamId(dreamId);
                dreamItem.setPublishTime(String.valueOf(System.currentTimeMillis()));
                dreamItem.saveToSQLite(PublishActivity.this);
                if(fromActivity.equals("detail")){
                    Intent intent = new Intent();
                    setResult(1, intent);
                    PublishActivity.this.finish();
                }else if(fromActivity.equals("main")){
                    Intent intent = new Intent(PublishActivity.this,DetailActivity.class);
                    intent.putExtra("dream_id", dreamId);
                    startActivity(intent);
                    PublishActivity.this.finish();
                }


            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(-1, intent);
                PublishActivity.this.finish();
            }
        });

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    getImageFromCamera();
                    break;
                case R.id.btn_pick_photo:
                    getImageFromAlbum();
                    break;
                default:
                    break;
            }


        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE&&resultCode == -1) {
            ContentResolver resolver = getContentResolver();
            Uri originalUri = data.getData();
            int degrees = getOrientation(this, originalUri);
            try {
                //使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                if (photo != null) {
                    int scale = 1;
                    Log.e("scale",""+photo.getByteCount());
                    if((photo.getByteCount()/1024/8)>1000){//如果原图尺寸大于1M
                        scale = 5;
                    }else if(photo.getByteCount()>(500*1024*8)){
                        scale = 2;
                    }
                    Log.e("scale","scale"+scale);
                    //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                    Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / scale, photo.getHeight() / scale);
                    String name=  String.valueOf(System.currentTimeMillis());
                    picName = name+".jpg";
                    //根据拍摄角度旋转
                    Bitmap newMap = rotateImage(smallBitmap, degrees);
                    //保存到SD卡中
                    ImageTools.savePhotoToSDCard(newMap, Constant.picPath, picName);
                    //显示到界面
                    publishImage.setImageBitmap(newMap);
                    //释放原始图片占用的内存，防止out of memory异常发生
                    if(scale!=1){
                        photo.recycle();
                    }


                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            publishCamera.setVisibility(View.GONE);

        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA&&resultCode== -1) {

            int degree = ImageOrientUtil.readPictureDegree(Environment.getExternalStorageDirectory() + "/image.jpg");
            //将保存在本地的图片取出并缩小后显示在界面上
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
            Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / 5, bitmap.getHeight() / 5);
            bitmap.recycle();
            Bitmap bitmap1= ImageOrientUtil.rotate(newBitmap, degree);
            publishImage.setImageBitmap(bitmap1);
            String name=  String.valueOf(System.currentTimeMillis());
            picName = name+".jpg";
            ImageTools.savePhotoToSDCard(bitmap1,Constant.picPath , picName);
            publishCamera.setVisibility(View.GONE);
        }
    }

    public static int getOrientation(Context context, Uri photoUri) {
        int orientation = 0;
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() != 1) {
                return -1;
            }
            cursor.moveToFirst();
            orientation = cursor.getInt(0);
            cursor.close();
        }
        return orientation;
    }
    public static Bitmap rotateImage(Bitmap bmp, int degrees) {
        if (degrees != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }
        return bmp;
    }
    protected void getImageFromAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    protected void getImageFromCamera() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, REQUEST_CODE_CAPTURE_CAMEIA);
//            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
//            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

}


