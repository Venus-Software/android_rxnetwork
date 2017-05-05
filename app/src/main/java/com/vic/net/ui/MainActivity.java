package com.vic.net.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.vic.net.R;
import com.vic.net.api.ApiService;
import com.vic.net.vo.WeatherVo;
import com.vic.rxnetsdk.RxRetrofit;
import com.vic.rxnetsdk.RxSubscriber;

import rx.android.schedulers.AndroidSchedulers;

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
//                request2();
                break;
        }
    }

    /**
     * 请求天气信息
     */
    private void requestWeatherInfo() {
        RxRetrofit.getInstance()
                .create(ApiService.class)
                .queryWeather("上海", "c835721be56ed3b6e603c6873625d4d5")
//                .queryWeatherByScalars("上海", "c835721be56ed3b6e603c6873625d4d5")
                .compose(this.<WeatherVo>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<WeatherVo>() {
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

//    private void request2() {
//        RxRetrofit.getInstance()
//                .create(ApiService.class)
//                .queryWeather("上海", "c835721be56ed3b6e603c6873625d4d5")
//                .compose(this.<WeatherVo>bindToLifecycle())
//
//    }
}
