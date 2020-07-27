package com.mindology.app.di;

import com.mindology.app.di.auth.AuthModule;
import com.mindology.app.di.auth.AuthScope;
import com.mindology.app.di.auth.AuthViewModelsModule;
import com.mindology.app.di.contactUs.ContactUsModule;
import com.mindology.app.di.contactUs.ContactUsViewModelsModule;
import com.mindology.app.di.forgetPassword.ForgetPasswordModule;
import com.mindology.app.di.forgetPassword.ForgetPasswordScope;
import com.mindology.app.di.forgetPassword.ForgetPasswordViewModelsModule;
import com.mindology.app.di.main.MainFragmentBuildersModule;
import com.mindology.app.di.main.MainModule;
import com.mindology.app.di.main.MainScope;
import com.mindology.app.di.main.MainViewModelsModule;
import com.mindology.app.ui.auth.AuthActivity;
import com.mindology.app.ui.auth.ContactUsActivity;
import com.mindology.app.ui.auth.ForgetPasswordActivity;
import com.mindology.app.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
            modules = {AuthViewModelsModule.class, AuthModule.class})
    abstract AuthActivity contributeAuthActivity();

    @ForgetPasswordScope
    @ContributesAndroidInjector(
            modules = {ForgetPasswordViewModelsModule.class, ForgetPasswordModule.class})
    abstract ForgetPasswordActivity contributeForgetPasswordActivity();

    @ContributesAndroidInjector(
            modules = {ContactUsViewModelsModule.class, ContactUsModule.class})
    abstract ContactUsActivity contributeContactUsActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {MainFragmentBuildersModule.class, MainViewModelsModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();

//    @RicohScoe
//    @ContributesAndroidInjector(
//            modules = {RicohModule.class, RicohViewModelsModule.class}
//    )
//    abstract ImageListActivity contributeImageListActivity();
//
//
//    @RicohScoe
//    @ContributesAndroidInjector(
//            modules = {RicohModule.class, RicohViewModelsModule.class}
//    )
//    abstract GLPhotoActivity contributeGLPhotoActivity();


//    @RicohScoe
//    @ContributesAndroidInjector(
//            modules = {RicohModule.class, RicohViewModelsModule.class}
//    )
//    abstract HotSpotsActivity contributeHotSpotsActivity();
//
//    @RicohScoe
//    @ContributesAndroidInjector(
//            modules = {RicohModule.class, RicohViewModelsModule.class}
//    )
//    abstract GalleryPanoramaActivity contributeGalleryPanoramaActivity();

}
