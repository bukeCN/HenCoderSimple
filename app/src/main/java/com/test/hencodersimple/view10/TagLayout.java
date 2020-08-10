package com.test.hencodersimple.view10;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2020/7/9
 * Author: liupang
 * Description:
 */
class TagLayout extends ViewGroup {

    private List<Rect> childBounds = new ArrayList<>();

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();

        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);

        int usedWidth = 0;
        int usedHeight = 0;
        int lineMaxHeight = 0;
        int lineMaxWidth = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            // widthUsed 表示已经使用的跨度，height 同理。
            measureChildWithMargins(child,widthMeasureSpec,0,heightMeasureSpec,lineMaxHeight);

            // 判断进行换行操作
            int preUsedWidth = usedWidth + child.getMeasuredWidth();
            if (preUsedWidth > specWidthSize){
                // 换行
                usedHeight += lineMaxHeight;
                usedWidth = 0;
                lineMaxHeight = 0;
                lineMaxWidth = 0;
                measureChildWithMargins(child,widthMeasureSpec,0,heightMeasureSpec,usedHeight);
            }

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            Rect rect = new Rect();
            rect.set(usedWidth, usedHeight, usedWidth + child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin
                    , usedHeight + child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            childBounds.add(rect);

            usedWidth += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;

            // 记录子view占用的宽度，和最大高度
            lineMaxHeight = Math.max(lineMaxHeight,child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            lineMaxWidth = Math.max(lineMaxWidth, usedWidth);
        }
        MarginLayoutParams selfLayoutParams = (MarginLayoutParams) getLayoutParams();
        int measureWidth = getPaddingLeft() + getPaddingRight()
                + selfLayoutParams.leftMargin + selfLayoutParams.rightMargin + Math.max(specWidthSize,lineMaxWidth);
        int measureHeight = getPaddingTop() + getPaddingBottom()
                + selfLayoutParams.topMargin + selfLayoutParams.bottomMargin+ usedHeight;


        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0;i < getChildCount(); i++ ){
            View child = getChildAt(i);
            Rect rect = childBounds.get(i);

            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

            child.layout(rect.left + params.leftMargin,rect.top + params.rightMargin,
                    rect.right - params.rightMargin,rect.bottom - params.bottomMargin);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
}
