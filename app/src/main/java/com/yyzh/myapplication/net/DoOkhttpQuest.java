package com.yyzh.myapplication.net;

import android.util.Log;

import com.yyzh.myapplication.MyApplication;
import com.yyzh.myapplication.ThreadUtli;
import com.yyzh.myapplication.net.cache.HttpCache;
import com.yyzh.myapplication.net.cookies.CookieManger;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class DoOkhttpQuest {
    private static DoOkhttpQuest mInstance;
    private DoOkhttpQuest(){}
    public static DoOkhttpQuest getInstance(){
        if(mInstance==null){
            synchronized (DoOkhttpQuest.class){
                if(mInstance==null){
                    mInstance=new DoOkhttpQuest();
                }
            }
        }
        return mInstance;
    }
    private static final int TIMEOUT_READ = 5;
    private static final int TIMEOUT_CONNECTION = 5;

    //    private static CacheInterceptor cacheInterceptor = new CacheInterceptor();
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //SSL证书
            .sslSocketFactory(TrustManager.getUnsafeOkHttpClient())
            .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            //打印日志
//            .addInterceptor(getHeaderInterceptor())
            .addInterceptor(getInterceptor())
            //设置Cache拦截器
//            .addNetworkInterceptor(cacheInterceptor)
//            .addInterceptor(cacheInterceptor)
//            .cache(HttpCache.getCache())
            // 设置 Cookie
//            .cookieJar(new CookieManger(MyApplication.mInstance))
            .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            //失败重连
            .retryOnConnectionFailure(true)
            .build();

    private static Interceptor getHeaderInterceptor() {
        //增加头部信息
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(build);
            }
        };
        return headerInterceptor;
    }

    private static Interceptor getInterceptor() {
        //开启Log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            private StringBuilder mMessage = new StringBuilder();

            @Override
            public void log(String message) {

                // 请求或者响应开始
                if (message.startsWith("--> POST") || message.startsWith("--> GET")) {
                    mMessage.setLength(0);
                }
                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if ((message.startsWith("{") && message.endsWith("}"))
                        || (message.startsWith("[") && message.endsWith("]"))) {
                    message = JsonUtil.formatJson(JsonUtil.decodeUnicode(message));
                }
                mMessage.append(message.concat("\n"));
                // 请求或者响应结束，打印整条日志
                if (message.startsWith("<-- END HTTP")) {
                    Log.e("callback", mMessage.toString());
                }
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return logInterceptor;
    }

    public void get(final String url, final IHttpCallBack<String> callBack) {

        ThreadUtli.getInstance().runOnIO(new ThreadUtli.ThreadIOHelper() {
            @Override
            public void runOnIO() {
                final Request request = new Request.Builder().url(url).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        ThreadUtli.getInstance().runOnUI(new ThreadUtli.ThreadUIHelper() {
                            @Override
                            public void runOnUI() {
                                if(callBack!=null){
                                    callBack.failure(e.toString());
                                }
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        ThreadUtli.getInstance().runOnUI(new ThreadUtli.ThreadUIHelper() {
                            @Override
                            public void runOnUI() {
                                if(callBack!=null){
                                    try {
                                        callBack.success(response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                });
            }

        });

    }
    
    public void post(final String url, final Map<String, String> param, final IHttpCallBack<String> callBack){
        ThreadUtli.getInstance().runOnIO(new ThreadUtli.ThreadIOHelper() {
            @Override
            public void runOnIO() {
                FormBody.Builder formBody = new FormBody.Builder();
                if(!param.isEmpty()) {
                    for (Map.Entry<String,String> entry: param.entrySet()) {
                        formBody.add(entry.getKey(),entry.getValue());
                    }
                }
                RequestBody form = formBody.build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.post(form)
                        .url(url)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        ThreadUtli.getInstance().runOnUI(new ThreadUtli.ThreadUIHelper() {
                            @Override
                            public void runOnUI() {
                                if(callBack!=null){
                                    callBack.failure(e.toString());
                                }
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        ThreadUtli.getInstance().runOnUI(new ThreadUtli.ThreadUIHelper() {
                            @Override
                            public void runOnUI() {
                                if(callBack!=null){
                                    try {
                                        callBack.success(response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
