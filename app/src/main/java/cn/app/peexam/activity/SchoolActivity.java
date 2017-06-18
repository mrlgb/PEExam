package cn.app.peexam.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.app.peexam.R;
import cn.app.peexam.adapter.ListClassesAdapter;
import cn.app.peexam.bean.GradeClass;
import cn.app.peexam.bean.School;
import cn.app.peexam.net.NetHttp;
import cn.app.peexam.net.ProgressSubscriber;
import cn.app.peexam.service.SubscriberOnNextListener;
import cn.app.peexam.util.TimeUtil;
import cn.app.peexam.util.ToastUtil;
import cn.app.peexam.view.NiceSpinner;
import okhttp3.OkHttpClient;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

public class SchoolActivity extends BaseActivity implements OnRefreshListener {
    private ListClassesAdapter classesAdapter;
    private String filterKey = "";
    private SubscriberOnNextListener<List<Object>> listener = new SubscriberOnNextListener<List<Object>>() {
        public void onNext(List<Object> objects) {
            if (objects == null || objects.size() != 2) {
                SchoolActivity.this.tv_empty.setVisibility(View.VISIBLE);
                SchoolActivity.this.rv_classes.setVisibility(View.GONE);
            } else {
                SchoolActivity.this.planId = ((Integer) objects.get(0)).intValue();
                List<GradeClass> gradeClassList = (List) objects.get(1);
                if (gradeClassList == null || gradeClassList.size() <= 0) {
                    SchoolActivity.this.tv_empty.setVisibility(View.VISIBLE);
                    SchoolActivity.this.rv_classes.setVisibility(View.GONE);
                } else {
                    if (SchoolActivity.this.mainAppication.getGradeClassList() != null) {
                        SchoolActivity.this.mainAppication.getGradeClassList().clear();
                    }
                    SchoolActivity.this.mainAppication.setGradeClassList(gradeClassList);
                    SchoolActivity.this.classesAdapter.update(SchoolActivity.this.mainAppication.getGradeClassList());
                    ((ScrollView) SchoolActivity.this.ptrsv_refresh.getRefreshableView()).smoothScrollTo(0, 20);
                    SchoolActivity.this.tv_empty.setVisibility(View.GONE);
                    SchoolActivity.this.rv_classes.setVisibility(View.VISIBLE);
                }
            }
            SchoolActivity.this.ptrsv_refresh.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(SchoolActivity.this.getString(R.string.refresh_lastUpdatedLabel) + ":" + TimeUtil.getNowTimeString());
            SchoolActivity.this.ptrsv_refresh.onRefreshComplete();
        }
    };
    @BindView(R.id.activity_school_ns_schools)
    public NiceSpinner ns_schools;

    private int planId = -1;
    private OnResponsePlanListener planListener = new OnResponsePlanListener() {
        public int getPlanId() {
            return SchoolActivity.this.planId;
        }
    };
    @BindView(R.id.activity_school_ptrsv_refresh)
    public PullToRefreshScrollView ptrsv_refresh;
    @BindView(R.id.activity_school_rv_classes)
    public RecyclerView rv_classes;
    private School school;
    @BindView(R.id.activity_school_tv_class_empty)
    public TextView tv_empty;
    @BindView(R.id.activity_school_edit_filter)
    public EditText text_filter;

    public interface OnResponsePlanListener {
        int getPlanId();
    }

    protected int getLayoutResId() {
        return R.layout.activity_school;
    }

    protected void initActivity(Bundle savedInstanceState) {
        super.initActivity(savedInstanceState);
        this.tv_empty.setVisibility(View.VISIBLE);
        this.rv_classes.setVisibility(View.GONE);
        this.school = (School) this.mainAppication.getSchoolList().get(getIntent().getIntExtra("position", 0));
        this.text_filter.setText("");
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

    private void initRecycleView() {
        this.rv_classes.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        this.rv_classes.setLayoutManager(layoutManager);

        this.classesAdapter = new ListClassesAdapter(this, this.mainAppication.getGradeClassList(), this.planListener);
        this.rv_classes.setAdapter(this.classesAdapter);

        List<String> dataset = new ArrayList();
        if (this.mainAppication.getSchoolList() != null) {
            for (School school : this.mainAppication.getSchoolList()) {
                dataset.add(school.getName());
            }
            this.ns_schools.attachDataSource(dataset);
            this.ns_schools.setSelectedIndex(getIntent().getIntExtra("position", 0));
        }
        this.ns_schools.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SchoolActivity.this.school = (School) SchoolActivity.this.mainAppication.getSchoolList().get(position);
                SchoolActivity.this.refresh();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    protected void activityStart() {
        refresh();
    }

    public void onRefresh(PullToRefreshBase refreshView) {
        refresh();
    }

    @OnClick({R.id.activity_school_iv_search, R.id.activity_school_iv_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_school_iv_search:
                if (this.planId == -1) {
                    ToastUtil.showToast((Context) this, "该校还未导入行政班级数据!");
                    return;
                }
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("position", this.ns_schools.getSelectedIndex());
                intent.putExtra("planId", this.planId);
                startActivity(intent);
                return;
            case R.id.activity_school_iv_filter:
                //fix---add filter functions
                filterClasses();
            default:
                return;
        }
    }


    private void filterClasses() {
        filterKey = text_filter.getText().toString().trim();
        if (filterKey.length() > 0) {
            this.classesAdapter = new ListClassesAdapter(this, this.mainAppication.getGradeClassList(), this.planListener, filterKey);
            this.mainAppication.setFilterGradeClassList(this.classesAdapter.getGradeClassList());
            //Update classes when use filter data feature!!-mrlgb
            this.mainAppication.setUseFilter(true);
            //
            Log.d("filter","Update filter list now !!!");

            this.rv_classes.setAdapter(this.classesAdapter);
        } else
            ToastUtil.showToast((Context) this, getString(R.string.filer_input));

    }


    private void refresh() {
        if (NetHttp.isConnection(this)) {
            this.planId = -1;
            this.mainAppication.getHttpMethods().getClassList(new ProgressSubscriber(this, this.listener), this.mainAppication.getUser().getUserId(), this.mainAppication.getUser().getUserType(), this.school.getId());
            //use or not use filter data feature!!-mrlgb
            this.mainAppication.setUseFilter(false);
            this.text_filter.setText("");
            return;
        }
        ToastUtil.showToast((Context) this, getString(R.string.net_not_connection));
        this.ptrsv_refresh.onRefreshComplete();
    }

    protected void destroy() {
        super.destroy();
        this.ns_schools = null;
        this.classesAdapter.update(null);
        this.classesAdapter = null;
        this.rv_classes.removeAllViews();
        this.rv_classes = null;
        this.ptrsv_refresh.removeAllViews();
        this.ptrsv_refresh = null;
        this.listener = null;
    }

}
