package cn.app.peexam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.OnClick;
import cn.app.peexam.R;
import cn.app.peexam.bean.User;
import cn.app.peexam.net.NetHttp;
import cn.app.peexam.net.ProgressSubscriber;
import cn.app.peexam.service.SubscriberOnNextListener;
import cn.app.peexam.util.ToastUtil;

import java.util.List;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.activity_login_et_pwd)
    public EditText et_pwd;
    @BindView(R.id.activity_login_et_username)
    public EditText et_username;
    private long firstTime = 0;
    private SubscriberOnNextListener<List<Object>> listener = new SubscriberOnNextListener<List<Object>>() {
        public void onNext(List<Object> objects) {
            if (objects != null && objects.size() == 2) {
                LoginActivity.this.mainAppication.setUser((User) objects.get(0));
                LoginActivity.this.mainAppication.setSchoolList((List) objects.get(1));
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, UserActivity.class));
                LoginActivity.this.mainAppication.managerActivity(LoginActivity.this, false);
            }
        }
    };
    @BindView(R.id.activity_login_rg_roles)
    public RadioGroup rg_roles;

    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    protected void initActivity(Bundle savedInstanceState) {
        super.initActivity(savedInstanceState);
    }

    @OnClick({R.id.activity_login_btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_btn_login:
                if (!NetHttp.isConnection(getApplicationContext())) {
                    ToastUtil.showToast((Context) this, getString(R.string.net_not_connection));
                    return;
                } else if (check()) {
                    login();
                }
            default:
                return;
        }
    }

    private boolean check() {
        String username = this.et_username.getText().toString().trim();
        String pwd = this.et_pwd.getText().toString().trim();
        if (username.isEmpty()) {
            ToastUtil.showToast((Context) this, getString(R.string.login_username_empty));
            return false;
        } else if (!pwd.isEmpty()) {
            return true;
        } else {
            ToastUtil.showToast((Context) this, getString(R.string.login_pwd_empty));
            return false;
        }
    }

    private void login() {
        String username = this.et_username.getText().toString().trim();
        String password = this.et_pwd.getText().toString().trim();
        int userType = 1;
        switch (this.rg_roles.getCheckedRadioButtonId()) {
            case R.id.activity_login_rb_teacher:
                userType = 1;
                break;
//            case R.id.activity_login_rb_master:
//                userType = 2;
//                break;
            case R.id.activity_login_rb_admin:
                userType = 3;
                break;
        }
        this.mainAppication.getHttpMethods().login(new ProgressSubscriber(this, this.listener), username, password, userType);
    }

    protected boolean activityKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - this.firstTime < 2000) {
                this.mainAppication.managerActivity(null, true);
            } else {
                this.firstTime = System.currentTimeMillis();
                ToastUtil.showToast((Context) this, getString(R.string.again_exit));
            }
        }
        return false;
    }

    protected void clearFocus() {
        this.et_username.clearFocus();
        this.et_pwd.clearFocus();
    }

    protected void destroy() {
        super.destroy();
        this.et_username = null;
        this.et_pwd = null;
        this.rg_roles = null;
    }
}
