package com.test.hencodersimple.view07;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
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
class BreakTextImageView extends View {

    private Paint paint;
    private TextPaint textPaint;

    private RectF circleRect = new RectF();

    private float drawTextMidde = Utils.dp2px(2);

    private String text = "那天我又独自坐在屋里，看着窗外的树叶“唰唰啦啦”地飘落。母亲进来了，挡在窗前：“北海的菊花开了，我推着你去看看吧。”她憔悴的脸上现出央求般的神色。“什么时候？”“你要是愿意，就明天?”她说。我的回答已经让她喜出望外了。“好吧，就明天。”我说。她高兴得一会坐下，一会站起：“那就赶紧准备准备。”“哎呀，烦不烦？几步路，有什么好准备的！”她也笑了，坐在我身边，絮絮叨叨地说着：“看完菊花，咱们就去‘仿膳’，你小时候最爱吃那儿的豌豆黄儿。还记得那回我带你去北海吗？你偏说那杨树花是毛毛虫，跑着，一脚踩扁一个……”她忽然不说了。对于“跑”和“踩”一类的字眼，她比我还敏感。她又悄悄地出去了。";


    StaticLayout staticLayout;

    float[] cutWidth = new float[1];

    {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(Utils.dp2px(20));
        textPaint.setColor(Color.parseColor("#25c6f6"));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        staticLayout = new StaticLayout(text, textPaint, getWidth(), Layout.Alignment.ALIGN_NORMAL
                , 1f, 0f, false);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        drawTextMidde = (int) (fontMetrics.bottom - fontMetrics.top);
    }

    public BreakTextImageView(Context context) {
        super(context);
    }

    public BreakTextImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BreakTextImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        invalidate();

        canvas.drawBitmap(Utils.getHeadPortrait(getResources(),300),getWidth()-300,150,paint);

//        staticLayout.draw(canvas);
        int startIndex = 0;
        for (int i = 0;i < 5;i++){
            int width = getWidth();
            float offsetX = drawTextMidde + i * textPaint.getFontSpacing();
            if (offsetX > 150){
                width = getWidth() - 300;
            }

            int index = textPaint.breakText(text,startIndex,text.length(), true, width, cutWidth);
            canvas.drawText(text, startIndex, startIndex + index, 0, drawTextMidde + i * textPaint.getFontSpacing(), textPaint);

            startIndex += index;
        }

    }
}
