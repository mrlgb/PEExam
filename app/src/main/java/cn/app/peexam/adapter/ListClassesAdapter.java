package cn.app.peexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.app.peexam.R;
import cn.app.peexam.activity.ProgramActivity;
import cn.app.peexam.activity.SchoolActivity.OnResponsePlanListener;
import cn.app.peexam.bean.GradeClass;
import cn.app.peexam.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ListClassesAdapter extends Adapter<ViewHolder> {
    private Context context;
    private List<GradeClass> gradeClassList;
    private LayoutInflater inflater;
    private OnResponsePlanListener planListener;

    public class ClassesHolder extends ViewHolder implements OnClickListener {
        private View itemView;
        @BindView(R.id.adapter_list_classes_tv_class)
        public TextView tv_classes;
        @BindView(R.id.adapter_list_classes_tv_people)
        public TextView tv_peoples;

        public ClassesHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind((Object) this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (((GradeClass) ListClassesAdapter.this.gradeClassList.get(((Integer) this.itemView.getTag()).intValue())).getNumber() <= 0) {
                ToastUtil.showToast(ListClassesAdapter.this.context, "该班级人数为0!");
                return;
            }
            Intent intent = new Intent(ListClassesAdapter.this.context, ProgramActivity.class);
            intent.putExtra("planId", ListClassesAdapter.this.planListener.getPlanId());
            intent.putExtra("class", ((Integer) this.itemView.getTag()).intValue());
            ListClassesAdapter.this.context.startActivity(intent);
        }
    }

    public ListClassesAdapter(Context context, List<GradeClass> gradeClassList, OnResponsePlanListener planListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.gradeClassList = gradeClassList;
        this.planListener = planListener;
    }

    public ListClassesAdapter(Context context, List<GradeClass> gradeClassList, OnResponsePlanListener planListener,String query){
        if(query.length()>0){
            query = query.toLowerCase();
            Log.d("filter", "query:"+query);
            final List<GradeClass> filteredModelList = new ArrayList<>();
            for (GradeClass model : gradeClassList) {
                final String text = model.getName().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.gradeClassList = filteredModelList;
            this.planListener = planListener;
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassesHolder(this.inflater.inflate(R.layout.adapter_list_classes, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ClassesHolder classesHolder = (ClassesHolder) holder;
        GradeClass gradeClass = (GradeClass) this.gradeClassList.get(position);
        classesHolder.tv_classes.setText(gradeClass.getName());
        classesHolder.tv_peoples.setText(gradeClass.getNumber() + "人");
        classesHolder.itemView.setTag(Integer.valueOf(position));
    }

    public int getItemCount() {
        return this.gradeClassList == null ? 0 : this.gradeClassList.size();
    }

    public void update(List<GradeClass> gradeClassList) {
        this.gradeClassList = gradeClassList;
        notifyDataSetChanged();
    }

    public List<GradeClass> getGradeClassList() {
        return gradeClassList;
    }

    public void setGradeClassList(List<GradeClass> gradeClassList) {
        this.gradeClassList = gradeClassList;
    }
}
