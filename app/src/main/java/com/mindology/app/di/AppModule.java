package com.mindology.app.di;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.mindology.app.R;
import com.mindology.app.models.User;
import com.mindology.app.util.Constants;
import com.mindology.app.util.SharedPrefrencesHelper;
import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.logger.Loggable;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance(final Application application) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder();

                String token = provideSharedPreferences(application).getToken();
                if (!token.isEmpty()) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }
                requestBuilder.method(original.method(), original.body());
                requestBuilder.addHeader("Content-Type", "*/*");
                Request request = requestBuilder.build();

                Response response = chain.proceed(request);
                return response;
            }
        }).addInterceptor(new CurlInterceptor(new Loggable() {
            @Override
            public void log(String message) {
                Log.v("Ok2Curl", message);
            }
        })).build();
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }


    @Singleton
    @Provides
    @Named("ContactUs")
    static Retrofit provideContactRetrofitInstance(final Application application) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    @Named("Loqate")
    static Retrofit provideLoqateRetrofitInstance() {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_LOQATE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    @Named("Storage")
    static Retrofit provideStorageRetrofitInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(new CurlInterceptor(new Loggable() {
                    @Override
                    public void log(String message) {
                        Log.v("Ok2CurlStorage", message);
                    }
                })).build();
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_STORAGE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    @Named("Auth")
    static Retrofit provideAuthRetrofitInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder();

                requestBuilder.method(original.method(), original.body());

                Request request = requestBuilder.build();

                Response response = chain.proceed(request);

                return response;

            }
        })
                .addInterceptor(new CurlInterceptor(new Loggable() {
                    @Override
                    public void log(String message) {
                        Log.v("Ok2Curl", message);
                    }
                }))
                .build();
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_AUTH_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions() {
        return RequestOptions
                .placeholderOf(R.drawable.ic_profile)
                .error(R.drawable.error_placeholder);
    }


    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions) {
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    static Drawable provideAppDrawable(Application application) {
        return ContextCompat.getDrawable(application, R.drawable.ic_logo);
    }

    @Singleton
    @Provides
    @Named("app_user")
    static User someUser() {
        return new User();
    }

    //    @Singleton
//    @Provides
//    static SharedPreferences provideSharedPreferences(Application application) {
//        return application.getSharedPreferences("yas-inv", Context.MODE_PRIVATE);
//    }
    @Singleton
    @Provides
    static SharedPrefrencesHelper provideSharedPreferences(Application application) {

        return SharedPrefrencesHelper.getInstance(application);
    }
}
















