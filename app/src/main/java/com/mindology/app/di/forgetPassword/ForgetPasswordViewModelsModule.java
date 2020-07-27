package com.mindology.app.di.forgetPassword;

import androidx.lifecycle.ViewModel;

import com.mindology.app.di.ViewModelKey;
import com.mindology.app.ui.auth.ForgetPasswordViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ForgetPasswordViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ForgetPasswordViewModel.class)
    public abstract ViewModel bindForgetPassViewModel(ForgetPasswordViewModel viewModel);
}
