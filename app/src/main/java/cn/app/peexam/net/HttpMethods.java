package cn.app.peexam.net;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.app.peexam.bean.Constant;
import cn.app.peexam.bean.HttpResult;
import cn.app.peexam.bean.HttpResultGradeClass;
import cn.app.peexam.bean.HttpResultMarkAdd;
import cn.app.peexam.bean.HttpResultSearch;
import cn.app.peexam.bean.HttpResultSubClasses;
import cn.app.peexam.bean.Program;
import cn.app.peexam.bean.Student;
import cn.app.peexam.service.ApiService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 胡仲友 on 2016/12/20.
 */
public class HttpMethods {

    /**
     * 连接超时时限
     * s(秒)
     */
    private final long ConnectionTime = 20;
    /**
     * 读取超时时限
     * s(秒)
     */
    private final long ReadTime = 30;
    /**
     * 写入超时时限
     * s(秒)
     */
    private final long WriteTime = 30;

    private static HttpMethods httpMethods;

    private Retrofit retrofit;
    private ApiService apiService;

    /**
     * 构造方法
     */
    private HttpMethods() {
        Gson gson = new GsonBuilder().serializeNulls().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.HTTP)
                //配置转化库，默认是Gson
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addConverterFactory(InputStreamConvertFactory.create())
                //配置回调库，采用RxJava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //设置OKHttpClient为网络客户端
                .client(genericClient())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    /**
     * 保证retrofit唯一
     *
     * @return
     */
    public static HttpMethods getInstance() {
        synchronized (HttpMethods.class) {
            if (httpMethods == null) {
                httpMethods = new HttpMethods();
            }
        }
        return httpMethods;
    }

    /**
     * 设置Retrofit参数
     */
    private OkHttpClient genericClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ConnectionTime, TimeUnit.SECONDS)
                .readTimeout(ReadTime, TimeUnit.SECONDS)
                .writeTimeout(WriteTime, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1")
//                        .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .build();
                        Response response = chain.proceed(request);
                        Log.i("info", response.request().url().toString());
                        return response;
                    }
                }).build();
        return client;
    }

    //添加线程管理并订阅
    private void toSubscribe(Observable o, Subscriber s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 登陆
     *
     * @param subscriber
     * @param username   用户名
     * @param password   密码
     * @param userType   用户类型
     */
    public void login(Subscriber<List<Object>> subscriber, String username, String password, int userType) {
        Observable observable = apiService.login(username, password, userType).map(new HttpResultFuncSubClass());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取班级列表
     *
     * @param subscriber
     * @param userId     用户id
     * @param userType   用户类型
     * @param schoolId   学校id
     */
    public void getClassList(Subscriber<List<Object>> subscriber, int userId, int userType, int schoolId) {
        Observable observable = apiService.getClassList(userId, userType, schoolId).map(new HttpResultFuncGradeClass());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取测试项目列表
     *
     * @param subscriber
     * @param planId     学校所对应的测试方案ID
     */
    public void getProgramList(Subscriber<List<Program>> subscriber, int planId, int classId) {
        Observable observable = apiService.getProgramList(planId, classId).map(new HttpResultFunc<List<Program>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取测试项目列表
     *
     * @param subscriber
     * @param planId     学校所对应的测试方案ID
     */
    public void getProgramList(Subscriber<List<Program>> subscriber, int planId) {
        Observable observable = apiService.getProgramList(planId).map(new HttpResultFunc<List<Program>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取学生列表
     *
     * @param subscriber
     * @param classId    班级id
     * @param projectId  项目id
     */
    public void getStudentList(Subscriber<List<Student>> subscriber, int classId, int projectId) {
        Observable observable = apiService.getStudentList(classId, projectId).map(new HttpResultFunc<List<Student>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 根据学生姓名或者学号查找学生
     *
     * @param subscriber
     * @param schoolId   学校ID
     * @param idOrName   学生学号或者学生姓名
     */
    public void getStudentListByStuInfo(Subscriber<HttpResultSearch> subscriber, int schoolId, String idOrName) {
        Observable observable = apiService.getStudentListByStuInfo(schoolId, idOrName).map(new HttpResultFuncSearch());
        toSubscribe(observable, subscriber);
    }

    /**
     * 提交数据
     *
     * @param subscriber
     * @param requestBody
     */
    public void setMarkAdd(Subscriber<Boolean> subscriber, String requestBody) {
        Observable observable = apiService.setMarkAdd(requestBody).map(new HttpResultGradeClassFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCode() != 0 || !httpResult.isResult()) {
                throw new ApiException(httpResult.getCode());
            }
            return httpResult.getData();
        }
    }

    private class HttpResultGradeClassFunc implements Func1<HttpResultMarkAdd, Boolean> {

        @Override
        public Boolean call(HttpResultMarkAdd httpResultMarkAdd) {
            if (httpResultMarkAdd.getCode() != 0 || !httpResultMarkAdd.isResult()) {
                throw new ApiException(httpResultMarkAdd.getCode());
            }
            return httpResultMarkAdd.isResult();
        }
    }

    private class HttpResultFuncSubClass implements Func1<HttpResultSubClasses, List<Object>> {
        @Override
        public List<Object> call(HttpResultSubClasses httpResultSubClasses) {
            if (httpResultSubClasses.getCode() != 0 || !httpResultSubClasses.isResult()) {
                throw new ApiException(httpResultSubClasses.getCode());
            }
            httpResultSubClasses.addListData(httpResultSubClasses.getUser());
            httpResultSubClasses.addListData(httpResultSubClasses.getSubData());
            return httpResultSubClasses.getList();
        }
    }

    private class HttpResultFuncGradeClass implements Func1<HttpResultGradeClass, List<Object>> {
        @Override
        public List<Object> call(HttpResultGradeClass httpResultGradeClass) {

            if (httpResultGradeClass.getCode() != 0 || !httpResultGradeClass.isResult()) {
                throw new ApiException(httpResultGradeClass.getCode());
            }
            httpResultGradeClass.addListData(httpResultGradeClass.getPlanId());
            httpResultGradeClass.addListData(httpResultGradeClass.getData());
            return httpResultGradeClass.getObjectList();
        }
    }

    private class HttpResultFuncSearch implements Func1<HttpResultSearch, HttpResultSearch> {

        @Override
        public HttpResultSearch call(HttpResultSearch httpResultSearch) {
            if (httpResultSearch.getCode() != 0 || !httpResultSearch.isResult()) {
                throw new ApiException(httpResultSearch.getCode());
            }
            return httpResultSearch;
        }
    }
}