package com.test.hencodersimple.view06;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

import com.test.hencodersimple.R;

/**
 * Time: 2020/6/28
 * Author: liupang
 * Description: View 工具类
 */
public class Utils {
    public static float dp2px(int dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,
                Resources.getSystem().getDisplayMetrics());
    }

    public static Bitmap getHeadPortrait(Resources resources,int width){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, R.drawable.duola,options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(resources,R.drawable.duola,options);
    }
    public static Bitmap getHeadPortrait(Resources resources){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, R.drawable.duola,options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        return BitmapFactory.decodeResource(resources,R.drawable.duola,options);
    }
}
