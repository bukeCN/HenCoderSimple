package com.test.hencodersimple.view13;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.hencodersimple.view06.Utils;

/**
 * Time: 2020/7/14
 * Author: liupang
 * Description:多点触摸滑动的 View，多个点进行协同作业，核心思想是获取多个手指的焦点，然后忽略其他手指，只关注焦点。
 */
public class MultipleTouchView03 extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap bitmap;

    private float offsetX;
    private float offsetY;

    private float downX;
    private float downY;

    private float lastX;
    private float lastY;


    public MultipleTouchView03(Context context) {
        super(context);
    }

    public MultipleTouchView03(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bitmap = Utils.getHeadPortrait(getResources());
    }


    private int foucsDownX;
    private int foucsDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 获取记录焦点
        int sumX = 0;
        int sumY = 0;
        int foucsX;
        int foucsY;
        int pointerCount = event.getPointerCount();
        boolean isPointerUp = event.getActionMasked() == MotionEvent.ACTION_POINTER_UP;
        for (int i = 0; i < event.getPointerCount(); i++) {
            if (!(isPointerUp && i == event.getActionIndex())){
                sumX += event.getX(i);
                sumY += event.getY(i);
            }
        }
        if (isPointerUp){
            pointerCount -= 1;
        }
        foucsX = sumX / pointerCount;
        foucsY = sumY / pointerCount;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                foucsDownX = foucsX;
                foucsDownY = foucsY;
                lastX = offsetX;
                lastY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = lastX + (foucsX - foucsDownX);
                offsetY = lastY + (foucsY - foucsDownY);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }
}
