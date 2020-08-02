package com.mindology.app.di.main;

import androidx.lifecycle.ViewModel;

import com.mindology.app.di.ViewModelKey;
import com.mindology.app.ui.main.MainViewModel;
import com.mindology.app.ui.main.inspections.InspectionDetailsViewModel;
import com.mindology.app.ui.main.main.MainPageViewModel;
import com.mindology.app.ui.main.posts.PostDetailViewModel;
import com.mindology.app.ui.main.posts.PostsViewModel;
import com.mindology.app.ui.main.profile.ChangePasswordViewModel;
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
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(MainInspectionsViewModel.class)
//    public abstract ViewModel bindInspectionsViewModel(MainInspectionsViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(CreateInspectionViewModel.class)
//    public abstract ViewModel bindCreateInspectionViewModel(CreateInspectionViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    public abstract ViewModel bindProfileViewModel(ProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel.class)
    public abstract ViewModel bindChangePasswordViewModel(ChangePasswordViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel.class)
    public abstract ViewModel bindEditProfileViewModel(EditProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(InspectionDetailsViewModel.class)
    public abstract ViewModel bindInspectionDetailsViewModel(InspectionDetailsViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(AssetsViewModel.class)
//    public abstract ViewModel bindAssetsViewModel(AssetsViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(CreateAreaViewModel.class)
//    public abstract ViewModel bindCreateAreaViewModel(CreateAreaViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(CreateMeterViewModel.class)
//    public abstract ViewModel bindCreateMeterViewModel(CreateMeterViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(CreateAlarmViewModel.class)
//    public abstract ViewModel bindCreateAlarmViewModel(CreateAlarmViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(AreaDetailsViewModel.class)
//    public abstract ViewModel bindAreaDetailsViewModel(AreaDetailsViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(MeterDetailsViewModel.class)
//    public abstract ViewModel bindMeterDetailsViewModel(MeterDetailsViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(AlarmDetailsViewModel.class)
//    public abstract ViewModel bindAlarmDetailsViewModel(AlarmDetailsViewModel viewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(DeleteAssetsViewModel.class)
//    public abstract ViewModel bindDeleteAssetsViewModel(DeleteAssetsViewModel viewModel);
}




