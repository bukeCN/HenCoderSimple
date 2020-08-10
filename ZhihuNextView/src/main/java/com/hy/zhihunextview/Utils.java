package com.hy.zhihunextview;

import android.content.res.Resources;
import android.content.res.TypedArray;
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
}
