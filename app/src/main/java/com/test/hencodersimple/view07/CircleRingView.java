package com.test.hencodersimple.view07;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.hencodersimple.view06.Utils;

/**
 * Time: 2020/7/2
 * Author: liupang
 * Description:
 */
class CircleRingView extends View {

    private Paint paint;
    private Paint progressPaint;
    private Paint textPaint;

    private RectF circleRect = new RectF();

    private Rect textRect = new Rect();

    private String text = "abab";

    private float radius;

    {

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(Utils.dp2px(40));
        textPaint.setColor(Color.parseColor("#25c6f6"));

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#22282c"));
        paint.setStrokeWidth(Utils.dp2px(10));
        paint.setStyle(Paint.Style.STROKE);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(Color.parseColor("#e03636"));
        progressPaint.setStrokeWidth(Utils.dp2px(10));
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        // 减去一半减去线的粗细影响
        radius = Utils.dp2px(100) - Utils.dp2px(10)/2;


        Paint.FontMetrics fontMetrics =  textPaint.getFontMetrics();


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        circleRect.set(getWidth()/2 - radius,getHeight()/2 - radius,
                getWidth()/2 + radius,getHeight()/2 + radius);

        textPaint.getTextBounds(text,0,text.length(),textRect);

    }

    public CircleRingView(Context context) {
        super(context);
    }

    public CircleRingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 背景圆环
        canvas.drawCircle(getWidth()/2,getHeight()/2,radius,paint);
//        paint.setColor(Color.RED);
//        paint.setStrokeWidth(Utils.dp2px(2));
//        canvas.drawLine(getWidth()/2,getHeight()/2,getWidth()/2,getHeight()/2+Utils.dp2px(100),paint);

        // 进度圆环
        canvas.drawArc(circleRect,-90,260,false,progressPaint);

        // 文字,保持居中
        canvas.drawText(text,getWidth()/2,getHeight()/2 + (textRect.bottom - textRect.top)/2,textPaint);

    }
}
