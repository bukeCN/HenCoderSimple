package com.test.hencodersimple.view06;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Time: 2020/6/28
 * Author: liupang
 * Description: 仪表盘
 */
class DashBoardView extends View {


    private static final int ANGLE = 240;

    private Paint paint;

    private Path dashPath;
    private Path arcPath;

    private int lineDistance = 100;

    private PathDashPathEffect dashPathEffect;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Utils.dp2px(2));

        dashPath = new Path();
        dashPath.addRect(0,0,
                Utils.dp2px(2),Utils.dp2px(10), Path.Direction.CW);

        arcPath = new Path();
        arcPath.addArc(Utils.dp2px(20),getHeight()/2 - (getWidth()/2-Utils.dp2px(20)),
                getWidth()-Utils.dp2px(20),getHeight()/2 + (getWidth()/2-Utils.dp2px(20)),150,ANGLE);

        // 计算刻度绘制间隔
        PathMeasure pathMeasure = new PathMeasure(arcPath,false);
        float length = pathMeasure.getLength();

        dashPathEffect = new PathDashPathEffect(dashPath,(length- Utils.dp2px(2))/20 ,0, PathDashPathEffect.Style.ROTATE);
    }

    public DashBoardView(Context context) {
        super(context);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画圆弧
        canvas.drawArc(Utils.dp2px(20),getHeight()/2 - (getWidth()/2-Utils.dp2px(20)),
                getWidth()-Utils.dp2px(20),getHeight()/2 + (getWidth()/2-Utils.dp2px(20)),
                150,ANGLE,false,paint);
//        canvas.drawPath(arcPath,paint);
        // 画刻度
        paint.setPathEffect(dashPathEffect);
        canvas.drawArc(Utils.dp2px(20),getHeight()/2 - (getWidth()/2-Utils.dp2px(20)),
                getWidth()-Utils.dp2px(20),getHeight()/2 + (getWidth()/2-Utils.dp2px(20)),
                150,ANGLE,false,paint);
        paint.setPathEffect(null);
        // 画出圆心
        canvas.drawCircle(getWidth()/2,getHeight()/2,Utils.dp2px(10),paint);
        // 画指正
        canvas.drawLine(getWidth()/2,getHeight()/2,
                (float)(Utils.dp2px(lineDistance)*Math.cos(0)+getWidth()/2),(float)(Utils.dp2px(lineDistance)*Math.sin(0)+getHeight()/2),paint);
    }
}
