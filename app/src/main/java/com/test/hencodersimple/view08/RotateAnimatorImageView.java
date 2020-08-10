package com.test.hencodersimple.view08;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.hencodersimple.view06.Utils;

/**
 * Time: 2020/7/2
 * Author: liupang
 * Description:
 */
 public class RotateAnimatorImageView extends View {

    private Paint paint;

    private Camera camera;

    private int rotateBottom = 0;

    private int cameraAngle = 0;
    private int cameraAngleTop = 0;

    public int getCameraAngleTop() {
        return cameraAngleTop;
    }

    public void setCameraAngleTop(int cameraAngleTop) {
        this.cameraAngleTop = cameraAngleTop;
        invalidate();
    }

    public int getCameraAngle() {
        return cameraAngle;
    }

    public void setCameraAngle(int cameraAngle) {
        this.cameraAngle = cameraAngle;
        invalidate();
    }

    public int getRotateBottom() {
        return rotateBottom;
    }

    public void setRotateBottom(int rotateBottom) {
        this.rotateBottom = rotateBottom;
        invalidate();
    }

    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        camera = new Camera();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    public RotateAnimatorImageView(Context context) {
        super(context);
    }

    public RotateAnimatorImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateAnimatorImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(400,400);
        canvas.rotate(-rotateBottom);
        camera.save();
        camera.rotateX(-cameraAngleTop);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(-200*2,-200*2,200*2,0);
        canvas.rotate(rotateBottom);
        canvas.translate(-400,-400);
        canvas.drawBitmap(Utils.getHeadPortrait(getResources(), 400), 200, 200, paint);
        canvas.restore();



        canvas.save();
        canvas.translate(400,400);
        canvas.rotate(-rotateBottom);
        camera.save();
        camera.rotateX(cameraAngle);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(-200*2,0,200*2,200*2);
        canvas.rotate(rotateBottom);
        canvas.translate(-400,-400);
        canvas.drawBitmap(Utils.getHeadPortrait(getResources(), 400), 200, 200, paint);
        canvas.restore();

    }
}
