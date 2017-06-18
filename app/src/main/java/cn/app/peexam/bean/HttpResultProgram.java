package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;

public class HttpResultProgram<T> extends HttpResult<T> {
    @SerializedName("projectList")
    private T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }
}
