package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;

public class School {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("zoneName")
    private String zoneName;

    public void setSchool(School school) {
        setId(school.getId());
        setName(school.getName());
        setZoneName(school.getZoneName());
    }

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

    public String getZoneName() {
        return this.zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
