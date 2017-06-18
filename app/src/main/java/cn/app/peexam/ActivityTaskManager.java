package cn.app.peexam;

import android.app.Activity;
import android.os.Process;

import java.util.HashMap;

public class ActivityTaskManager {

    private static ActivityTaskManager activityTaskManager = null;
    private HashMap<String, Activity> activityMap;

    private ActivityTaskManager() {
        this.activityMap = null;
        this.activityMap = new HashMap();
    }

    public static synchronized ActivityTaskManager getInstance() {
        ActivityTaskManager activityTaskManager = null;
        synchronized (ActivityTaskManager.class) {
            if (ActivityTaskManager.activityTaskManager == null) {
                activityTaskManager = new ActivityTaskManager();
            }
            ActivityTaskManager.activityTaskManager = activityTaskManager;
        }
        return activityTaskManager;
    }

    public Activity putActivity(String name, Activity activity) {
        return (Activity) this.activityMap.put(name, activity);
    }

    public Activity getActivity(String name) {
        return (Activity) this.activityMap.get(name);
    }

    public boolean isEmpty() {
        return this.activityMap.isEmpty();
    }

    public int size() {
        return this.activityMap.size();
    }

    public boolean containsName(String name) {
        return this.activityMap.containsKey(name);
    }

    public boolean containsActivity(Activity activity) {
        return this.activityMap.containsValue(activity);
    }

    public void closeAllActivity() {
        for (String string : this.activityMap.keySet()) {
            finisActivity((Activity) this.activityMap.get(string));
        }
        this.activityMap.clear();
        Process.killProcess(Process.myPid());
    }

    public void closeAllActivityExceptOne(String nameSpecified) {
        Activity activitySpecified = (Activity) this.activityMap.get(nameSpecified);
        for (String name : this.activityMap.keySet()) {
            if (!name.equals(nameSpecified)) {
                finisActivity((Activity) this.activityMap.get(name));
            }
        }
        this.activityMap.clear();
        this.activityMap.put(nameSpecified, activitySpecified);
    }

    public void removeActivity(String name) {
        finisActivity((Activity) this.activityMap.remove(name));
    }

    private final void finisActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }
}
