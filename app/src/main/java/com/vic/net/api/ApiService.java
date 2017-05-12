package com.vic.net.api;

import android.support.annotation.NonNull;

import com.vic.net.vo.WeatherVo;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by liu song on 2017/5/3.
 */

public interface ApiService {

    @GET("http://op.juhe.cn/onebox/weather/quer")
    Observable<WeatherVo> queryWeather(
            @NonNull @Query("cityname") String cityname,
            @NonNull @Query("key") String key
    );

    //ScalarsConverterFactory返回原始数据
    @GET("http://op.juhe.cn/onebox/weather/query")
    Observable<String> queryWeatherByScalars(
            @NonNull @Query("cityname") String cityname,
            @NonNull @Query("key") String key
    );

    //GET-------------------------------------------

    /**
     * method  请求方法，不区分大小写
     * path    路径
     * hasBody 是否有请求体
     */
    @HTTP(method = "get", path = "test/{id}", hasBody = false)
    Observable<ResponseBody> testHttp(@Path("id") int id);

    //一般的Get请求
    @GET("test/id")
    Observable<ResponseBody> testGet(@Query("id") int id);

    //一般的Get请求,query集合
    @GET("test/id")
    Observable<ResponseBody> testQueryMap(@QueryMap Map<String, String> params);

    //一般的Get请求，URL上用占位符
    @GET("test/{id}")
    Observable<ResponseBody> testGetPath(@Path("id") int id);

    //URL上用占位符和一个键对应多个值
    @GET("test/{id}")
    Observable<ResponseBody> testGetByArray(@Path("id") int id, @Query("key") String... key);

    //动态加url
    @GET
    Observable<ResponseBody> testUrl(@Url String url);

    //静态添加header
    @Headers("key: value")
    @GET("test")
    Observable<ResponseBody> testStaticHeader();

    //静态添加多个header
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @GET("test")
    Observable<ResponseBody> testStaticHeaders();

    //动态加header
    @GET("test")
    Observable<ResponseBody> testHeader(@Header("xxx") String header);

    //动态加headers
    @GET("test")
    Observable<ResponseBody> testHeaderMap(@HeaderMap Map<String, String> headers);

    @GET
    Observable<ResponseBody> get(
            @Url String url,
            @QueryMap Map<String, String> map
    );

    //POST-------------------------------------------

    //post请求
    @FormUrlEncoded
    @POST("http://op.juhe.cn/onebox/weather/query")
    Observable<WeatherVo> testPost(
            @NonNull @Field("cityname") String cityname,
            @Field("content") String content,
            @NonNull @Field("key") String key
    );

    //post请求,Filed集合
    @FormUrlEncoded
    @POST("test")
    Observable<ResponseBody> testPost(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("book/reviews")
    Observable<ResponseBody> testContent(
            @Field("book") String bookId,
            @Field("title") String title,
            @Field("content") String content,
            @Field("rating") String rating
    );

    //非表单json请求体,参数可以是一个Vo类封装数据
    @FormUrlEncoded
    @POST("test")
    Observable<ResponseBody> testBody(@Body RequestBody body);

}
