package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("id")
    private int id;
    @SerializedName("mark")
    private String mark;
    @SerializedName("name")
    private String name;
    @SerializedName("sex")
    private int sex;
    @SerializedName("studentNo")
    private String stuNo;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getStuNo() {
        return this.stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
