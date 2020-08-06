package com.mindology.app.ui.main.profile;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.RequestManager;
import com.mindology.app.SessionManager;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.ErrorResponse;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.SharedPrefrencesHelper;
import com.mindology.app.util.Utils;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {

    private static final String TAG = "ProfileViewModel";
    private final SessionManager sessionManager;
    private final MainApi mainApi;
    public final RequestManager requestManager;
    private MediatorLiveData<Resource<ClientUserDTO>> getProfileLiveData;

    @Inject
    public ProfileViewModel(MainApi mainApi, SessionManager sessionManager, RequestManager requestManager) {
        Log.d(TAG, "PropertiesViewModel: viewmodel is working...");
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        this.requestManager = requestManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

}



















