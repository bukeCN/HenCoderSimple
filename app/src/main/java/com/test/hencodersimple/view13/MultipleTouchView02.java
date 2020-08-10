package com.test.hencodersimple.view13;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.hencodersimple.view06.Utils;

/**
 * Time: 2020/7/19
 * Author: liupang
 * Description: 多点触摸滑动的 View，支持多根手指划线
 */
public class MultipleTouchView02 extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private SparseArray<Path> paths = new SparseArray<>();


    {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(Utils.dp2px(4));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public MultipleTouchView02(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int count = event.getPointerCount();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                int index = event.getActionIndex();
                int id = event.getPointerId(index);

                float downX = event.getX(index);
                float downY = event.getY(index);
                Path path = new Path();
                path.moveTo(downX,downY);
                paths.append(id,path);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    int indexId = event.getPointerId(i);
                    float moveX = event.getX(i);
                    float moveY = event.getY(i);
                    Path path1 = paths.get(indexId);
                    path1.lineTo(moveX,moveY);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                int removeId = event.getPointerId(event.getActionIndex());
                paths.remove(removeId);
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.valueAt(i),paint);
        }
    }
}
