package cn.app.peexam.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import butterknife.ButterKnife;
import cn.app.peexam.MainAppication;
import cn.app.peexam.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {

    protected MainAppication mainAppication;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutResId = getLayoutResId();
        if (layoutResId == 0) {
            finish();
        }
        this.mainAppication = (MainAppication) getApplication();
        setContentView(layoutResId);
        applyKitKatTranslucency();
        ButterKnife.bind(this);
        this.mainAppication.managerActivity(this, true);
        initActivity(savedInstanceState);
    }

    protected int getLayoutResId() {
        return 0;
    }

    private void applyKitKatTranslucency() {
        if (VERSION.SDK_INT >= 19) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setStatusBarTintResource(R.color.title);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= 67108864;
        } else {
            winParams.flags &= -67108865;
        }
        win.setAttributes(winParams);
    }

    protected void initActivity(Bundle savedInstanceState) {
    }

    protected void onStart() {
        super.onStart();
        activityStart();
    }

    protected void activityStart() {
    }

    protected void onPause() {
        super.onPause();
        activityPause();
    }

    protected void activityPause() {
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        activityExceptionExit(outState);
    }

    protected void activityExceptionExit(Bundle outState) {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return activityKeyDown(keyCode, event);
    }

    protected boolean activityKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return true;
        }
        this.mainAppication.managerActivity(this, false);
        return false;
    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v == null || !(v instanceof EditText)) {
            return false;
        }
        int[] l = new int[]{0, 0};
        v.getLocationInWindow(l);
        int left = l[0];
        int top = l[1];
        int bottom = top + v.getHeight();
        int right = left + v.getWidth();
        if (event.getX() <= ((float) left) || event.getX() >= ((float) right) || event.getY() <= ((float) top) || event.getY() >= ((float) bottom)) {
            return true;
        }
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(token, 2);
            //16908290
            getWindow().getDecorView().findViewById(android.R.id.content).setFocusable(true);
        }
        clearFocus();
    }

    protected void clearFocus() {
    }

    protected void onDestroy() {
        super.onDestroy();
        destroy();
        finish();
    }

    protected void destroy() {
        this.mainAppication = null;
    }
}
