package com.test.hencodersimple.view06;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Time: 2020/6/29
 * Author: liupang
 * Description:
 */
class PieChart extends View {
    private int[] colors = new int[]{Color.RED,Color.BLUE,Color.YELLOW,Color.GREEN};
    private float[] angles = new float[]{60,130,80,90};

    private int radius = 100;

    private Paint paint;

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        int cruAngle = 0;
        for (int i = 0;i < colors.length;i++){
            paint.setColor(colors[i]);
            if (i == 1){
                canvas.save();
                // 按照需要突出的饼的角度和突出间距计算移动的位置
                float x =  (float) Math.cos(Math.toRadians(cruAngle+angles[i]/2))*Utils.dp2px(10);
                float y =  (float) Math.sin(Math.toRadians(cruAngle+angles[i]/2))*Utils.dp2px(10);
                canvas.translate(x,y);
            }
            canvas.drawArc(getWidth()/2 - Utils.dp2px(radius),getHeight()/2 - Utils.dp2px(radius),
                    getWidth()/2 + Utils.dp2px(radius),getHeight()/2 + Utils.dp2px(radius),
                    (float) cruAngle,angles[i],true,paint);

            if (i == 1){
                canvas.restore();
            }
            cruAngle += angles[i];
        }
    }
}
