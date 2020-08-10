package com.test.hencodersimple.view07;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.hencodersimple.view06.Utils;

/**
 * Time: 2020/7/2
 * Author: liupang
 * Description:
 */
class RuningImageView extends View {

    private Paint paint;

    private Camera camera;

    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        camera = new Camera();
        camera.rotateX(60);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    public RuningImageView(Context context) {
        super(context);
    }

    public RuningImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RuningImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(400,400);
        canvas.rotate(-20);
        canvas.clipRect(-200*2,-200*2,200*2,0);
        canvas.rotate(20);
        canvas.translate(-400,-400);
        canvas.drawBitmap(Utils.getHeadPortrait(getResources(), 400), 200, 200, paint);
        canvas.restore();



        canvas.save();
        canvas.translate(400,400);
        canvas.rotate(-20);
        camera.applyToCanvas(canvas);
        canvas.clipRect(-200*2,0,200*2,200*2);
        canvas.rotate(20);
        canvas.translate(-400,-400);
        canvas.drawBitmap(Utils.getHeadPortrait(getResources(), 400), 200, 200, paint);
        canvas.restore();

//
//
//        canvas.save();
//        canvas.translate(400,400);
//        camera.applyToCanvas(canvas);
//        canvas.translate(-400,-400);
//
//        canvas.clipRect(200,200+200,200+400,200+400);
//
//        canvas.drawBitmap(Utils.getHeadPortrait(getResources(), 400), 200, 200, paint);
//        canvas.restore();

    }
}
