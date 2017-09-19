package com.vector.com.card.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForTask;

/**
 * Created by Administrator on 2017/8/20.
 */
public class MyRecyclerViewForTask extends RecyclerView {
    private int maxLength;
    private int mStartX = 0;
    private View itemView;
    private int pos;
    private Rect mTouchFrame;
    private int xDown, xMove, yDown, yMove, mTouchSlop, xUp, yUp;
    private Scroller mScroller;
    private TextView tv_look, tv_delete, tv_abort;
    private onGetListener listener;
    private int windowWidth;

    public interface onGetListener {

        void onClickDelete(View view, long id);

        void onClickLook(View view, long id,String startTime,String stopTime);

        void onClickAbort(View view, long id);

    }

    public void setListener(onGetListener listener) {
        this.listener = listener;
    }

    public MyRecyclerViewForTask(Context context) {
        this(context, null);
    }

    public MyRecyclerViewForTask(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerViewForTask(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowWidth = windowManager.getDefaultDisplay().getWidth();
        //滑动到最小距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //滑动的最大距离
        maxLength = ((int) (180 * context.getResources().getDisplayMetrics().density));
        //初始化Scroller
        mScroller = new Scroller(context, new LinearInterpolator(context, null));
    }


    private int dipToPx(Context context, int dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                xDown = x;
                yDown = y;
                //通过点击的坐标计算当前的position
                int mFirstPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                Rect frame = mTouchFrame;
                if (frame == null) {
                    mTouchFrame = new Rect();
                    frame = mTouchFrame;
                }
                int count = getChildCount();
                if (count > 0) {
                    for (int i = count - 1; i >= 0; i--) {
                        final View child = getChildAt(i);
                        if (child.getVisibility() == View.VISIBLE) {
                            child.getHitRect(frame);
                            if (frame.contains(x, y)) {
                                pos = mFirstPosition + i;
                            }
                        }
                    }
                    //通过position得到item的viewHolder
                    View view = getChildAt(pos - mFirstPosition);
                    RecyclerViewAdapterForTask.MyViewHolder viewHolder = (RecyclerViewAdapterForTask.MyViewHolder) getChildViewHolder(view);
                    itemView = viewHolder.view;
                    tv_look = (TextView) itemView.findViewById(R.id.self_task_item_look);
                    tv_delete = (TextView) itemView.findViewById(R.id.self_task_item_delete);
                    tv_abort = (TextView) itemView.findViewById(R.id.self_task_item_abort);
                }
            }
            break;

            case MotionEvent.ACTION_MOVE: {
                if (itemView != null) {
                    xMove = x;
                    yMove = y;
                    int dx = xMove - xDown;
                    int dy = yMove - yDown;

                    if (Math.abs(dy) < mTouchSlop * 2 && Math.abs(dx) > mTouchSlop) {
                        int scrollX = itemView.getScrollX();
                        int newScrollX = mStartX - x;
                        if (newScrollX < 0 && scrollX <= 0) {
                            newScrollX = 0;
                        } else if (newScrollX > 0 && scrollX >= maxLength) {
                            newScrollX = 0;
                        } else if ((scrollX + newScrollX) > maxLength) {
                            newScrollX = maxLength - scrollX;
                        }
                        itemView.scrollBy(newScrollX, 0);
                    }
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                if (itemView != null) {
                    xUp = x;
                    yUp = y;
                    int dx = xUp - xDown;
                    int dy = yUp - yDown;
                    int scrollX = itemView.getScrollX();
                    if (Math.abs(dy) < mTouchSlop && Math.abs(dx) < mTouchSlop && scrollX >= maxLength) {
                        if (xUp < (windowWidth - maxLength)) {
                            mScroller.startScroll(scrollX, 0, -scrollX, 0);
                        } else if (xUp > (windowWidth - maxLength) && xUp < (windowWidth - maxLength * 2 / 3)) {
                            if (listener != null) {
                                listener.onClickLook(tv_look, ((RecyclerViewAdapterForTask) getAdapter()).getItemId(pos),((RecyclerViewAdapterForTask) getAdapter()).getItemStartTime(pos),((RecyclerViewAdapterForTask) getAdapter()).getItemStopTime(pos));
                            }
                        } else if (xUp > (windowWidth - maxLength * 2 / 3) && xUp < (windowWidth - maxLength / 3)) {
                            if (listener != null) {
                                listener.onClickDelete(tv_delete, ((RecyclerViewAdapterForTask) getAdapter()).getItemId(pos));
                            }
                        } else {
                            if (listener != null) {
                                listener.onClickAbort(tv_abort, ((RecyclerViewAdapterForTask) getAdapter()).getItemId(pos));
                            }
                        }
                    } else {
                        if (scrollX >= maxLength * 1 / 3) {
                            mScroller.startScroll(scrollX, 0, -scrollX + maxLength, 0);
                            invalidate();
//                        ((RecyclerViewAdapterForTask) getAdapter()).removeItem(pos);
                        } else {
                            mScroller.startScroll(scrollX, 0, -scrollX, 0);
                            invalidate();
                        }
                    }
                }
            }
            break;
        }
        mStartX = x;
        return super.onTouchEvent(event);
    }

    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            itemView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
