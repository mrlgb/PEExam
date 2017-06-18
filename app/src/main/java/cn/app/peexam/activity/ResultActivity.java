package cn.app.peexam.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
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
import cn.app.peexam.adapter.ListResultsAdapter;
import cn.app.peexam.bean.GradeClass;
import cn.app.peexam.bean.Program;
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

public class ResultActivity extends BaseActivity {
    private GradeClass gradeClass;
    private SubscriberOnNextListener<Boolean> markAddListener = new SubscriberOnNextListener<Boolean>() {
        public void onNext(Boolean aBoolean) {
            if (aBoolean == null || !aBoolean.booleanValue()) {
                ToastUtil.showToast(ResultActivity.this, ResultActivity.this.getString(R.string.submit_fail));
            } else {
                ToastUtil.showToast(ResultActivity.this, ResultActivity.this.getString(R.string.submit_success));
            }
            ResultActivity.this.refresh();
        }
    };
    @BindView(R.id.activity_result_ns_classes)
    public NiceSpinner ns_classes;
    @BindView(R.id.activity_result_ns_program)
    public NiceSpinner ns_program;
    private Program program;
    private OnResponseProgramListener programListener = new OnResponseProgramListener() {
        public Program getProgram() {
            return ResultActivity.this.program;
        }
    };
    private ListResultsAdapter resultsAdapter;
    @BindView(R.id.activity_result_rv_results)
    public XRecyclerView rv_results;
    private List<Student> studentList;
    private SubscriberOnNextListener<List<Student>> studentlistener = new SubscriberOnNextListener<List<Student>>() {
        public void onNext(List<Student> studentList) {
            if (studentList == null || studentList.size() <= 0) {
                ResultActivity.this.showEmpty(true);
            } else {
//                List<Student> students = new ArrayList();
//                if (ResultActivity.this.program.getSex() == 0) {
//                    students.addAll(studentList);
//                } else {
//                    for (Student student : studentList) {
//                        if (student.getSex() == ResultActivity.this.program.getSex()) {
//                            students.add(student);
//                        }
//                    }
//                }
                ResultActivity.this.studentList = studentList;
                updateBySex(nsSex.getSelectedIndex());
                ResultActivity.this.showEmpty(false);
            }
            ResultActivity.this.rv_results.refreshComplete();
        }
    };
    @BindView(R.id.activity_result_tv_student_empty)
    public TextView tv_empty;

    @BindView(R.id.activity_result_ns_sex)
    public NiceSpinner nsSex;

    public interface OnResponseProgramListener {
        Program getProgram();
    }

    protected int getLayoutResId() {
        return R.layout.activity_result;
    }

    protected void initActivity(Bundle savedInstanceState) {
        super.initActivity(savedInstanceState);
        showEmpty(true);
        if (this.mainAppication.isUseFilter()) {
            this.gradeClass = (GradeClass) this.mainAppication.getFilterGradeClassList().get(getIntent().getIntExtra("class", 0));
            Log.d("Filter", "Filer-Result["+getIntent().getIntExtra("class", 0));
        } else {
            Log.d("Filter", "No---Filer-Result["+getIntent().getIntExtra("class", 0));
            this.gradeClass = (GradeClass) this.mainAppication.getGradeClassList().get(getIntent().getIntExtra("class", 0));
        }

//        this.gradeClass = (GradeClass) this.mainAppication.getGradeClassList().get(getIntent().getIntExtra("class", 0));
        this.program = (Program) this.mainAppication.getProgramList().get(getIntent().getIntExtra("position", 0));
        if (this.program == null) {
            this.mainAppication.managerActivity(this, false);
            return;
        }
        initSex();
        initRecycleView();
        initNiceSpinner();

        refresh();
    }

    private void initRecycleView() {
//        this.rv_results.noMoreLoading();
        this.rv_results.setLoadingMoreEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        this.rv_results.setLayoutManager(layoutManager);
        this.resultsAdapter = new ListResultsAdapter(this, this.studentList, this.programListener);
        this.rv_results.setAdapter(this.resultsAdapter);
        this.rv_results.setLoadingListener(new LoadingListener() {
            public void onRefresh() {
                ResultActivity.this.refresh();
            }

            public void onLoadMore() {
                ResultActivity.this.rv_results.loadMoreComplete();
            }
        });
    }

