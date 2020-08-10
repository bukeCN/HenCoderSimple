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
 * Description: 多指触摸互不干扰型，核心思想只看最后落下的手指抛弃前面的。
 */
public class MultipleTouchView01 extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap bitmap;

    private float offsetX;
    private float offsetY;

    private float downX;
    private float downY;

    private float lastX;
    private float lastY;


    public MultipleTouchView01(Context context) {
        super(context);
    }

    public MultipleTouchView01(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bitmap = Utils.getHeadPortrait(getResources());
    }

    private int targetId;
    private int targetIndex;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 获取第一根手指的 id
                targetId = event.getPointerId(0);
                // 记录按下时候的位置
                downX = event.getX();
                downY = event.getY();

                lastX = offsetX;
                lastY = offsetY;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // 当有别的手指按下
                int index = event.getActionIndex();
                targetId = event.getPointerId(index);
                lastX = offsetX;
                lastY = offsetY;

                downX = event.getX(index);
                downY = event.getY(index);
                break;
            case MotionEvent.ACTION_MOVE:
                int targetIndex = event.findPointerIndex(targetId);
                offsetX = lastX + event.getX(targetIndex) - downX;
                offsetY = lastY + event.getY(targetIndex) - downY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // 最初按下的手指抬起了
                lastX = offsetX;
                lastY = offsetY;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 后续按下的手指抬起了，检查抬起的手指是那根手指
                int upIndex = event.getActionIndex();
                int upId = event.findPointerIndex(upIndex);
                if (upId == targetId){
                    // 如果就是刚刚按下的那根,需要将滑动监听切换至最后一根按下的
                    int pointerCount = event.getPointerCount();
                    if (upIndex == pointerCount - 1){
                        // 如果抬起的手指就是最后按下的手指
                        targetId = event.getPointerId(pointerCount - 2);
                    } else {
                        // 否则
                        targetId = event.getPointerId(pointerCount - 1);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }
}
