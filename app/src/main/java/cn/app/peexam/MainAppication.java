package cn.app.peexam;

import android.app.Activity;
import android.app.Application;

import cn.app.peexam.bean.GradeClass;
import cn.app.peexam.bean.Program;
import cn.app.peexam.bean.School;
import cn.app.peexam.bean.User;
import cn.app.peexam.net.HttpMethods;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig.Builder;

public class MainAppication extends Application {
    private ActivityTaskManager activityTaskManager;
    private List<GradeClass> gradeClassList;
    private List<GradeClass> filterGradeClassList;
    private HttpMethods httpMethods;
    private List<Program> programList;
    private List<School> schoolList;
    private User user;
    private boolean useFilter=false;

    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new Builder().setDefaultFontPath("fonts/sanxing.ttf").setFontAttrId(R.attr.fontPath).build());
        this.activityTaskManager = ActivityTaskManager.getInstance();
        this.httpMethods = HttpMethods.getInstance();
        CrashHandler.getInstance().init(this);
    }

    public void managerActivity(Activity activity, boolean isFlag) {
        if (activity == null) {
            this.activityTaskManager.closeAllActivity();
        } else if (isFlag) {
            this.activityTaskManager.putActivity(activity.getClass().getSimpleName(), activity);
        } else if (!isFlag) {
            this.activityTaskManager.removeActivity(activity.getClass().getSimpleName());
        }
    }

    public HttpMethods getHttpMethods() {
        return this.httpMethods;
    }

    public User getUser() {
        return this.user;
    }

    public boolean setUser(User user) {
        if (user == null) {
            return false;
        }
        this.user = user;
        return true;
    }

    public List<School> getSchoolList() {
        return this.schoolList;
    }

    public boolean setSchoolList(List<School> schoolList) {
        if (schoolList == null || schoolList.size() == 0) {
            return false;
        }
        if (this.schoolList == null) {
            this.schoolList = schoolList;
        } else {
            this.schoolList.addAll(schoolList);
        }
        return true;
    }

    public List<GradeClass> getGradeClassList() {
        return this.gradeClassList;
    }

    public boolean setGradeClassList(List<GradeClass> gradeClassList) {
        if (gradeClassList == null) {
            return false;
        }
        if (this.gradeClassList == null) {
            this.gradeClassList = gradeClassList;
        } else {
            this.gradeClassList.addAll(gradeClassList);
        }
        return true;
    }

    public List<GradeClass> getFilterGradeClassList() {
        return filterGradeClassList;
    }

    public boolean setFilterGradeClassList(List<GradeClass> filterGradeClassList){
        if (filterGradeClassList == null) {
            return false;
        }
        if (this.filterGradeClassList == null) {
            this.filterGradeClassList = filterGradeClassList;
        } else {
            this.filterGradeClassList.clear();
            this.filterGradeClassList.addAll(filterGradeClassList);
        }
        return true;
    }

    public List<Program> getProgramList() {
        return this.programList;
    }

    public boolean setProgramList(List<Program> programList) {
        if (programList == null) {
            return false;
        }
        if (this.programList == null) {
            this.programList = programList;
        } else {
            this.programList.addAll(programList);
        }
        return true;
    }

    public boolean isUseFilter() {
        return useFilter;
    }

    public void setUseFilter(boolean useFilter) {
        this.useFilter = useFilter;
    }
}
