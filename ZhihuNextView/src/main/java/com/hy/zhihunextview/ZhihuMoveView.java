package com.hy.zhihunextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Time: 2020/8/8
 * Author: liupang
 * Description: 仿知乎下一个回答的 View
 * 1. 是个圆，能动
 */
public class NextView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int DEFAULT_RADIUS = (int) Utils.dp2px(20);

    public NextView(Context context) {
        super(context);
    }

    public NextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth()/2,getHeight()/2,DEFAULT_RADIUS,paint);
    }
}
