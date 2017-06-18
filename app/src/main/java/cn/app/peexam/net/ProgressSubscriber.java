package cn.app.peexam.net;

import android.content.Context;
import cn.app.peexam.service.ProgressCancelListener;
import cn.app.peexam.service.SubscriberOnNextListener;
import cn.app.peexam.util.ToastUtil;
import rx.Subscriber;

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private Context context;
    private ProgressDialogHandler mProgressDialogHandler;
    private SubscriberOnNextListener mSubscriberOnNextListener;
    private String tips;

    public ProgressSubscriber(Context context, SubscriberOnNextListener mSubscriberOnNextListener) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    public ProgressSubscriber(Context context, SubscriberOnNextListener mSubscriberOnNextListener, String tips) {
        this(context, mSubscriberOnNextListener);
        this.tips = tips;
    }

    private void showProgressDialog() {
        if (this.mProgressDialogHandler != null) {
            this.mProgressDialogHandler.obtainMessage(1, this.tips).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (this.mProgressDialogHandler != null) {
            this.mProgressDialogHandler.obtainMessage(2).sendToTarget();
            this.mProgressDialogHandler = null;
        }
    }

    public void onStart() {
        showProgressDialog();
    }

    public void onCompleted() {
        dismissProgressDialog();
    }

    public void onError(Throwable e) {
        dismissProgressDialog();
        ToastUtil.showToast(this.context, e.getMessage());
        this.mSubscriberOnNextListener.onNext(null);
    }

    public void onNext(T t) {
        this.mSubscriberOnNextListener.onNext(t);
    }

    public void onCancelProgress() {
        if (!isUnsubscribed()) {
            unsubscribe();
        }
    }
}
