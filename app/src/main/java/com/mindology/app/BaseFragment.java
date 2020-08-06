package com.mindology.app;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.mindology.app.ui.main.MainActivity;
import com.mindology.app.ui.main.MainViewModel;
import com.mindology.app.ui.main.profile.EditProfileViewModel;
import com.mindology.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    @Inject
    public ViewModelProviderFactory providerFactory;

    protected MainViewModel mainViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mainViewModel = ViewModelProviders.of(this, providerFactory).get(MainViewModel.class);
        mainViewModel = ((MainActivity)getActivity()).viewModel;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
