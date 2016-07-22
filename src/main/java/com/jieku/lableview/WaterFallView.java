package com.jieku.lableview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.List;

/**
 * Created by baoli on 2016/7/22.
 */
public class WaterFallView extends View {

    private Paint paint;
    private List<Lable> mLabels;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private final int DIRECTION_LEFT = 0;
    private final int DIRECTION_TOP = 1;
    private final int DIRECTION_RIGHT = 2;
    private final int DIRECTION_BOTTOM = 3;
    private Thread mThread;
    private final float FONT_SIZE = 100f;
    private int mDownX;
    private int mDownY;
    private int mDownIndex;
    private OnItemClickListener mListener;
    private int scaledTouchSlop;

    public WaterFallView(Context context) {
        this(context, null);
    }

    public WaterFallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterFallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!hasContents()) {
            return;
        }
        for (int i = 0; i < mLabels.size(); i++) {
            Lable lable = mLabels.get(i);
            paint.setTextSize(FONT_SIZE);
            paint.setColor(Color.RED);
            canvas.drawText(lable.getContents(), lable.getxPosition(), lable.getyPosition(), paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                mDownIndex = getClickIndex();
                break;
            case MotionEvent.ACTION_UP:
                int nowX = (int) ev.getX();
                int nowY = (int) ev.getY();
                if (nowX - mDownX < scaledTouchSlop && nowY - mDownY < scaledTouchSlop
                        && mDownIndex != -1 && mListener != null) {
                    mListener.onItemClick(mDownIndex, mLabels.get(mDownIndex));
                }

                mDownX = mDownY = mDownIndex = -1;
                break;
        }

        return true;
    }

    private void initLayout() {
        if (!hasContents()) {
            return;
        }
        minX = getPaddingLeft();
        minY = getPaddingTop();
        maxX = getMeasuredWidth() - getPaddingRight();
        maxY = getMeasuredHeight() - getPaddingBottom();

        for (int i = 0; i < mLabels.size(); i++) {
            Lable lable = mLabels.get(i);

            //初始化位置
            lable.setxPosition((int) (minX + Math.random() * maxX));
            ;
            lable.setyPosition((int) (minY + Math.random() * maxY));
            ;

            //初始化方向
            lable.setxDirection(Math.random() > 0.5 ? DIRECTION_LEFT : DIRECTION_RIGHT);
            lable.setyDirection(Math.random() > 0.5 ? DIRECTION_TOP : DIRECTION_BOTTOM);

            //获取lable的宽和高
            paint.setTextSize(lable.getFontSize() == 0 ? FONT_SIZE : lable.getFontSize());
            Rect rect = new Rect();
            paint.getTextBounds(lable.getContents(), 0, lable.getContents().length(), rect);
            lable.setLableWidth(rect.width());
            lable.setLableHeight(rect.height());
        }


        if (mThread != null && mThread.isAlive()) {
            return;
        }

        mThread = new Thread(runnable);
        mThread.start();
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (; ; ) {
                SystemClock.sleep(100);

                for (int i = 0; i < mLabels.size(); i++) {
                    Lable lable = mLabels.get(i);
                    //判断是否越界
                    if (lable.getxPosition() <= minX) {
                        lable.setxDirection(DIRECTION_RIGHT);
                        lable.setxPosition(minX);
                    }

                    if (lable.getyPosition() <= minY) {
                        lable.setyDirection(DIRECTION_BOTTOM);
                        lable.setyPosition(minY);
                    }

                    if (lable.getxPosition() >= maxX - lable.getLableWidth()) {
                        lable.setxDirection(DIRECTION_LEFT);
                        lable.setxPosition(maxX - lable.getLableWidth());
                    }

                    if (lable.getyPosition() >= maxY - lable.getLableHeight()) {
                        lable.setyDirection(DIRECTION_TOP);
                        lable.setyPosition(maxY - lable.getLableHeight());
                    }
                    int speedX = 2;
                    int speedY = 3;
                    int tempX = lable.getxPosition() + (lable.getxDirection() == DIRECTION_RIGHT ? speedX : -speedX);
                    int tempY = lable.getyPosition() + (lable.getyDirection() == DIRECTION_BOTTOM ? speedY : -speedY);
                    lable.setxPosition(tempX);
                    lable.setyPosition(tempY);
                }
                postInvalidate();
            }
        }
    };


    public List<Lable> getmLabels() {
        return mLabels;
    }

    public void setmLabels(List labels) {
        mLabels = labels;
        requestLayout();
    }

    private boolean hasContents() {

        return mLabels != null && mLabels.size() > 0;
    }

    private int getClickIndex() {
        //点击的位置
        Rect downRect = new Rect();
        //文字所在的位置
        Rect locationRect = new Rect();
        for (int i = 0; i < mLabels.size(); i++) {
            Lable lable = mLabels.get(i);
            downRect.set(mDownX - lable.getLableWidth(), mDownY
                    - lable.getLableHeight(), mDownX
                    + lable.getLableWidth(), mDownY
                    + lable.getLableHeight());

            locationRect.set(lable.getxPosition(), lable.getyPosition(),
                    lable.getxPosition() + lable.getLableWidth(),
                    lable.getyPosition() + lable.getLableHeight());

            if (locationRect.intersect(downRect)) {
                return i;
            }
        }
        return -1;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        getParent().requestDisallowInterceptTouchEvent(true);
        mListener = l;
    }

    public interface OnItemClickListener {
        public void onItemClick(int index, Lable label);
    }
}
