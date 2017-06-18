package cn.app.peexam.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import butterknife.ButterKnife;
import cn.app.peexam.MainAppication;

public abstract class BaseDialog extends Dialog {
    protected Context context;
    protected MainAppication mainAppication;

    protected abstract int getLayoutResId();

    public BaseDialog(Context context) {
        super(context);
        this.context = context;
        this.mainAppication = (MainAppication) context.getApplicationContext();
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.mainAppication = (MainAppication) context.getApplicationContext();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutResId = getLayoutResId();
        if (layoutResId == 0) {
            dismiss();
            return;
        }
        setContentView(layoutResId);
        ButterKnife.bind((Dialog) this);
        initDialog();
    }

    protected void initDialog() {
    }
}
