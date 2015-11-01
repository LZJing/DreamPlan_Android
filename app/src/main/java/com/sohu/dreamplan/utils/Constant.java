package com.sohu.dreamplan.utils;

import android.os.Environment;

/**
 * Created by lizha on 2015/8/31.
 */
public class Constant {
    public static String picPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/dreamplan/pictures";
    public static final int ADD_NEW_DREAM_REQUEST_CODE = 1;
    public static final int ADD_NEW_DREAM_RESULT_CODE = 1;
    public static final int ADD_NEW_DREAM_CANCLE_CODE = -1;
    public static final int PUBLISH_NEW_ITEM_REQUEST_CODE = 2;
    public static final int PUBLISH_NEW_ITEM_RESULT_CODE = 2;
}
