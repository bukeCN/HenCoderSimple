package com.hy.zhihunextview;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Time: 2020/8/8
 * Author: liupang
 * Description:
 */
public class Utils {
    public static float dp2px(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,Resources.getSystem().getDisplayMetrics());
    }
    public static float sp2px(float sp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,Resources.getSystem().getDisplayMetrics());
    }
}
