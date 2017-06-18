package cn.app.peexam.util;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {
    public static final int ANIMATION_IN_TIME = 500;
    public static final int ANIMATION_OUT_TIME = 500;

    public static Animation createInAnimation(Context context, int fromYDelta) {
        AnimationSet set = new AnimationSet(context, null);
        set.setFillAfter(true);
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float) fromYDelta, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        set.addAnimation(alphaAnimation);
        return set;
    }

    public static Animation createOutAnimation(Context context, int toYDelta) {
        AnimationSet set = new AnimationSet(context, null);
        set.setFillAfter(true);
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) toYDelta);
        animation.setDuration(500);
        set.addAnimation(animation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500);
        set.addAnimation(alphaAnimation);
        return set;
    }
}
