package cn.app.peexam.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.reflect.Field;

public class TypefaceUtil {
    public static String addHtmlRedFlag(String string) {
        return "<font color=\"red\">" + string + "</font>";
    }

    public static String keywordMadeRed(String sourceString, String keyword) {
        String result = "";
        if (sourceString == null || "".equals(sourceString.trim())) {
            return result;
        }
        if (keyword == null || "".equals(keyword.trim())) {
            return sourceString;
        }
        return sourceString.replaceAll(keyword, "<font color=\"red\">" + keyword + "</font>");
    }

    public static void replaceFont(@NonNull View root, String fontPath) {
        if (root != null && !TextUtils.isEmpty(fontPath)) {
            if (root instanceof TextView) {
                TextView textView = (TextView) root;
                int style = 0;
                if (textView.getTypeface() != null) {
                    style = textView.getTypeface().getStyle();
                }
                textView.setTypeface(createTypeface(root.getContext(), fontPath), style);
            } else if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    replaceFont(viewGroup.getChildAt(i), fontPath);
                }
            }
        }
    }

    public static void replaceFont(@NonNull Activity context, String fontPath) {
        replaceFont(getRootView(context), fontPath);
    }

    public static Typeface createTypeface(Context context, String fontPath) {
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(16908290)).getChildAt(0);
    }

    public static void replaceSystemDefaultFont(@NonNull Context context, @NonNull String fontPath) {
        replaceTypefaceField("SERIF", createTypeface(context, fontPath));
    }

    private static void replaceTypefaceField(String fieldName, Object value) {
        try {
            Field defaultField = Typeface.class.getDeclaredField(fieldName);
            defaultField.setAccessible(true);
            defaultField.set(null, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
}
