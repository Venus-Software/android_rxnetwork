# android_rxnetwork

## Usage

### in Gradle
~~~
repositories {
    maven { url "https://jitpack.io" }
}
~~~
~~~
dependencies {
	  compile 'com.github.Venus-Software:android_rxnetwork:v0.0.1-beta'
}
~~~

There is a simple initialization step which occurs in your Application class:

~~~
RxRetrofit.initInstance()
                .showRequestInChrome(this, true)
                .baseUrl("http://www.baidu.com/")
                .initOkhttpClient()
                .timeOut(60, 60, 10)
                //请求再处理
                .addReprocessRequestInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        return chain.proceed(request);
                    }
                })
                .addConverterFactory(GsonConverterFactory.create())
                .initialize();
~~~
