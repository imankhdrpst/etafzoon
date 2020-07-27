package com.mindology.app.di.contactUs;

import androidx.lifecycle.ViewModel;

import com.mindology.app.di.ViewModelKey;
import com.mindology.app.ui.auth.ContactUsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ContactUsViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsViewModel.class)
    public abstract ViewModel bindContactUsViewModel(ContactUsViewModel viewModel);
}
