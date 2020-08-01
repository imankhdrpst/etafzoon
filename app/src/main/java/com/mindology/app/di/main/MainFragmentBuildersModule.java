package com.mindology.app.di.main;


import com.mindology.app.ui.main.inspections.InspectionDetailsFragment;
import com.mindology.app.ui.main.main.MainFragment;
import com.mindology.app.ui.main.posts.PostsFragment;
import com.mindology.app.ui.main.profile.ChangePasswordFragment;
import com.mindology.app.ui.main.profile.EditProfileFragment;
import com.mindology.app.ui.main.profile.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract MainFragment contributeMainFragment();

    @ContributesAndroidInjector
    abstract PostsFragment contributePostsFragment();

//    @ContributesAndroidInjector
//    abstract MainInspectionsFragment contributeMainInspectionsFragment();

//    @ContributesAndroidInjector
//    abstract CreateInspectionFragment contributeCreateInspectionFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract ChangePasswordFragment contributeChangePasswordFragment();

    @ContributesAndroidInjector
    abstract EditProfileFragment contributeEditProfileFragment();

    @ContributesAndroidInjector
    abstract InspectionDetailsFragment contributeInspectionDetailsFragment();

//    @ContributesAndroidInjector
//    abstract AssetsFragment contributeAssetsFragment();
//
//    @ContributesAndroidInjector
//    abstract CreateAreaFragment contributeCreateAreaFragment();

//    @ContributesAndroidInjector
//    abstract CreateMeterFragment contributeCreateMeterFragment();
//
//    @ContributesAndroidInjector
//    abstract CreateAlarmFragment contributeCreateAlarmFragment();
//
//    @ContributesAndroidInjector
//    abstract AreaDetailsFragment contributeAreaDetailsFragment();
//
//    @ContributesAndroidInjector
//    abstract MeterDetailsFragment contributeMeterDetailsFragment();
//
//    @ContributesAndroidInjector
//    abstract AlarmDetailsFragment contributeAlarmDetailsFragment();
//
//    @ContributesAndroidInjector
//    abstract DeleteAssetsFragment contributeDeleteAssetsFragment();
}
