package com.mindology.app.di.contactUs;

import com.mindology.app.network.main.MainApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ContactUsModule {

    @Provides
    static MainApi provideContactMainApi(@Named("ContactUs") Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }
}
