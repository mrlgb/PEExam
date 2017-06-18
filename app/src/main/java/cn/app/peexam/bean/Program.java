package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;

public class Program {

    //id
    @SerializedName("id")
    private int id;

    //最大值
    @SerializedName("max")
    private float max;

    //最小值
    @SerializedName("min")
    private float min;

    //测试项目的名称
    @SerializedName("name")
    private String name;

    //测试的性别
    @SerializedName("sex")
    private int sex;

    //测试过的人数
    @SerializedName("testNumber")
    private int testNumber;

    //类型
    @SerializedName("type")
    private int type = 0;

    //单位
    @SerializedName("unit")
    private String unit;

    //输入类型，保留几位小数
    @SerializedName("inputType")
    private int inputType;

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

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getMax() {
        return this.max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return this.min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getTestNumber() {
        return this.testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }
}
