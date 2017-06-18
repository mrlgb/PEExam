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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.app.peexam.MainAppication;
import cn.app.peexam.R;
import cn.app.peexam.activity.ProgramActivity.OnResponseGradeClassListener;
import cn.app.peexam.activity.ResultActivity;
import cn.app.peexam.bean.GradeClass;
import cn.app.peexam.bean.Program;

import java.util.List;

public class ListProgramsAdapter extends Adapter<ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private OnResponseGradeClassListener listener;
    private List<Program> programsList;

    public class ProgramsHolder extends ViewHolder implements OnClickListener {
        private View itemView;
        @BindView(R.id.adapter_list_programs_pb_percent)
        public ProgressBar pb_percent;
        @BindView(R.id.adapter_list_programs_rl_percent)
        public RelativeLayout rl_percent;
        @BindView(R.id.adapter_list_programs_tv_percent)
        public TextView tv_percent;
        @BindView(R.id.adapter_list_programs_tv_program)
        public TextView tv_program;

        public ProgramsHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind((Object) this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(ListProgramsAdapter.this.context, ResultActivity.class);
            intent.putExtra("position", ((Integer) this.itemView.getTag()).intValue());
            intent.putExtra("class", ListProgramsAdapter.this.listener.getGradeClassPosition());
            ListProgramsAdapter.this.context.startActivity(intent);
        }
    }

    public ListProgramsAdapter(Context context, List<Program> programsList, OnResponseGradeClassListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.programsList = programsList;
        this.listener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProgramsHolder(this.inflater.inflate(R.layout.adapter_list_programs, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ProgramsHolder programsHolder = (ProgramsHolder) holder;
        Program program = (Program) this.programsList.get(position);
        programsHolder.tv_program.setText(program.getName());
        int number = ((GradeClass) ((MainAppication) this.context.getApplicationContext()).getGradeClassList().get(this.listener.getGradeClassPosition())).getNumber();
        programsHolder.pb_percent.setMax(number);
        programsHolder.pb_percent.setProgress(program.getTestNumber());
        programsHolder.tv_percent.setText(program.getTestNumber() + "/" + number);
        programsHolder.itemView.setTag(Integer.valueOf(position));
    }

    public int getItemCount() {
        return this.programsList == null ? 0 : this.programsList.size();
    }

    public void update(List<Program> programsList) {
        this.programsList = programsList;
        notifyDataSetChanged();
    }
}
