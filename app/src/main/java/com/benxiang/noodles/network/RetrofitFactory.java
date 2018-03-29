package com.benxiang.noodles.network;

import com.benxiang.noodles.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 刘圣如 on 2017/9/5.
 */

public class RetrofitFactory {
    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls().create();

    private static final int DEFAULT_TIMEOUT = 12;

    private OkHttpClient configClient(boolean accessToken) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Interceptor header = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.addHeader("X-Client-Platform", "Android");
                requestBuilder.addHeader("X-Client-Version", BuildConfig.VERSION_NAME);
                requestBuilder.addHeader("X-Client-Build", String.valueOf(BuildConfig.VERSION_CODE));
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        }
        //clientBuilder.addNetworkInterceptor(header);
        return clientBuilder.build();
    }

    public <T> T configRetrofit(Class<T> service, String url, boolean accessToken) {
        Retrofit retrofit = new Retrofit.Builder().client(configClient(accessToken))
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }
}
