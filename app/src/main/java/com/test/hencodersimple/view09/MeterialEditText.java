package com.test.hencodersimple.view09;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.test.hencodersimple.R;
import com.test.hencodersimple.view06.Utils;

/**
 * Time: 2020/7/8
 * Author: liupang
 * Description: 带标签的 EditText
 */
public class MeterialEditText extends androidx.appcompat.widget.AppCompatEditText {
    private static final String TAG = "MeterialEditText";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // 是否使用 label
    private boolean useLabelText;

    // 是否显示 label
    private boolean isShowLabelText;

    // label 最终一行文字的高度
    private float labelTextFontSpacing;

    private ObjectAnimator labelAnimator;
    // 动画进度，代表显示进度
    private float animaProgrees;

    // 初始 label Y 轴的位置
    private int finalY;

    // 输入框 text 的尺寸，字体缩放动画
    private float mainTextSize;

    public float getAnimaProgrees() {
        return animaProgrees;
    }

    public void setAnimaProgrees(float animaProgrees) {
        this.animaProgrees = animaProgrees;
        invalidate();
    }

    private static final int LABEL_TEXT_SIZE = (int) Utils.dp2px(12);

    public MeterialEditText(Context context) {
        super(context);
    }

    public MeterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        finalY = getHeight() - getPaddingBottom();
    }


    private void init(Context context, AttributeSet attrs) {
        paint.setTextSize(LABEL_TEXT_SIZE);
        labelTextFontSpacing = paint.getFontSpacing();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeterialEditText);
        useLabelText = typedArray.getBoolean(R.styleable.MeterialEditText_useTextLabel, false);
        typedArray.recycle();
        if (useLabelText) {
            mainTextSize = getTextSize();

            // 这里为什么可以使用 getBackground.getBounds() 这个属性？因为 android 背景使用的是点九图是自带内边距的。
            Rect rect = new Rect();
            rect = getBackground().getBounds();
            setPadding(getPaddingLeft(), (int) (rect.top + labelTextFontSpacing), getPaddingRight(), getPaddingBottom());

            addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s) && !isShowLabelText) {
                        isShowLabelText = true;
                        getLabelAnimator().start();
                    } else if (TextUtils.isEmpty(s) && isShowLabelText) {
                        isShowLabelText = false;
                        getLabelAnimator().reverse();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }


    private ObjectAnimator getLabelAnimator() {
        if (labelAnimator == null) {
            labelAnimator = ObjectAnimator.ofFloat(this, "animaProgrees", 1);
        }
        return labelAnimator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (useLabelText){
            float labelTextSize = LABEL_TEXT_SIZE + (1 - animaProgrees) * (mainTextSize - LABEL_TEXT_SIZE);
            paint.setTextSize(labelTextSize);

            paint.setAlpha((int) (0xff * animaProgrees));
            // 计算 y 点的位置，目标最终位置 + (1 - 进度)*(需要变化的总高度)
            int y = (int) (labelTextFontSpacing + (1 - animaProgrees) * (finalY - labelTextFontSpacing));
            canvas.drawText(getHint().toString(), getPaddingLeft(), y, paint);
        }
    }
}
