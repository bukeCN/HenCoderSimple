package com.test.work.view.scroll.pullrefreshview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;

import com.test.hencodersimple.view06.Utils;

/**
 * Time: 2020/7/27
 * Author: liupang
 * Description: 下拉刷新 ViewGroup
 * 1、支持 NestedScroll 协调
 */
public class PullRefreshView extends FrameLayout implements NestedScrollingParent3 {

    private ViewConfiguration viewConfiguration;
    private NestedScrollingParentHelper nestedParentHelper;

    private OverScroller overScroller;

    private View headView;
    private int headViewHeight = (int) Utils.dp2px(200);

    public PullRefreshView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PullRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        viewConfiguration = ViewConfiguration.get(context);
        nestedParentHelper = new NestedScrollingParentHelper(this);

        overScroller = new OverScroller(context);

        headView = new View(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,headViewHeight);
        headView.setLayoutParams(layoutParams);
        headView.setBackgroundColor(Color.BLUE);
        addView(headView,0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        headView.layout(left,top-headViewHeight,right,0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    private float downY;
    private float scrollY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                scrollY = getScrollY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int oneceDistance = (int) (y - downY);
                int absDistance = Math.abs(oneceDistance);
                if (absDistance > viewConfiguration.getScaledTouchSlop()){
                    oneceDistance = (int) (-oneceDistance + scrollY);
                    if (absDistance > headViewHeight){
                        oneceDistance = -headViewHeight;
                    }
                    scrollTo(0,oneceDistance);
                }
                break;
            case MotionEvent.ACTION_UP:
                int onceScrollDistance = getScrollY();
                int absOnceScrollDistance = Math.abs(onceScrollDistance);
                if (absOnceScrollDistance >= headViewHeight / 2){
                    overScroller.startScroll(0,onceScrollDistance,0,-(headViewHeight - absOnceScrollDistance),800);
                } else {
                    overScroller.startScroll(0,onceScrollDistance,0,absOnceScrollDistance);
                }
                postInvalidateOnAnimation();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (overScroller.computeScrollOffset()){
            scrollTo(overScroller.getCurrX(),overScroller.getCurrY());
            postInvalidateOnAnimation();
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {

    }

    /**
     * 子view请求协调滑动
     * @param child
     * @param target
     * @param axes
     * @param type
     * @return
     */
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        // 返回 true 表示同意协调滑动
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        // 结束协调滑动，
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        // 子view处理
        scrollTo(0,dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        scrollTo(0,consumed[1]);
    }
}
