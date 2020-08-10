package com.test.work.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;

/**
 * Time: 2020/7/22
 * Author: liupang
 * Description:
 */
public class ScrollParentView extends FrameLayout implements NestedScrollingParent3 {

    private NestedScrollingParentHelper parentHelper;
    private NestedScrollingChildHelper childHelper;

    public ScrollParentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        Log.i("TAG", "onNestedScroll: "+ dxUnconsumed );
        int targetRight = target.getRight();
        int targetBottom = target.getBottom();
        if (targetRight > getWidth()){
            dxUnconsumed = targetRight - getWidth();
        }
        if (targetBottom > getHeight()){
            dyUnconsumed = targetBottom - getHeight();
        }
        offsetLeftAndRight(dxUnconsumed);
        offsetTopAndBottom(dyUnconsumed);

    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.i("TAG", "onStartNestedScroll: "+"收到" );
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

    }
}
