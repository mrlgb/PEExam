package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HttpResultSearch {
    @SerializedName("code")
    private int code;
    @SerializedName("projectList")
    private List<Program> programList;
    @SerializedName("result")
    private boolean result = false;
    @SerializedName("studentList")
    private List<Student> studentList;

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

    public List<Student> getStudentList() {
        return this.studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<Program> getProgramList() {
        return this.programList;
    }

    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }
}
