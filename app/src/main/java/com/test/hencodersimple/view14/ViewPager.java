package com.test.hencodersimple.view14;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

/**
 * Time: 2020/7/20
 * Author: liupang
 * Description: 只有两个子view的 ViewPager
 */
public class ViewPager extends ViewGroup {

    private ViewConfiguration viewConfiguration;

    private OverScroller overScroller;

    private VelocityTracker velocityTracker;

    public ViewPager(Context context) {
        super(context);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewConfiguration = ViewConfiguration.get(context);

        overScroller = new OverScroller(context);

        velocityTracker = VelocityTracker.obtain();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int usedWidth = 0;
        int width = getWidth();
        int height = getHeight();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.layout(usedWidth, 0, usedWidth + width, height);
            usedWidth += width;
        }
    }


    float downX = 0;
    float downY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        velocityTracker.clear();
        velocityTracker.addMovement(ev);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                float moveDistance = moveX - downX;
                if (Math.abs(moveDistance) > viewConfiguration.getScaledTouchSlop()) {
                    // 滑动
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    int scrollX = 0;
    int targetPager = 1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                // 获取之前的点
                scrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                float moveX = event.getX();
                float moveY = event.getY();
                float moveDistance = moveX - downX;
                if (Math.abs(moveDistance) > viewConfiguration.getScaledTouchSlop()) {
                    // 滑动
                    int scrollDistance = (int) (-moveDistance + scrollX);
                    scrollDistance = Math.max(scrollDistance, 0);
                    scrollDistance = scrollDistance > getWidth() ? getWidth() : scrollDistance;
                    scrollTo(scrollDistance, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                float onceDistance = upX - downX;
                int nowScrollX = getScrollX();
                Log.e("sun","速度"+velocityTracker.getXVelocity());
                float xVelocity = velocityTracker.getXVelocity();

                if (onceDistance > 0) {
                    // 1 > 0
                    targetPager = getWidth() - nowScrollX > getWidth() / 2 ? 0 : 1;
                    if (targetPager == 1 && Math.abs(xVelocity) > viewConfiguration.getScaledMinimumFlingVelocity()){
                        targetPager = 0;
                    }
                } else {
                    // 0 > 1
                    targetPager = nowScrollX > getWidth() / 2 ? 1 : 0;
                    if (targetPager == 0 && Math.abs(xVelocity) > viewConfiguration.getScaledMinimumFlingVelocity()){
                        targetPager = 1;
                    }
                }

                int targetDistance = targetPager == 0 ? -nowScrollX : getWidth() - nowScrollX;
                overScroller.startScroll(nowScrollX, 0, targetDistance, 0);
                postInvalidateOnAnimation();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), 0);
            postInvalidateOnAnimation();
        }
    }
}
