package com.vic.net.ui;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.vic.net.R;
import com.vic.net.api.ApiService;
import com.vic.net.vo.WeatherVo;
import com.vic.rxnetsdk.RxApi;
import com.vic.rxnetsdk.RxRetrofit;
import com.vic.rxnetsdk.RxSubscriber;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;


public class MainActivity extends RxAppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
//                requestWeatherInfo();
                testX();
                break;
        }
    }

    private void testX() {

        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("cityname", "上海");
        arrayMap.put("key", "c835721be56ed3b6e603c6873625d4d5");

//        RxRetrofit.getInstance()
//                .create(entityClass)
//                .get("http://op.juhe.cn/onebox/weather/query", arrayMap)
//                .compose(this.<ResponseBody>bindToLifecycle())
//                .subscribe(new RxSubscriber<ResponseBody>(this) {
//                    @Override
//                    protected void callBack(ResponseBody responseBody) {
//                        Toast.makeText(MainActivity.this,responseBody.toString() , Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    /**
     * 请求天气信息
     */
    private void requestWeatherInfo() {
        RxRetrofit.getInstance()
                .create(ApiService.class)
                .testPost("上海", "good", "c835721be56ed3b6e603c6873625d4d5")
//                .queryWeatherByScalars("上海", "c835721be56ed3b6e603c6873625d4d5")
                .compose(this.<WeatherVo>bindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<WeatherVo>(this) {
                    @Override
                    protected void callBack(WeatherVo dataSet) {
                        Toast.makeText(MainActivity.this, "onNext=天气预报时间：" + dataSet.getResult().getData().getWeather().get(0).getDate(), Toast.LENGTH_SHORT).show();
                    }
                });
//                .subscribe(new CommonSubscriber<WeatherVo>() {
//
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                    }
//
//                    @Override
//                    public void onCompleted() {
////                        Toast.makeText(RetrofitActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(WeatherVo dataSet) {
////                        Toast.makeText(MainActivity.this, ""+dataSet, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, "onNext=天气预报时间：" + dataSet.getResult().getData().getWeather().get(0).getDate(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
}
