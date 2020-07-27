package com.mindology.app.di.main;

import com.mindology.app.network.loqate.LoqateApi;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.network.storage.StorageApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

//    @MainScope
//    @Provides
//    static PropertiesRecyclerAdapter provideAdapter(){
//        return new PropertiesRecyclerAdapter();
//    }

    @MainScope
    @Provides
    static MainApi provideMainApi(Retrofit retrofit){
        return retrofit.create(MainApi.class);
    }

    @MainScope
    @Provides
    static LoqateApi provideLoqateApi(@Named("Loqate") Retrofit retrofit){
        return retrofit.create(LoqateApi.class);
    }

    @MainScope
    @Provides
    static StorageApi provideStorageApi(@Named("Storage") Retrofit retrofit){
        return retrofit.create(StorageApi.class);
    }

}
