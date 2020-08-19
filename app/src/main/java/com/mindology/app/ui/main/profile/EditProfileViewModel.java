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
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.SharedPrefrencesHelper;
import com.mindology.app.util.Utils;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class EditProfileViewModel extends ViewModel {

    private static final String TAG = "EditProfileViewModel";
    private final SessionManager sessionManager;
    private final MainApi mainApi;
    private final RequestManager requestManager;
    private MediatorLiveData<Resource<ClientUserDTO>> profileLiveData;


    @Inject
    public EditProfileViewModel(MainApi mainApi, SessionManager sessionManager, RequestManager requestManager) {
        Log.d(TAG, "PropertiesViewModel: viewmodel is working...");
        this.sessionManager = sessionManager;
        this.requestManager = requestManager;
        this.mainApi = mainApi;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }



}



















