package com.test.hencodersimple.view11;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Time: 2020/7/11
 * Author: liupang
 * Description:
 */
public class TouchView extends View {

    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_UP){
            // 调用点击事件
            performClick();
        }
        return true;
    }
}
