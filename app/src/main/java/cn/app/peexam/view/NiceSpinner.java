package cn.app.peexam.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.app.peexam.R;
import cn.app.peexam.adapter.nicespinner.NiceSpinnerAdapter;
import cn.app.peexam.adapter.nicespinner.NiceSpinnerAdapterWrapper;
import cn.app.peexam.adapter.nicespinner.NiceSpinnerBaseAdapter;
import cn.app.peexam.util.DensityUtil;
import cn.app.peexam.util.ScreenUtil;

/**
 * @author angelo.marchesin
 */
@SuppressWarnings("unused")
public class NiceSpinner extends TextView {

    private static final int MAX_LEVEL = 10000;
    private static final int DEFAULT_ELEVATION = 16;
    private static final String INSTANCE_STATE = "instance_state";
    private static final String SELECTED_INDEX = "selected_index";
    private static final String IS_POPUP_SHOWING = "is_popup_showing";

    private int selectedIndex;
    private Drawable drawable;
    private PopupWindow popupWindow;
    private ListView listView;
    private NiceSpinnerBaseAdapter adapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;
    private boolean isArrowHide;
    private int textColor;
    private int backgroundSelector;

    private boolean isFlag = true;

    @SuppressWarnings("ConstantConditions")
    public NiceSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public NiceSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NiceSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(SELECTED_INDEX, selectedIndex);
        if (popupWindow != null) {
            bundle.putBoolean(IS_POPUP_SHOWING, popupWindow.isShowing());
            dismissDropDown();
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof Bundle) {
            Bundle bundle = (Bundle) savedState;
            selectedIndex = bundle.getInt(SELECTED_INDEX);

            if (adapter != null) {
                setText(adapter.getItemInDataset(selectedIndex).toString());
                adapter.notifyItemSelected(selectedIndex);
            }

            if (bundle.getBoolean(IS_POPUP_SHOWING)) {
                if (popupWindow != null) {
                    // Post the show request into the looper to avoid bad token exception
                    post(new Runnable() {
                        @Override
                        public void run() {
                            showDropDown();
                        }
                    });
                }
            }
            savedState = bundle.getParcelable(INSTANCE_STATE);
        }
        super.onRestoreInstanceState(savedState);
    }

    private void init(Context context, AttributeSet attrs) {
        Resources resources = getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NiceSpinner);
        int defaultPadding = resources.getDimensionPixelSize(R.dimen.x10);

        //setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        //setPadding(defaultPadding, 0, 0, 0);
        setClickable(true);

        backgroundSelector = typedArray.getResourceId(R.styleable.NiceSpinner_backgroundSelector, R.drawable.spinner_selector);
        setBackgroundResource(backgroundSelector);
        textColor = typedArray.getColor(R.styleable.NiceSpinner_textTint, -1);
        setTextColor(textColor);


        listView = new ListView(context);
        listView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        // Set the spinner's id into the listview to make it pretend to be the right parent in
        // onItemClick
        listView.setId(getId());
        listView.setDivider(null);
        listView.setItemsCanFocus(true);
        //hide vertical and horizontal scrollbars
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= selectedIndex && position < adapter.getCount()) {
                    position++;
                }

                // Need to set selected index before calling listeners or getSelectedIndex() can be
                // reported incorrectly due to race conditions.
                selectedIndex = position;

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }

                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(parent, view, position, id);
                }

                adapter.notifyItemSelected(position);
                setText(adapter.getItemInDataset(position).toString());
                dismissDropDown();
            }
        });

        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(listView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(DEFAULT_ELEVATION);
            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.spinner_drawable));
        } else {
            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.mipmap.drop_down_shadow));
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (!isArrowHide) {
                    animateArrow(false);
                }
            }
        });

        isArrowHide = typedArray.getBoolean(R.styleable.NiceSpinner_hideArrow, false);
        if (!isArrowHide) {
//            Drawable basicDrawable = ContextCompat.getDrawable(context, R.drawable.spinner_arrow);
//            int size = (int) getResources().getDimension(R.dimen.x20);
//
//            int resId = typedArray.getColor(R.styleable.NiceSpinner_arrowTint, -1);
//            if (basicDrawable != null) {
//                drawable = DrawableCompat.wrap(basicDrawable);
//                if (resId != -1) {
//                    DrawableCompat.setTint(drawable, resId);
//                }
//            }
//            drawable.setBounds(0,0,5,5);
//            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            /**
             * 取得自定义属性值
             */
            int drawableWidth = typedArray.getDimensionPixelSize(R.styleable.NiceSpinner_drawableWidth, -1);
            int drawableHeight = typedArray.getDimensionPixelSize(R.styleable.NiceSpinner_drawableHeight, -1);
            /**
             * 取得TextView的Drawable(左上右下四个组成的数组值)
             */
//            Drawable[] drawables = getCompoundDrawables();
//            Drawable textDrawable = null;
//            for (Drawable drawable : drawables) {
//                if (drawable != null) {
//                    textDrawable = drawable;
//                }
//            }
            Drawable basicDrawable = ContextCompat.getDrawable(context, R.drawable.spinner_arrow);
            int resId = typedArray.getColor(R.styleable.NiceSpinner_arrowTint, -1);
            if (basicDrawable != null) {
                drawable = DrawableCompat.wrap(basicDrawable);
                if (resId != -1) {
                    DrawableCompat.setTint(drawable, resId);
                }
            }
            /**
             * 设置宽高
             */
            if (drawable != null && drawableWidth != -1 && drawableHeight != -1) {
                drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            }
            /**
             * 设置给TextView
             */
            setCompoundDrawables(null, null, drawable, null);
        }

        typedArray.recycle();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void isFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    /**
     * Set the default spinner item using its index
     *
     * @param position the item's position
     */
    public void setSelectedIndex(int position) {
        if (adapter != null) {
            if (position >= 0 && position <= adapter.getCount()) {
                adapter.notifyItemSelected(position);
                selectedIndex = position;
                setText(adapter.getItemInDataset(position).toString());
            } else {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }
        }
    }

    public void addOnItemClickListener(@NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectedListener(@NonNull AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public <T> void attachDataSource(@NonNull List<T> dataset) {
        adapter = new NiceSpinnerAdapter<>(getContext(), dataset, textColor, backgroundSelector);
        setAdapterInternal(adapter);
    }

    public void setAdapter(@NonNull ListAdapter adapter) {
        this.adapter = new NiceSpinnerAdapterWrapper(getContext(), adapter, textColor, backgroundSelector);
        setAdapterInternal(this.adapter);
    }

    public ListAdapter getAdapter() {
        return this.adapter;
    }

    private void setAdapterInternal(@NonNull NiceSpinnerBaseAdapter adapter) {
        // If the adapter needs to be settled again, ensure to reset the selected index as well
        selectedIndex = 0;
        listView.setAdapter(adapter);
        setText(adapter.getItemInDataset(selectedIndex).toString());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        popupWindow.setWidth(MeasureSpec.getSize(widthMeasureSpec));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!popupWindow.isShowing()) {
                showDropDown();
            } else {
                dismissDropDown();
            }
        }
        return super.onTouchEvent(event);
    }

    private void animateArrow(boolean shouldRotateUp) {
        int start = shouldRotateUp ? 0 : MAX_LEVEL;
        int end = shouldRotateUp ? MAX_LEVEL : 0;
        ObjectAnimator animator = ObjectAnimator.ofInt(drawable, "level", start, end);
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.start();
    }

    public void dismissDropDown() {
        if (!isArrowHide) {
            animateArrow(false);
        }
        popupWindow.dismiss();
    }

    public void showDropDown() {
        if (!isArrowHide) {
            animateArrow(true);
        }
        Log.i("info", "showDropDown" + isFlag);
        if (isFlag) {
            popupWindow.showAsDropDown(this);
        } else {
            //获取需要在其上方显示的控件的位置信息
//            int[] location = calculatePopWindowPos(this, listView);
            //在控件上方显示
            int[] location = new int[2];
            getLocationOnScreen(location);
            popupWindow.getContentView().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0], location[1] - getHeight() * 2);
        }
    }

    public void setTintColor(@ColorRes int resId) {
        if (drawable != null && !isArrowHide) {
            DrawableCompat.setTint(drawable, ContextCompat.getColor(getContext(), resId));
        }
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    private int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtil.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtil.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }
}