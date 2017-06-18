package cn.app.peexam.util;

import android.view.View;
import android.view.View.MeasureSpec;

public class ViewUtil {

    public static int getViewMeasuredHeight(View view) {
        calculateViewMeasure(view);
        return view.getMeasuredHeight();
    }

    public static int getViewMeasuredWidth(View view) {
        calculateViewMeasure(view);
        return view.getMeasuredWidth();
    }

    private static void calculateViewMeasure(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
    }
}
