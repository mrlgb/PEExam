package cn.app.peexam.net;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import cn.app.peexam.R;
import cn.app.peexam.dialog.LoadingDialog;
import cn.app.peexam.service.ProgressCancelListener;

public class ProgressDialogHandler extends Handler {
    public static final int DISMISS_PROGRESS_DIALOG = 2;
    public static final int SHOW_PROGRESS_DIALOG = 1;
    private boolean cancelable;
    private Context context;
    private LoadingDialog loadingDialog;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener, boolean cancelable) {
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(String tips) {
        if (this.loadingDialog == null) {
            this.loadingDialog = new LoadingDialog(this.context);
            this.loadingDialog.setCancelable(this.cancelable);
            if (this.cancelable) {
                this.loadingDialog.setOnCancelListener(new OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        ProgressDialogHandler.this.mProgressCancelListener.onCancelProgress();
                    }
                });
            }
            if (!this.loadingDialog.isShowing()) {
                this.loadingDialog.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (this.loadingDialog != null) {
            this.loadingDialog.dismiss();
            this.loadingDialog = null;
        }
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                initProgressDialog(msg.obj == null ? this.context.getResources().getString(R.string.load) : msg.obj.toString());
                return;
            case 2:
                dismissProgressDialog();
                return;
            default:
                return;
        }
    }
}
