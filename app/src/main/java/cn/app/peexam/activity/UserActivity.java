package cn.app.peexam.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.app.peexam.R;
import cn.app.peexam.adapter.ListSchoolsAdapter;
import cn.app.peexam.bean.User;
import cn.app.peexam.util.TimeUtil;
import cn.app.peexam.util.ToastUtil;

public class UserActivity extends BaseActivity {
    private long firstTime = 0;
    @BindView(R.id.activity_user_rv_schools)
    public RecyclerView rv_schools;
    private ListSchoolsAdapter schoolsAdapter;
    @BindView(R.id.activity_user_tv_school_empty)
    public TextView tv_empty;
    @BindView(R.id.activity_user_tv_login_time)
    public TextView tv_loginTime;
    @BindView(R.id.activity_user_tv_role)
    public TextView tv_role;
    @BindView(R.id.activity_user_tv_username)
    public TextView tv_username;
    private User user;

    protected int getLayoutResId() {
        return R.layout.activity_user;
    }

    protected void initActivity(Bundle savedInstanceState) {
        super.initActivity(savedInstanceState);
        this.user = this.mainAppication.getUser();
        this.tv_username.setText(this.user.getRealName());
        this.tv_loginTime.setText(TimeUtil.getTimeFromTimeStamp(this.user.getLastLoginTime().getTime() + ""));
        switch (this.mainAppication.getUser().getUserType()) {
            case 1:
                this.tv_role.setText(getString(R.string.login_rb_teacher));
                break;
            case 2:
                this.tv_role.setText(getString(R.string.login_rb_master));
                break;
            case 3:
                this.tv_role.setText(getString(R.string.login_rb_admin));
                break;
            default:
                this.tv_role.setText(getString(R.string.login_rb_teacher));
                break;
        }
        initRecycleView();
    }

    private void initRecycleView() {
        if (this.mainAppication.getSchoolList().size() <= 0) {
            this.tv_empty.setVisibility(View.VISIBLE);
            this.rv_schools.setVisibility(View.GONE);
            return;
        }
        this.tv_empty.setVisibility(View.GONE);
        this.rv_schools.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        this.rv_schools.setLayoutManager(layoutManager);
        this.schoolsAdapter = new ListSchoolsAdapter(this);
        this.rv_schools.setAdapter(this.schoolsAdapter);
    }

    protected boolean activityKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (System.currentTimeMillis() - this.firstTime < 2000) {
                this.mainAppication.managerActivity(null, true);
            } else {
                this.firstTime = System.currentTimeMillis();
                ToastUtil.showToast((Context) this, getString(R.string.again_exit));
            }
        }
        return false;
    }

    protected void destroy() {
        super.destroy();
        this.tv_username = null;
        this.tv_loginTime = null;
        this.tv_role = null;
        this.rv_schools.removeAllViews();
        this.schoolsAdapter = null;
        this.rv_schools = null;
    }
}