    private void initNiceSpinner() {
        List<String> strings = new ArrayList();
        if (this.mainAppication.isUseFilter()) {
            for (GradeClass gradeClass : this.mainAppication.getFilterGradeClassList()) {
                strings.add(gradeClass.getName() + "(" + gradeClass.getNumber() + "人)");
            }
        }
        else{
            for (GradeClass gradeClass : this.mainAppication.getGradeClassList()) {
                strings.add(gradeClass.getName() + "(" + gradeClass.getNumber() + "人)");
            }

        }


        this.ns_classes.attachDataSource(strings);
        this.ns_classes.setSelectedIndex(getIntent().getIntExtra("class", 0));
        this.ns_classes.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ResultActivity.this.gradeClass = (GradeClass) ResultActivity.this.mainAppication.getGradeClassList().get(position);
                ResultActivity.this.refresh();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        List<String> programs = new ArrayList();
        for (Program program : this.mainAppication.getProgramList()) {
            programs.add(program.getName());
        }
        this.ns_program.attachDataSource(programs);
        this.ns_program.setSelectedIndex(getIntent().getIntExtra("position", 0));
        this.ns_program.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ResultActivity.this.program = (Program) ResultActivity.this.mainAppication.getProgramList().get(position);
                ResultActivity.this.refresh();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initSex() {
        List<String> stringList = new ArrayList<>();
        stringList.add("全部");
        stringList.add("男");
        stringList.add("女");
        this.nsSex.isFlag(false);
        this.nsSex.attachDataSource(stringList);

        this.nsSex.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateBySex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateBySex(int position) {
        if (studentList != null && studentList.size() > 0) {
            List<Student> students = new ArrayList();
            if (position == 0) {
                students.addAll(studentList);
            } else {
                for (Student student : studentList) {
                    if (student.getSex() == position) {
                        students.add(student);
                    }
                }
            }
            resultsAdapter.updata(students);
        }
    }

//    protected void activityStart() {
//        refresh();
//    }

    private void refresh() {
        if (NetHttp.isConnection(this)) {
            this.mainAppication.getHttpMethods().getStudentList(new ProgressSubscriber(this, this.studentlistener), this.gradeClass.getId(), this.program.getId());
            return;
        }
        ToastUtil.showToast((Context) this, getString(R.string.net_not_connection));
        this.rv_results.refreshComplete();
    }

    @OnClick({R.id.activity_result_btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_result_btn_save:
                if (this.program == null) {
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

    private void submit() {
        if (NetHttp.isConnection(this)) {
            String json = markAdd();
            if (json.trim().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "没有数据要提交!", Toast.LENGTH_SHORT);
                ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setTextSize(getResources().getDimension(R.dimen.x6));
                toast.setGravity(Gravity.CENTER, 0, 0);
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
        if (viewList == null) {
            return message;
        }
        for (int i = 0; i < viewList.size(); i++) {
            EditText editText = (EditText) ((View) viewList.get(i)).findViewById(R.id.adapter_list_results_et_result);
            EditText et_Text1 = (EditText) ((View) viewList.get(i)).findViewById(R.id.adapter_list_results_et_result1);
            String valueString;
            float value;
            if (this.program.getType() == 1 && editText != null && et_Text1 != null && et_Text1.getVisibility() == View.VISIBLE) {
                valueString = "";
                if (!(editText.getText().toString().trim().isEmpty() || et_Text1.getText().toString().trim().isEmpty())) {
                    value = Float.parseFloat(editText.getText().toString());
                    if (value == 0.0) {
                        value = 0 - value;
                    }
                    if (this.program.getUnit().contains("秒")) {
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
                        message = message + "{\"student\":{\"id\":\"" + ((Student) this.studentList.get(((Integer) viewList.get(i).getTag(R.string.tag)).intValue())).getId() + "\"}" + ",\"mark\":\"" + valueString + "\"},";
                    }
                }
            } else if (this.program.getType() == 0 && editText != null) {
                valueString = "";
                if (!editText.getText().toString().trim().isEmpty()) {
                    value = Float.parseFloat(editText.getText().toString());
                    if (value == 0.0f) {
                        value = 0.0f - value;
                    }
//                    if (this.program.getUnit().contains("秒")) {
//                        valueString = String.format("%.2f", new Object[]{Float.valueOf(value)});
//                    } else {
//                        valueString = String.format("%.1f", new Object[]{Float.valueOf(value)});
//                    }
                    valueString = String.format("%." + program.getInputType() + "f", value);
                }
                if (valueString != "") {
                    message = message + "{\"student\":{\"id\":\"" + ((Student) this.studentList.get(((Integer) viewList.get(i).getTag(R.string.tag)).intValue())).getId() + "\"}" + ",\"mark\":\"" + valueString + "\"},";
                }
            }
        }
        if (message.isEmpty()) {
            return message;
        }
        return "{\"markList\":[" + message.substring(0, message.lastIndexOf(",")) + "],\"projectId\":" + this.program.getId() + "}";
    }

    protected void clearFocus() {
        this.rv_results.clearFocus();
    }

    private void showEmpty(boolean isFlag) {
        if (isFlag) {
            this.tv_empty.setVisibility(View.VISIBLE);
            this.rv_results.setVisibility(View.GONE);
            return;
        }
        this.tv_empty.setVisibility(View.GONE);
        this.rv_results.setVisibility(View.VISIBLE);
    }

    protected void destroy() {
        super.destroy();
        this.ns_classes = null;
        this.ns_program = null;
        this.rv_results.removeAllViews();
        this.resultsAdapter = null;
        this.rv_results = null;
        if (this.studentList != null) {
            this.studentList.clear();
        }
        this.studentList = null;
        this.studentlistener = null;
    }
}
