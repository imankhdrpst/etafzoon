package com.mindology.app.di.main;


import com.mindology.app.ui.main.main.MainFragment;
import com.mindology.app.ui.main.mood.MoodListFragment;
import com.mindology.app.ui.main.posts.PostDetailFragment;
import com.mindology.app.ui.main.posts.PostsFragment;
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

    @ContributesAndroidInjector
    abstract PostDetailFragment contributePostDetailFragment();

    @ContributesAndroidInjector
    abstract MoodListFragment contributeMoodListFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract EditProfileFragment contributeEditProfileFragment();
}
