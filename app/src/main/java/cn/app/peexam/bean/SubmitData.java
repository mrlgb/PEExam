package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SubmitData {
    @SerializedName("markList")
    private List<String> markList;
    @SerializedName("projectId")
    private int projectId;

    public int getProjectId() {
        return this.projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public List<String> getMarkList() {
        return this.markList;
    }

    public void setMarkList(List<String> markList) {
        this.markList = markList;
    }
}
