package com.mindology.app.di.auth;

import androidx.lifecycle.ViewModel;

import com.mindology.app.di.ViewModelKey;
import com.mindology.app.ui.auth.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindAuthViewModel(AuthViewModel viewModel);


}
