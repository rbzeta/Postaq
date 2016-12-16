package app.rbzeta.postaq.rest;

import android.support.v4.util.LruCache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import app.rbzeta.postaq.application.AppConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Robyn on 11/28/2016.
 */

public class NetworkService {
    private static final String BASE_URL = AppConfig.BASE_URL;
    private NetworkApi mNetworkAPI;
    private OkHttpClient okHttpClient;
    private LruCache<Class<?>, Observable<?>> apiObservables;

    public NetworkService(){
        this(BASE_URL);
    }

    public NetworkService(String baseUrl){
        okHttpClient = buildClient();
        apiObservables = new LruCache<>(10);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        mNetworkAPI = retrofit.create(NetworkApi.class);

    }

    public NetworkApi getNetworkAPI(){
        return mNetworkAPI;
    }

    public OkHttpClient buildClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                return response;
            }
        });

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request().newBuilder().addHeader("Accept", "application/json").build();
                return chain.proceed(request);
            }
        });

        builder.connectTimeout(1, TimeUnit.MINUTES);

        return  builder.build();
    }

    public void clearCache(){
        apiObservables.evictAll();
    }

    public Observable<?> getPreparedObservable(Observable<?> unPreparedObservable,
                                               Class<?> clazz,
                                               boolean cacheObservable,
                                               boolean useCache){

        Observable<?> preparedObservable = null;

        if(useCache)
            preparedObservable = apiObservables.get(clazz);

        if(preparedObservable!=null)
            return preparedObservable;

        preparedObservable = unPreparedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        if(cacheObservable){
            preparedObservable = preparedObservable.cache();
            apiObservables.put(clazz, preparedObservable);
        }

        return preparedObservable;
    }
}