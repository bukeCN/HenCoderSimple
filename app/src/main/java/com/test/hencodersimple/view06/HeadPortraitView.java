package com.test.hencodersimple.view06;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Time: 2020/7/2
 * Author: liupang
 * Description:
 */
class HeadPortraitView extends View {

    private Paint paint;
    private PorterDuffXfermode xfermode;
    private RectF bounds;

    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

        bounds = new RectF(100,100,500,500);
    }


    public HeadPortraitView(Context context) {
        super(context);
    }

    public HeadPortraitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadPortraitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(300,300,Utils.dp2px(54),paint);
        int com =  canvas.saveLayer(bounds,paint);

        canvas.drawCircle(300,300,Utils.dp2px(50),paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(Utils.getHeadPortrait(getResources(),400),100,100,paint);
        paint.setXfermode(null);

        canvas.restoreToCount(com);
    }
}
