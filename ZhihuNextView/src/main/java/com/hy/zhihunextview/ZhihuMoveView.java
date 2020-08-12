package com.hy.zhihunextview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

public class ZhihuMoveView extends View{
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float radius = Utils.dp2px(20);

    private int defaultColor = Color.DKGRAY;

    private OverScroller overScroller;
    private ViewConfiguration viewConfiguration;

    private float downX;
    private float downY;

    private int parentWidth;
    private int parentHeight;

    /**
     * 手指抬起来时，用于辅助计算回弹距离的值
     */
    private float lastOffsetValue;

    private int leftMargin;
    private int topMargin;
    private int rightMargin;
    private int bottomMargin;

    private int textSize = 16;
    private String contentText = "测试一下";
    private Rect contentTextBounds = new Rect();

    // 收缩动画值
    private float faction = 0;
    // 文字动画改变值
    private float contentTextFaction = 0;

    private int zoomBeforLeft;
    private int zoomBeforTop;
    private int zoomBeforRight;
    private int zoomBeforBottom;

    private ValueAnimator zoomAniman;
    private ValueAnimator contentTextAniman;

    private int iconResId;
    private Drawable iconReawable;
    private Rect iconBounds = new Rect();


    /**
     * 收缩时靠左还是靠右，靠那一方那一方不动
     */
    private boolean zoomLeftOrRight = true;

    private ZoomTask zoomTask = new ZoomTask();

    public ZhihuMoveView(Context context) {
        super(context);
        init(context);
    }

    public ZhihuMoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ZhihuMoveView);
        iconResId = typedArray.getResourceId(R.styleable.ZhihuMoveView_iconScr,-1);
        typedArray.recycle();

        init(context);
    }

    private void init(Context context) {
        iconReawable = context.getResources().getDrawable(iconResId,context.getTheme());

        overScroller = new OverScroller(context);
        viewConfiguration = ViewConfiguration.get(context);

        textPaint.setTextSize(Utils.sp2px(textSize));

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(defaultColor);
        paint.setStrokeWidth(Utils.dp2px(1));

        textPaint.getTextBounds(contentText,0,contentText.length(),contentTextBounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;

        int middleWidth = (int) ((1 - faction) * (contentTextBounds.right - contentTextBounds.left));
        width = (int) (2 * radius + middleWidth + getPaddingLeft() + getPaddingRight());
        height = (int) (2 * radius + getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ViewGroup parent = (ViewGroup) getParent();
        parentWidth = parent.getWidth();
        parentHeight = parent.getHeight();

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        leftMargin = layoutParams.leftMargin;
        topMargin = layoutParams.topMargin;
        rightMargin = layoutParams.rightMargin;
        bottomMargin = layoutParams.bottomMargin;

        iconBounds.left = getPaddingLeft();
        iconBounds.top = getPaddingTop();
        iconBounds.right = (int) (iconBounds.left + radius*2);
        iconBounds.bottom = (int) (iconBounds.top + radius*2);

        if (iconReawable != null){
            iconReawable.setBounds(iconBounds);
        }
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        // 忽略夫如果调用了缩放动画，那么忽略调父 View 给的 l and t
        if (zoomAniman != null){
            if (zoomLeftOrRight){
                // 左
                int beforR = zoomBeforLeft + (r - l);
                super.layout(zoomBeforLeft, zoomBeforTop, beforR, zoomBeforBottom);
            } else {
                // 右
                int beforL = zoomBeforLeft + ((zoomBeforRight - zoomBeforLeft)-(r - l));
                super.layout(beforL, zoomBeforTop, zoomBeforRight, zoomBeforBottom);
            }
        } else {
            super.layout(l, t, r, b);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        textPaint.setAlpha((int) (1 - contentTextFaction)*255);
        canvas.drawText(contentText,getWidth() - ((contentTextBounds.right - contentTextBounds.left)+ getPaddingRight()),
                getHeight()/2f + (contentTextBounds.bottom - contentTextBounds.top)/2f,textPaint);

//        canvas.drawCircle(getHeight() / 2f, getHeight() / 2f, 30, paint);
        iconReawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                float moveDistanceX = moveX - downX;
                float moveDistanceY = moveY - downY;
                // 判断边界修正位置
                float[] fixValues = offsetFix(moveDistanceX, moveDistanceY);
                // 处理滑动
                offsetLeftAndRight((int) fixValues[0]);
                offsetTopAndBottom((int) fixValues[1]);
                break;
            case MotionEvent.ACTION_UP:
                // 抬起来判断 View 当前所处位置，没有挨着边就滑倒边缘去
                int left = getLeft();
                int right = getRight();
                lastOffsetValue = 0;
                // 支持 margin
                int horizontalBackDistance = left < (parentWidth - right) ?
                        left - leftMargin : -(parentWidth - right) + rightMargin;
                if (left != 0) {
                    zoomLeftOrRight = horizontalBackDistance > 0 ? true : false;
                    overScroller.startScroll(0, 0, horizontalBackDistance, 0);
                    postOnAnimation(zoomTask);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private float[] offsetFix(float moveDistanceX, float moveDistanceY) {
        int left = getLeft();
        int top = getTop() + topMargin;
        int right = getRight();
        int bottom = getBottom() - bottomMargin;

        left += moveDistanceX;
        right += moveDistanceX;
        top += moveDistanceY;
        bottom += moveDistanceY;

        if (left < leftMargin | right > parentWidth - rightMargin) {
            moveDistanceX = 0;
        }
        if (top < topMargin | bottom > parentHeight - bottomMargin) {
            moveDistanceY = 0;
        }

        float[] result = new float[2];
        result[0] = moveDistanceX;
        result[1] = moveDistanceY;
        return result;
    }


    private ValueAnimator getContentTextAniman() {
        if (contentTextAniman == null){
            contentTextAniman = ValueAnimator.ofFloat(1);
            contentTextAniman.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    contentTextFaction = (float) animation.getAnimatedValue();
                }
            });
        }
        return contentTextAniman;
    }

    private ValueAnimator getZoomAnima() {
        if (zoomAniman == null){
            zoomAniman = ValueAnimator.ofFloat(1);
            zoomAniman.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    faction = (float) animation.getAnimatedValue();
                    requestLayout();
                }
            });
        }
        return zoomAniman;
    }

    private void beGainZoomAniman(){
        // 控制，只能执行一次
        if (zoomAniman == null){
            // 记录当前位置以便请求重新布局的时候计算使用
            zoomBeforLeft = getLeft();
            zoomBeforTop = getTop();
            zoomBeforRight = getRight();
            zoomBeforBottom = getBottom();

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(getContentTextAniman(),getZoomAnima());
            animatorSet.start();
        }
    }

    private class ZoomTask implements Runnable{
        @Override
        public void run() {
            if (overScroller.computeScrollOffset()) {
                // 问题是，按照现在的方式是递增的，因此增加 lastOffsetValue
                if (overScroller.getStartX() != overScroller.getFinalX()) {
                    offsetLeftAndRight((int) -(overScroller.getCurrX() - lastOffsetValue));
                    lastOffsetValue = overScroller.getCurrX();
                } else {
                    offsetTopAndBottom((int) -(overScroller.getCurrY() - lastOffsetValue));
                    lastOffsetValue = overScroller.getCurrY();
                }
                postOnAnimation(this);
            } else {
                beGainZoomAniman();
            }
        }
    }
}
