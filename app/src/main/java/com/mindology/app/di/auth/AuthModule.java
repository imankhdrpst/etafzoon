package com.mindology.app.di.auth;

import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.network.auth.AuthApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AuthModule {

    @AuthScope
    @Provides
    @Named("auth_user")
    static ClientUserDTO someUser(){
        return new ClientUserDTO();
    }

    @AuthScope
    @Provides
    static AuthApi provideAuthApi(@Named("Auth") Retrofit retrofit){
        return retrofit.create(AuthApi.class);
    }
}
