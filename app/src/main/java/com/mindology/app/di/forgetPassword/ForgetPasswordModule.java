package com.mindology.app.di.forgetPassword;

import com.mindology.app.network.auth.AuthApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ForgetPasswordModule {


    @ForgetPasswordScope
    @Provides
    static AuthApi provideAuthApi(@Named("Auth") Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }
}
