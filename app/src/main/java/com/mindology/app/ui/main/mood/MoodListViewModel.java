package com.mindology.app.ui.main.mood;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.Post;
import com.mindology.app.models.PostGroup;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.ui.auth.AuthResource;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.SharedPrefrencesHelper;
import com.mindology.app.util.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MoodListViewModel extends ViewModel {

    private MediatorLiveData<Resource<List<PostGroup>>> data;
    private MediatorLiveData<Resource<List<Post>>> postsLiveData;


    private List<PostGroup> postGroupList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();

    // inject
    private final MainApi mainApi;
    private final SessionManager sessionManager;
    private final SharedPrefrencesHelper sharedPrefs;

    @Inject
    public MoodListViewModel(MainApi mainApi, SessionManager sessionManager, SharedPrefrencesHelper helper) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        this.sharedPrefs = helper;
    }

    public LiveData<AuthResource<ClientUserDTO>> getAuthenticatedUser() {
        return sessionManager.getAuthUser();
    }




}



















