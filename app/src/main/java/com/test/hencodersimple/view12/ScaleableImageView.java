package com.test.hencodersimple.view12;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.test.hencodersimple.view06.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Time: 2020/7/11
 * Author: liupang
 * Description:
 */
public class ScaleableImageView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
Runnable, ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = ScaleableImageView.class.getName();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private Bitmap bitmap;

    // 用于初始位图偏移
    private int initialOffsetX;
    private int initialOffsetY;
    // 用户滑动偏移
    private float offsetX;
    private float offsetY;

    // 初始放大比例
    private float smallScale;
    private float bigScale;
    // 放大还是平常状态标志
    private boolean isBig = false;
    // 缩放超出值，为了在放大状态下能上下滑动
    private float overScaleFraction = 1.5f;

    // 检查触摸手势
    private GestureDetectorCompat gestureDetectorCompat;
    // 放大缩小动画
    private ObjectAnimator scaleAnimator;
    private float currentScale;

    // 最大缩放比例宽度
    private int mestBitmapWidth;
    private int mestBitmapHeight;
    // abs 值，滑动的时候左右上下最大可滑动的距离，以限制边界
    private int mestOffsetX;
    private int mestOffsetY;

    private OverScroller scroller;

    // 双指撑开缩放
    private ScaleGestureDetector scaleGestureDetector;

    private float getCurrentScale() {
        return currentScale;
    }

    private void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        invalidate();
    }

    private ObjectAnimator getScaleAnimator(float start,float end){
        if (scaleAnimator == null){
            scaleAnimator = ObjectAnimator.ofFloat(this,"currentScale",0);
        }
        // 因为最小缩放比例和最大缩放比例是变化的，因此每次获取动画对象时都应该设置数值
        scaleAnimator.setFloatValues(start,end);
        return scaleAnimator;
    }
    private ObjectAnimator getScaleAnimator(){
        return getScaleAnimator(smallScale,bigScale);
    }

    public ScaleableImageView(Context context) {
        super(context);
        init(context);
    }

    public ScaleableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        scroller = new OverScroller(context);

        scaleGestureDetector = new ScaleGestureDetector(context, this);

        bitmap = Utils.getHeadPortrait(getResources());

        // 初始化手势检测器
        gestureDetectorCompat = new GestureDetectorCompat(context,this);
        // 添加双击回调
        gestureDetectorCompat.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 判断何时使用 Gesture 何时使用 ScaleDetector
        // 先交给撑放的 ScaleGestureDetector，然后判断是否正在缩放，没有则交给 getsture
        boolean result = scaleGestureDetector.onTouchEvent(event);
        if (!scaleGestureDetector.isInProgress()){
            result = gestureDetectorCompat.onTouchEvent(event);
        }
        return result;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        initialOffsetX = (getWidth() - bitmapWidth)/2;
        initialOffsetY = (getHeight() - bitmapWidth)/2;

        smallScale = (float) getWidth() / bitmapWidth;
        bigScale = (float) getHeight() / bitmapHeight;
        bigScale = bigScale * overScaleFraction;
        currentScale = smallScale;

        mestBitmapWidth = (int) (bigScale * bitmapWidth);
        mestBitmapHeight = (int) (bigScale * bitmapHeight);


        mestOffsetX = mestBitmapWidth - (mestBitmapWidth/2 + getWidth()/2);
        mestOffsetY = mestBitmapHeight - (mestBitmapHeight/2 + getHeight()/2);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        // 计算动画的百分比，动画的过程是
        float animatorFraction = (currentScale - smallScale) / (bigScale - smallScale);
        canvas.translate(animatorFraction*offsetX,animatorFraction*offsetY);

