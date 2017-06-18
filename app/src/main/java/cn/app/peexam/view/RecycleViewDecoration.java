package cn.app.peexam.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class RecycleViewDecoration extends ItemDecoration {
    public static final int[] ATRRS = new int[]{16843284};
    public static final int HORIZONTAL_LIST = 0;
    public static final int VERTICAL_LIST = 1;
    private Context mContext;
    private Drawable mDivider;
    private int mOrientation;

    public RecycleViewDecoration(Context context, int orientation) {
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(ATRRS);
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation == 0 || orientation == 1) {
            this.mOrientation = orientation;
            return;
        }
        throw new IllegalArgumentException("invalid orientation");
    }

    public void onDraw(Canvas c, RecyclerView parent, State state) {
        if (this.mOrientation == 0) {
            drawVerticalLine(c, parent, state);
        } else {
            drawHorizontalLine(c, parent, state);
        }
    }

    public void drawHorizontalLine(Canvas c, RecyclerView parent, State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom() + ((LayoutParams) child.getLayoutParams()).bottomMargin;
            this.mDivider.setBounds(left, top, right, top + this.mDivider.getIntrinsicHeight());
            this.mDivider.draw(c);
        }
    }

    public void drawVerticalLine(Canvas c, RecyclerView parent, State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int left = child.getRight() + ((LayoutParams) child.getLayoutParams()).rightMargin;
            this.mDivider.setBounds(left, top, left + this.mDivider.getIntrinsicWidth(), bottom);
            this.mDivider.draw(c);
        }
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        if (this.mOrientation == 0) {
            outRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
        }
    }
}
