package com.mindology.app.di.main;

import androidx.lifecycle.ViewModel;

import com.mindology.app.di.ViewModelKey;
import com.mindology.app.ui.main.MainViewModel;
import com.mindology.app.ui.main.main.MainPageViewModel;
import com.mindology.app.ui.main.mood.MoodListViewModel;
import com.mindology.app.ui.main.notifications.NotificationsViewModel;
import com.mindology.app.ui.main.posts.PostDetailViewModel;
import com.mindology.app.ui.main.posts.PostsViewModel;
import com.mindology.app.ui.main.profile.EditProfileViewModel;
import com.mindology.app.ui.main.profile.ProfileViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel.class)
    public abstract ViewModel bindPostsViewModel(PostsViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(MainPageViewModel.class)
    public abstract ViewModel bindMainPageViewModel(MainPageViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(PostDetailViewModel.class)
    public abstract ViewModel bindPostDetailViewModel(PostDetailViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(MoodListViewModel.class)
    public abstract ViewModel bindMoodListViewModel(MoodListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    public abstract ViewModel bindProfileViewModel(ProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel.class)
    public abstract ViewModel bindEditProfileViewModel(EditProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel.class)
    public abstract ViewModel bindNotificationsViewModel(NotificationsViewModel viewModel);
}




