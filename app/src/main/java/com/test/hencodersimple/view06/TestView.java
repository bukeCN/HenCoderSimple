package com.test.hencodersimple.view06;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Time: 2020/7/2
 * Author: liupang
 * Description:
 */
class TestView extends ViewGroup {
    public TestView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    // 负责分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    // 决定是否拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    // 处理事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
