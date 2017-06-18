package cn.app.peexam.service;

import cn.app.peexam.bean.Constant;
import cn.app.peexam.bean.HttpResultGradeClass;
import cn.app.peexam.bean.HttpResultMarkAdd;
import cn.app.peexam.bean.HttpResultProgram;
import cn.app.peexam.bean.HttpResultSearch;
import cn.app.peexam.bean.HttpResultStudent;
import cn.app.peexam.bean.HttpResultSubClasses;
import cn.app.peexam.bean.Program;
import cn.app.peexam.bean.Student;

import java.util.List;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    @POST(Constant.CLASSESLIST)
    Observable<HttpResultGradeClass> getClassList(@Query("userId") int i, @Query("userType") int i2, @Query("schoolId") int i3);

    @POST(Constant.PROJECTLIST)
    Observable<HttpResultProgram<List<Program>>> getProgramList(@Query("planId") int i);

    @POST(Constant.PROJECTLIST)
    Observable<HttpResultProgram<List<Program>>> getProgramList(@Query("planId") int i, @Query("classId") int i2);

    @POST(Constant.STUDENTLIST)
    Observable<HttpResultStudent<List<Student>>> getStudentList(@Query("classId") int i, @Query("projectId") int i2);

    @POST(Constant.SEARCH)
    Observable<HttpResultSearch> getStudentListByStuInfo(@Query("schoolId") int i, @Query("idOrName") String str);

    @POST(Constant.LOGIN)
    Observable<HttpResultSubClasses> login(@Query("userName") String userName, @Query("password") String password, @Query("userType") int userType);

    @POST(Constant.MARKADD)
    Observable<HttpResultMarkAdd> setMarkAdd(@Query("json") String str);
}
