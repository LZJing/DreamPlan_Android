package com.sohu.dreamplan.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

/**
 * Created by lizha on 2015/8/31.
 */
public class LoadImageUtils {

    public static void loadImageAsyn(String picName,ImageView imageView,ImageLoader imageLoader,DisplayImageOptions options){

        if(picName.startsWith("cover")){
            Log.e("detail", "startsWith cover");
            String assetsUrl = ImageDownloader.Scheme.ASSETS.wrap(picName);
            imageLoader.displayImage(assetsUrl, imageView, options);

        }else{
            Log.e("detail","not startsWith cover");
            String name = Constant.picPath +"/" + picName;
//            BitmapFactory.Options option = new BitmapFactory.Options();
//            option.inSampleSize = 4;
//            Bitmap bm = BitmapFactory.decodeFile(name,option);
//            imageView.setImageBitmap(bm);

            String imageUrl = ImageDownloader.Scheme.FILE.wrap(name);
            imageLoader.displayImage(imageUrl, imageView, options);
        }

    }
}
