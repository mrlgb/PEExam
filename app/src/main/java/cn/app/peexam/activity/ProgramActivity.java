package cn.app.peexam.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.app.peexam.R;
import cn.app.peexam.adapter.ListProgramsAdapter;
import cn.app.peexam.bean.GradeClass;
import cn.app.peexam.bean.Program;
import cn.app.peexam.net.NetHttp;
import cn.app.peexam.net.ProgressSubscriber;
import cn.app.peexam.service.SubscriberOnNextListener;
import cn.app.peexam.util.TimeUtil;
import cn.app.peexam.util.ToastUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.List;

public class ProgramActivity extends BaseActivity implements OnRefreshListener {
    private OnResponseGradeClassListener classListener = new OnResponseGradeClassListener() {
        public int getGradeClassPosition() {
            return ProgramActivity.this.getIntent().getIntExtra("class", 0);
        }
    };
    private GradeClass gradeClass;
    private SubscriberOnNextListener<List<Program>> listener = new SubscriberOnNextListener<List<Program>>() {
        public void onNext(List<Program> programs) {
            if (programs == null || programs.size() <= 0) {
                ProgramActivity.this.showEmpty(true);
            } else {
                if (ProgramActivity.this.mainAppication.getProgramList() != null) {
                    ProgramActivity.this.mainAppication.getProgramList().clear();
                }
                ProgramActivity.this.mainAppication.setProgramList(programs);
                ProgramActivity.this.programsAdapter.update(ProgramActivity.this.mainAppication.getProgramList());
                ProgramActivity.this.showEmpty(false);
            }
            ProgramActivity.this.ptrsv_refresh.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(ProgramActivity.this.getString(R.string.refresh_lastUpdatedLabel) + ":" + TimeUtil.getNowTimeString());
            ProgramActivity.this.ptrsv_refresh.onRefreshComplete();
        }
    };
    private int planId = 0;
    private ListProgramsAdapter programsAdapter;
    @BindView(R.id.activity_program_ptrsv_refresh)
    public PullToRefreshScrollView ptrsv_refresh;
    @BindView(R.id.activity_program_rv_programs)
    public RecyclerView rv_programs;
    @BindView(R.id.activity_program_tv_class)
    public TextView tv_class;
    @BindView(R.id.activity_program_tv_program_empty)
    public TextView tv_empty;

    public interface OnResponseGradeClassListener {
        int getGradeClassPosition();
    }

    protected int getLayoutResId() {
        return R.layout.activity_program;
    }

    protected void initActivity(Bundle savedInstanceState) {
        super.initActivity(savedInstanceState);
        this.planId = getIntent().getIntExtra("planId", 0);

        if (this.mainAppication.isUseFilter()) {
            this.gradeClass = (GradeClass) this.mainAppication.getFilterGradeClassList().get(this.classListener.getGradeClassPosition());
            Log.d("Filter", "Filer-Program["+this.mainAppication.isUseFilter()+"]"+this.classListener.getGradeClassPosition());
        } else {
            Log.d("Filter", "No---Filer-Program["+this.mainAppication.isUseFilter()+"]"+this.classListener.getGradeClassPosition());
            this.gradeClass = (GradeClass) this.mainAppication.getGradeClassList().get(this.classListener.getGradeClassPosition());
        }


        if (this.gradeClass == null) {
            this.mainAppication.managerActivity(this, false);
            return;
        }
        this.tv_class.setText(this.gradeClass.getName() + "(" + this.gradeClass.getNumber() + "人)");
        initPullToRefresh();
        initRecycleView();
    }

    private void initPullToRefresh() {
        this.ptrsv_refresh.setMode(Mode.PULL_FROM_START);
        this.ptrsv_refresh.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(getString(R.string.refresh_lastUpdatedLabel));
        this.ptrsv_refresh.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.refresh_pullLabel));
        this.ptrsv_refresh.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refresh_refreshingLabel));
        this.ptrsv_refresh.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.refresh_releaseLabel));
        this.ptrsv_refresh.setOnRefreshListener((OnRefreshListener) this);
    }

    protected void activityStart() {
        refresh();
    }

    private void initRecycleView() {
        this.rv_programs.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        this.rv_programs.setLayoutManager(layoutManager);
        this.programsAdapter = new ListProgramsAdapter(this, this.mainAppication.getProgramList(), this.classListener);
        this.rv_programs.setAdapter(this.programsAdapter);
    }

    public void onRefresh(PullToRefreshBase refreshView) {
        refresh();
    }

    private void refresh() {
        if (NetHttp.isConnection(this)) {
            this.mainAppication.getHttpMethods().getProgramList(new ProgressSubscriber(this, this.listener), this.planId, this.gradeClass.getId());
            return;
        }
        ToastUtil.showToast((Context) this, getString(R.string.net_not_connection));
        this.ptrsv_refresh.onRefreshComplete();
    }

    private void showEmpty(boolean isFlag) {
        if (isFlag) {
            this.tv_empty.setVisibility(View.VISIBLE);
            this.rv_programs.setVisibility(View.GONE);
            return;
        }
        this.tv_empty.setVisibility(View.GONE);
        this.rv_programs.setVisibility(View.VISIBLE);
    }

    protected void destroy() {
        super.destroy();
        this.tv_class = null;
        this.rv_programs.removeAllViews();
        this.programsAdapter = null;
        this.rv_programs = null;
        this.ptrsv_refresh.removeAllViews();
        this.ptrsv_refresh = null;
        this.programsAdapter = null;
        this.listener = null;
    }
}
