package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class HttpResultGradeClass {
    @SerializedName("code")
    private int code;
    @SerializedName("classList")
    private List<GradeClass> data;
    private List<Object> objectList = new ArrayList();
    @SerializedName("planId")
    private int planId;
    @SerializedName("result")
    private boolean result;

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

    public List<GradeClass> getData() {
        return this.data;
    }

    public void setData(List<GradeClass> data) {
        this.data = data;
    }

    public int getPlanId() {
        return this.planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public List<Object> getObjectList() {
        return this.objectList;
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
    }

    public void addListData(Object object) {
        this.objectList.add(object);
    }
}
