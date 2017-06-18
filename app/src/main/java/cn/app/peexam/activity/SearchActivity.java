package cn.app.peexam.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.app.peexam.R;
import cn.app.peexam.activity.ResultActivity.OnResponseProgramListener;
import cn.app.peexam.adapter.ListSearchResultsAdapter;
import cn.app.peexam.adapter.nicespinner.NiceSpinnerAdapter;
import cn.app.peexam.bean.HttpResultSearch;
import cn.app.peexam.bean.Program;
import cn.app.peexam.bean.School;
import cn.app.peexam.bean.Student;
import cn.app.peexam.net.NetHttp;
import cn.app.peexam.net.ProgressSubscriber;
import cn.app.peexam.service.SubscriberOnNextListener;
import cn.app.peexam.util.ToastUtil;
import cn.app.peexam.view.NiceSpinner;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.activity_search_et_search)
    public EditText et_search;
    private NiceSpinnerAdapter listAdapter;
    private SubscriberOnNextListener<Boolean> markAddListener = new SubscriberOnNextListener<Boolean>() {
        public void onNext(Boolean aBoolean) {
            if (aBoolean == null || !aBoolean.booleanValue()) {
                ToastUtil.showToast(SearchActivity.this, SearchActivity.this.getString(R.string.submit_fail));
            } else {
                ToastUtil.showToast(SearchActivity.this, SearchActivity.this.getString(R.string.submit_success));
            }
//            SearchActivity.this.refresh();
        }
    };
    @BindView(R.id.activity_search_ns_programs)
    public NiceSpinner ns_program;
    private List<Program> programList;
    private OnResponseProgramListener programListener = new OnResponseProgramListener() {
        public Program getProgram() {
            return (Program) SearchActivity.this.programList.get(SearchActivity.this.ns_program.getSelectedIndex());
        }
    };
    private ListSearchResultsAdapter resultsAdapter;
    @BindView(R.id.activity_search_rv_student)
    public XRecyclerView rv_student;
    private School school;
    private List<Student> studentList;
    private SubscriberOnNextListener<HttpResultSearch> studentListener = new SubscriberOnNextListener<HttpResultSearch>() {
        public void onNext(HttpResultSearch httpResultSearch) {
            if (httpResultSearch == null || httpResultSearch.getStudentList() == null || httpResultSearch.getProgramList() == null || httpResultSearch.getStudentList().size() <= 0 || httpResultSearch.getProgramList().size() <= 0) {
                SearchActivity.this.showEmpty(true);
            } else {
                SearchActivity.this.studentList = httpResultSearch.getStudentList();
                SearchActivity.this.programList = httpResultSearch.getProgramList();
                SearchActivity.this.initNiceSpinner();
                SearchActivity.this.resultsAdapter.updata(SearchActivity.this.studentList);
                SearchActivity.this.showEmpty(false);
            }
            SearchActivity.this.rv_student.refreshComplete();
        }
    };
    @BindView(R.id.activity_search_tv_student_empty)
    public TextView tv_empty;

    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    protected void initActivity(Bundle savedInstanceState) {
        super.initActivity(savedInstanceState);
        int position = getIntent().getIntExtra("position", -1);
        if (position == -1) {
            this.mainAppication.managerActivity(this, false);
            return;
        }
        this.school = (School) this.mainAppication.getSchoolList().get(position);
        initNiceSpinner();
        showEmpty(true);
        initRecycleView();
    }

    private void initRecycleView() {
//        this.rv_student.noMoreLoading();
        this.rv_student.setLoadingMoreEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        this.rv_student.setLayoutManager(layoutManager);
        this.resultsAdapter = new ListSearchResultsAdapter(this, this.studentList, this.programListener);
        this.rv_student.setAdapter(this.resultsAdapter);
        this.rv_student.setLoadingListener(new LoadingListener() {
            public void onRefresh() {
                SearchActivity.this.refresh();
            }

            public void onLoadMore() {
                SearchActivity.this.rv_student.loadMoreComplete();
            }
        });
        this.ns_program.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (studentList != null) {
                    for (Student student : studentList) {
                        student.setMark("");
                    }
                }
                SearchActivity.this.resultsAdapter.updata(SearchActivity.this.studentList);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initNiceSpinner() {
        List<String> strings = new ArrayList();
        if (this.programList == null || this.programList.size() == 0) {
            strings.add("测试项目");
        } else {
            for (Program program : this.programList) {
                strings.add(program.getName());
            }
        }
        if (this.listAdapter == null) {
            this.ns_program.attachDataSource(strings);
            this.listAdapter = (NiceSpinnerAdapter) this.ns_program.getAdapter();
        } else {
            this.listAdapter.update(strings);
        }
        this.ns_program.setSelectedIndex(0);
    }

    @OnClick({R.id.activity_search_btn_save, R.id.activity_search_btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_search_btn_search:
                refresh();
                return;
            case R.id.activity_search_btn_save:
                if (this.programList == null || this.programList.size() <= 0) {
                    ToastUtil.showToast((Context) this, "没有选择测试项目!");
                    return;
                } else {
                    submit();
                    return;
                }
            default:
                return;
        }
    }

    protected void activityStart() {
    }

    private void submit() {
        if (NetHttp.isConnection(this)) {
            String json = markAdd();
            if (json.trim().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "没有数据要提交!", Toast.LENGTH_SHORT);
                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setTextSize(getResources().getDimension(R.dimen.x6));
                toast.setGravity(17, 0, 0);
                toast.show();
                return;
            }
            this.mainAppication.getHttpMethods().setMarkAdd(new ProgressSubscriber(this, this.markAddListener), json);
            return;
        }
        ToastUtil.showToast((Context) this, getString(R.string.net_not_connection));
    }

    private String markAdd() {
        String message = "";
        List<View> viewList = this.resultsAdapter.getViewList();
        Program program = (Program) this.programList.get(this.ns_program.getSelectedIndex());
        if (viewList == null) {
            return message;
        }
        for (int i = 0; i < viewList.size(); i++) {
            EditText editText = (EditText) ((View) viewList.get(i)).findViewById(R.id.adapter_list_results_et_result);
            EditText et_Text1 = (EditText) ((View) viewList.get(i)).findViewById(R.id.adapter_list_results_et_result1);
            String valueString;
            float value;
            if (program.getType() == 1 && editText != null && et_Text1 != null && et_Text1.getVisibility() == View.VISIBLE) {
                valueString = "";
                if (!(editText.getText().toString().trim().isEmpty() || et_Text1.getText().toString().trim().isEmpty())) {
                    value = Float.parseFloat(editText.getText().toString());
                    if (value == 0.0f) {
                        value = 0.0f - value;
                    }
                    if (program.getUnit().contains("秒")) {
                        valueString = String.format("%.2f", new Object[]{Float.valueOf(value)});
                    } else {
                        valueString = String.format("%.1f", new Object[]{Float.valueOf(value)});
                    }
                    value = Float.parseFloat(et_Text1.getText().toString());
                    if (value == 0.0f) {
                        value = 0.0f - value;
                    }
                    valueString = valueString + "/" + String.format("%.1f", new Object[]{Float.valueOf(value)});
                    if (valueString != "") {
                        message = message + "{\"student\":{\"id\":\"" + ((Student) this.studentList.get(((Integer) ((View) viewList.get(i)).getTag(R.string.tag)).intValue())).getId() + "\"}" + ",\"mark\":\"" + valueString + "\"},";
                    }
                }
            } else if (program.getType() == 0 && editText != null) {
                valueString = "";
                if (!editText.getText().toString().trim().isEmpty()) {
                    value = Float.parseFloat(editText.getText().toString());
                    if (value == 0.0f) {
                        value = 0.0f - value;
                    }
//                    if (program.getUnit().contains("秒")) {
//                        valueString = String.format("%.2f", new Object[]{Float.valueOf(value)});
//                    } else {
//                        valueString = String.format("%.1f", new Object[]{Float.valueOf(value)});
//                    }
                    valueString = String.format("%." + program.getInputType() + "f", value);
                }
                if (valueString != "") {
                    message = message + "{\"student\":{\"id\":\"" + ((Student) this.studentList.get(((Integer) ((View) viewList.get(i)).getTag(R.string.tag)).intValue())).getId() + "\"}" + ",\"mark\":\"" + valueString + "\"},";
                }
            }
        }
        if (message.isEmpty()) {
            return message;
        }
        return "{\"markList\":[" + message.substring(0, message.lastIndexOf(",")) + "],\"projectId\":" + program.getId() + "}";
    }

    protected void clearFocus() {
        this.et_search.clearFocus();
        this.rv_student.clearFocus();
    }

    private void showEmpty(boolean isFlag) {
        if (isFlag) {
            this.tv_empty.setVisibility(View.VISIBLE);
            this.rv_student.setVisibility(View.GONE);
            return;
        }
        this.tv_empty.setVisibility(View.GONE);
        this.rv_student.setVisibility(View.VISIBLE);
    }

    protected void destroy() {
        super.destroy();
        this.ns_program = null;
        this.rv_student.removeAllViews();
        this.resultsAdapter = null;
        this.rv_student = null;
        if (this.studentList != null) {
            this.studentList.clear();
        }
        this.studentList = null;
        this.studentListener = null;
    }

    private void refresh() {
        String value = this.et_search.getText().toString().trim();
        if (value.isEmpty()) {
            ToastUtil.showToast((Context) this, "请输入学生姓名或者学号");
            this.rv_student.refreshComplete();
        } else if (NetHttp.isConnection(this)) {
            this.programList = null;
            initNiceSpinner();
            this.mainAppication.getHttpMethods().getStudentListByStuInfo(new ProgressSubscriber(this, this.studentListener), this.school.getId(), value);
        } else {
            ToastUtil.showToast((Context) this, getString(R.string.net_not_connection));
            this.rv_student.refreshComplete();
        }
    }
}
