package cn.app.peexam.dialog;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import cn.app.peexam.R;

public class LoadingDialog extends BaseDialog {

    @BindView(R.id.dialog_loading_pb_loading)
    public ProgressBar pb_loading;
    @BindView(R.id.dialog_loading_tv_tips)
    public TextView tv_tips;

    public LoadingDialog(Context context) {
        super(context, R.style.MyDialogStyle);
    }

    protected int getLayoutResId() {
        return R.layout.dialog_loading;
    }

    public void dismiss() {
        super.dismiss();
        this.mainAppication = null;
        this.context = null;
    }

    public void cancel() {
        super.cancel();
        this.mainAppication = null;
        this.context = null;
    }

    public void setTips(String tips) {
        if (tips != null && !tips.isEmpty()) {
            this.tv_tips.setText(tips);
        }
    }
}
