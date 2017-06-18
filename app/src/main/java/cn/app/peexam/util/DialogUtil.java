package cn.app.peexam.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.app.peexam.R;

public class DialogUtil {
    private AlertDialog dialog;
    @BindView(2131427515)
    public TextView textView;
    private View view;

    public DialogUtil(Context context, View view) {
        this.view = view;
        ButterKnife.bind((Object) this, view);
        this.dialog = new Builder(context.getApplicationContext(), R.style.NewAlertDialog).create();
        this.dialog.getWindow().setType(2003);
    }

    public void setTextView(String s) {
        this.textView.setText(s);
    }

    public void showDialog() {
        this.dialog.show();
        this.dialog.setContentView(this.view);
        this.dialog.getWindow().setLayout((int) this.dialog.getContext().getResources().getDimension(R.dimen.x200), (int) this.dialog.getContext().getResources().getDimension(R.dimen.y50));
    }
}
