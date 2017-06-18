package cn.app.peexam.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String getNowTimeString() {
        return new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date());
    }

    public static String getTimeFromTimeStamp(String str) {
        if (str == null || str.isEmpty()) {
            return getNowTimeString();
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new Long(str).longValue()));
    }
}
