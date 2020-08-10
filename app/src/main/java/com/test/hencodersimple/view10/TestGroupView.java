package com.test.hencodersimple.view10;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Time: 2020/7/9
 * Author: liupang
 * Description:
 */
class TestGroupView extends ViewGroup {


    public TestGroupView(Context context) {
        super(context);
    }

    public TestGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
