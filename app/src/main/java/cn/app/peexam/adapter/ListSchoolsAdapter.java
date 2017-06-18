package cn.app.peexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.app.peexam.MainAppication;
import cn.app.peexam.R;
import cn.app.peexam.activity.SchoolActivity;
import cn.app.peexam.bean.School;

import java.util.List;

public class ListSchoolsAdapter extends Adapter<ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<School> schoolList;

    public class SchoolHolder extends ViewHolder implements OnClickListener {

        private View itemView;
        @BindView(R.id.adapter_list_schools_tv_class)
        public TextView tv_classes;
        @BindView(R.id.adapter_list_school_tv_people)
        public TextView tv_peoples;

        public SchoolHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(ListSchoolsAdapter.this.context, SchoolActivity.class);
            intent.putExtra("position", (Integer) this.itemView.getTag());
            ListSchoolsAdapter.this.context.startActivity(intent);
        }
    }

    public ListSchoolsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.schoolList = ((MainAppication) context.getApplicationContext()).getSchoolList();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SchoolHolder(this.inflater.inflate(R.layout.adapter_list_schools, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        SchoolHolder classesHolder = (SchoolHolder) holder;
        School school = (School) this.schoolList.get(position);
        classesHolder.tv_classes.setText(school.getName());
        classesHolder.tv_peoples.setText(school.getZoneName());
        classesHolder.itemView.setTag(Integer.valueOf(position));
    }

    public int getItemCount() {
        return this.schoolList == null ? 0 : this.schoolList.size();
    }
}
