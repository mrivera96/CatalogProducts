package com.diadema.catalogproducts.services;

import com.diadema.catalogproducts.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by CEK on 23/03/2019.
 */

public class RetrofitBuilder {

    //ip
    private static final String BASE_URL = "http://10.203.101.98:8080/biometriciot/public/api/";

    private final static OkHttpClient client = buildClient();
    private final static Retrofit retrofit = buildRetrofit(client);

    private static OkHttpClient buildClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();

                    Request.Builder builder1 = request.newBuilder()
                            .addHeader("Connection", "close");

                    request = builder1.build();

                    return chain.proceed(request);
                });

        if(BuildConfig.DEBUG){
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        return builder.build();
    }

    private static Retrofit buildRetrofit(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    public static <T> T createService(Class<T> service){
        return retrofit.create(service);
    }

    public static <T> T createServiceWithAuth(Class<T> service){

        OkHttpClient newClient = client.newBuilder().addInterceptor(chain -> {

            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            request = builder.build();
            return chain.proceed(request);
        }).authenticator(CustomAuthenticator.getInstance()).readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS).build();

        Retrofit newRetrofit = retrofit.newBuilder().client(newClient).build();
        return newRetrofit.create(service);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}