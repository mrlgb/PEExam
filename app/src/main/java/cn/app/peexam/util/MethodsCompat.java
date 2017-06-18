package cn.app.peexam.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.Window;
import java.io.File;

public class MethodsCompat {
    @TargetApi(5)
    public static void overridePendingTransition(Activity activity, int enter_anim, int exit_anim) {
        activity.overridePendingTransition(enter_anim, exit_anim);
    }

    @TargetApi(7)
    public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, Options options) {
        return Thumbnails.getThumbnail(cr, origId, kind, options);
    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    @TargetApi(11)
    public static void recreate(Activity activity) {
        if (VERSION.SDK_INT >= 11) {
            activity.recreate();
        }
    }

    @TargetApi(11)
    public static void setLayerType(View view, int layerType, Paint paint) {
        if (VERSION.SDK_INT >= 11) {
            view.setLayerType(layerType, paint);
        }
    }

    @TargetApi(14)
    public static void setUiOptions(Window window, int uiOptions) {
        if (VERSION.SDK_INT >= 14) {
            window.setUiOptions(uiOptions);
        }
    }
}
