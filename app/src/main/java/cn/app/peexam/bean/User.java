package cn.app.peexam.bean;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("ascription")
    private String ascription;
    @SerializedName("lastLoginTime")
    private LastLoginTime lastLoginTime;
    @SerializedName("password")
    private String password;
    @SerializedName("realName")
    private String realName;
    @SerializedName("userId")
    private int userId;
    @SerializedName("userType")
    private int userType;
    @SerializedName("userName")
    private String username;

    public static class LastLoginTime {
        @SerializedName("date")
        private int date;
        @SerializedName("day")
        private int day;
        @SerializedName("hours")
        private int hours;
        @SerializedName("minutes")
        private int minutes;
        @SerializedName("month")
        private int month;
        @SerializedName("seconds")
        private int seconds;
        @SerializedName("time")
        private long time;
        @SerializedName("timezoneOffset")
        private int timezoneOffset;
        @SerializedName("year")
        private int year;

        public int getDate() {
            return this.date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getDay() {
            return this.day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHours() {
            return this.hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMinutes() {
            return this.minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getMonth() {
            return this.month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getSeconds() {
            return this.seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public long getTime() {
            return this.time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getTimezoneOffset() {
            return this.timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getYear() {
            return this.year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }

    public void setUser(User user) {
        setAscription(user.getAscription());
        setPassword(user.getPassword());
        setRealName(user.getRealName());
        setUserId(user.userId);
        setUsername(user.getUsername());
        setUserType(user.getUserType());
        setLastLoginTime(user.getLastLoginTime());
    }

    public String getAscription() {
        return this.ascription;
    }

    public void setAscription(String ascription) {
        this.ascription = ascription;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public LastLoginTime getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(LastLoginTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
