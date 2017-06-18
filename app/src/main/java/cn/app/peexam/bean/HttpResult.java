package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;

public abstract class HttpResult<T> {
    @SerializedName("code")
    private int code;
    @SerializedName("result")
    private boolean result;

    public abstract T getData();

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isResult() {
        return this.result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
