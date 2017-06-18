package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HttpResultSubClasses {

    @SerializedName("code")
    private int code;
    private List<Object> list = new ArrayList();
    @SerializedName("result")
    private boolean result;
    @SerializedName("schoolList")
    private List<School> subData;
    @SerializedName("user")
    private User user;

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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<School> getSubData() {
        return this.subData;
    }

    public void setSubData(List<School> subData) {
        this.subData = subData;
    }

    public List<Object> getList() {
        return this.list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public void addListData(Object obj) {
        this.list.add(obj);
    }
}