//        float scale = smallScale + (bigScale*overScaleFraction - smallScale)* animatorFraction;

        canvas.scale(currentScale,currentScale,getWidth()/2,getHeight()/2);
        canvas.drawBitmap(bitmap, initialOffsetX, initialOffsetY,paint);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent down, MotionEvent event, float distanceX, float distanceY) {
        // 解决滑动问题，计算 x y 的偏移，同时处理边界问题
        Log.e(TAG, "onScroll: "+distanceX+"*"+distanceX );
        if (isBig){
            offsetX -= distanceX;
            offsetY -= distanceY;
            offsetX = Math.abs(offsetX) < mestOffsetX ? offsetX : (offsetX < 0 ? -mestOffsetX : mestOffsetX);
            offsetY = Math.abs(offsetY) < mestOffsetY ? offsetY : (offsetY < 0 ? -mestOffsetY : mestOffsetY);
            invalidate();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent down, MotionEvent event, float velocityX, float velocityY) {
        if (isBig){
            scroller.fling((int) offsetX,(int) offsetY,(int) velocityX,(int) velocityY,
                    -mestOffsetX,mestOffsetX,
                    -mestOffsetY,mestOffsetY);
            postOnAnimation(this);
        }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }


    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // 响应双击
        isBig = !isBig;
        if (isBig){
            // 这个是控制双击放大，手指触摸的图片位置不飞走
            // 计算思路，手指触摸图片的位置 - 放大之后手指触摸点的位置
            offsetX = ((e.getX() - getWidth() / 2f)*smallScale) - ((e.getX() - getWidth() / 2f) * bigScale);
            offsetY = ((e.getY() - getHeight() / 2f)*smallScale) - ((e.getY() - getHeight() / 2f) * bigScale);
            offsetX = Math.abs(offsetX) < mestOffsetX ? offsetX : (offsetX < 0 ? -mestOffsetX : mestOffsetX);
            offsetY = Math.abs(offsetY) < mestOffsetY ? offsetY : (offsetY < 0 ? -mestOffsetY : mestOffsetY);
            // 放大
            getScaleAnimator().start();
        } else {
            // 缩小
            getScaleAnimator(currentScale,smallScale).start();
        }

        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }


    @Override
    public void run() {
        if (scroller.computeScrollOffset()){
            offsetX = scroller.getCurrX();
            offsetY = scroller.getCurrY();
            invalidate();
            postOnAnimation(this);
        }
    }

    private float lastScale;
    private float onceOffsetX;
    private float onceOffsetY;
    private float foucsX;
    private float foucsY;
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // 缩放中
//        Log.i(TAG, "onScale: 缩放中"+detector.getScaleFactor());
        currentScale = detector.getScaleFactor() * lastScale;
//
//        onceOffsetX = ((foucsX - getWidth() / 2f)*lastScale) - ((foucsX - getWidth() / 2f) * currentScale);
//        onceOffsetY = ((foucsY - getHeight() / 2f)*lastScale) - ((foucsY - getHeight() / 2f) * currentScale);
//
//        offsetX =  onceOffsetX;
//        offsetY =  onceOffsetY;
//
//        offsetX = Math.abs(offsetX) < mestOffsetX ? offsetX : (offsetX < 0 ? -mestOffsetX : mestOffsetX);
//        offsetY = Math.abs(offsetY) < mestOffsetY ? offsetY : (offsetY < 0 ? -mestOffsetY : mestOffsetY);

        invalidate();
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        lastScale = currentScale;
        foucsX = detector.getFocusX();
        foucsY = detector.getFocusY();
        Log.e(TAG, "onScaleBegin: "+detector.getFocusX()+","+detector.getFocusY()+"-"+
                detector.getPreviousSpan()+"-"+detector.getCurrentSpan());

        // 开始缩放
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        // 缩放结束
        if (currentScale > smallScale){
            isBig = true;
        }

        if (currentScale > bigScale){
            getScaleAnimator(currentScale,bigScale).start();
        } else if (currentScale < smallScale){
            getScaleAnimator(currentScale,smallScale).start();
        }
    }
}
