package cn.app.peexam.util;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.app.peexam.R;

public class ToastUtil {
    private static String oldMsg;
    private static long oneTime = 0;
    protected static Toast toast = null;
    private static long twoTime = 0;

    public static void showToast(Context context, int resId) {
        showToast(context.getApplicationContext(), context.getString(resId));
    }

    public static void showToast(Context context, int resId, int gravity) {
        showToast(context.getApplicationContext(), context.getString(resId), gravity, 0, 0);
    }

    public static void showToast(Context context, String s, int gravity) {
        showToast(context.getApplicationContext(), s, gravity, 0, 0);
    }

    public static void showToast(Context context, int resId, int gravity, int offX, int offY) {
        showToast(context, context.getString(resId), gravity, offX, offY);
    }

    public static void showToast(Context context, String s) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_SHORT);
            ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setTextSize(context.getResources().getDimension(R.dimen.x6));
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (!s.equals(oldMsg)) {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            } else if (twoTime - oneTime > 0) {
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(Context context, String s, int gravity, int offX, int offY) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_SHORT);
            ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setTextSize(context.getResources().getDimension(R.dimen.x6));
            toast.setGravity(gravity, offX, offY);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (!s.equals(oldMsg)) {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            } else if (twoTime - oneTime > 0) {
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}
