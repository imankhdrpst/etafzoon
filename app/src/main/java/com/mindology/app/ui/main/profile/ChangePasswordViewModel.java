package com.mindology.app.ui.main.profile;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.mindology.app.network.main.MainApi;

import javax.inject.Inject;

public class ChangePasswordViewModel extends ViewModel {

    private static final String TAG = "ChangePassword";

    @Inject
    public ChangePasswordViewModel(MainApi mainApi) {
        Log.d(TAG, "PropertiesViewModel: viewmodel is working...");
    }


}



















