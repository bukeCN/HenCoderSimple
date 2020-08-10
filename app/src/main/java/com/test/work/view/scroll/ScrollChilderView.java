package com.test.work.view.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

/**
 * Time: 2020/7/22
 * Author: liupang
 * Description: NestedScroll 协调父View进行滑动
 */
public class ScrollChilderView extends View implements NestedScrollingChild3 {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 帮助和父View进行协商的类
     */
    private NestedScrollingChildHelper childHelper;

    public ScrollChilderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint.setStyle(Paint.Style.FILL);
        childHelper = new NestedScrollingChildHelper(this);
        // 开启嵌套滑动
        childHelper.setNestedScrollingEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.RED);
        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2-10,paint);
    }

    private float downX;
    private float downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                // 向父view请求嵌套滑动
                childHelper.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL | ViewCompat.SCROLL_AXIS_HORIZONTAL);

                downX = event.getX();
                downY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getX();
                float currentY = event.getY();

                int oneDistanceX = (int) (currentX - downX + getScrollX());
                int oneDistanceY = (int) (currentY - downY + getScrollY());

                offsetLeftAndRight(oneDistanceX);
                offsetTopAndBottom(oneDistanceY);

                // 查看是否到了边界，到了边界就通知父View开始一起滑。
                Log.e("sun", "onTouchEvent: "+ getLeft() + "*"+getTop() + "*" + getRight() + "*" + getBottom());
                int unconsumedX = 0;
                int unconsumedY = 0;
                if (getLeft() < 0){
                    unconsumedX = getLeft();
                }
                if (getTop() < 0){
                    unconsumedY = getTop();
                }
                Log.e("sun", "onTouchEvent: "+ getLeft() + "*"+getTop() + "*" + getRight() + "*" + getBottom());
                childHelper.dispatchNestedScroll(oneDistanceX,oneDistanceY,unconsumedX,unconsumedY,null);

                // 这里按照我们的需要实现的逻辑，应该是当没有滑动到边界的时候 view 自身一个人玩，当滑动到边界的时候，带着父View一起玩
                // 因此下面说的看看就行.
//                Log.e("sun",oneDistanceX+"");
//                int[] consumed = new int[2];
//                consumed[0] = oneDistanceX;
//                consumed[1] = oneDistanceY;
//                boolean parentResult = childHelper.dispatchNestedPreScroll(0,0,consumed,null);
//                if (parentResult){
//                    // 如果父View返回了 true 说明父View没有滑动或者没有消耗完，有交给子view处理的
//
//                } else {
//                    // 返回 false 说明父View消耗完了
//
//                }
//                childHelper.dispatchNestedScroll()


//                childHelper.dispatchNestedScroll();

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {

    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return childHelper.startNestedScroll(axes,type);
    }

    @Override
    public void stopNestedScroll(int type) {
        childHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return childHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }
}
