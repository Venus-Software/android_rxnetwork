package com.vic.net.api;

import android.support.annotation.NonNull;

import com.vic.net.vo.WeatherVo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liu song on 2017/5/3.
 */

public interface ApiService {

    @GET("http://op.juhe.cn/onebox/weather/query")
    Observable<WeatherVo> queryWeather(
            @NonNull @Query("cityname") String cityname,
            @NonNull @Query("key") String key
    );

}
