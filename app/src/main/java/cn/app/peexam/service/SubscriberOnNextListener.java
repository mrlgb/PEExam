package cn.app.peexam.service;

public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
