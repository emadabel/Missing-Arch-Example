package com.emadabel.missingarchexample.data.network;

import com.emadabel.missingarchexample.utilities.LiveDataCallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebClient {

    private static final String BASE_URL = "https://api.github.com/";
    private static final Object LOCK = new Object();
    private static WebClient sInstance = null;

    private Webservice mWebservice;

    private WebClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
        mWebservice = retrofit.create(Webservice.class);
    }

    public static WebClient getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WebClient();
            }
        }
        return sInstance;
    }

    public Webservice getWebservice() {
        return mWebservice;
    }
}
