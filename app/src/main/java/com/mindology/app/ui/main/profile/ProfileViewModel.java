package com.mindology.app.ui.main.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.RequestManager;
import com.mindology.app.SessionManager;
import com.mindology.app.models.ErrorResponse;
import com.mindology.app.models.User;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.Utils;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {

    private static final String TAG = "ProfileViewModel";
    private final SessionManager sessionManager;
    private final MainApi mainApi;
    public final RequestManager requestManager;
    private MediatorLiveData<Resource<User>> getProfileLiveData;

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

    public LiveData<Resource<User>> observerGetProfile() {
        if (getProfileLiveData == null) {
            getProfileLiveData = new MediatorLiveData<>();
        }
        getProfileLiveData.setValue(Resource.loading((User) null));
//        final LiveData<Resource<User>> source = LiveDataReactiveStreams.fromPublisher(
//
//                mainApi.getProfile()
//
//                        .onErrorReturn(new Function<Throwable, User>() {
//                            @Override
//                            public User apply(Throwable throwable) throws Exception {
//                                ErrorResponse errorResponse = Utils.fetchError(throwable);
//
//                                User result = new User();
////                                result.setErrorResponse(errorResponse);
//                                return result;
//                            }
//                        })
//
//                        .map(new Function<User, Resource<User>>() {
//                            @Override
//                            public Resource<User> apply(User result) throws Exception {
//
////                                if (result == null) {
////                                    return Resource.error("Something went wrong", null);
////                                } else if (result.getErrorResponse() != null) {
////                                    return Resource.error(result.getErrorResponse().getMessage(), null);
////                                }
//                                return Resource.success(result);
//                            }
//                        })
//
//                        .subscribeOn(Schedulers.io())
//        );
//
//        getProfileLiveData.addSource(source, new Observer<Resource<User>>() {
//            @Override
//            public void onChanged(Resource<User> listResource) {
//                getProfileLiveData.setValue(listResource);
//                getProfileLiveData.removeSource(source);
//            }
//        });
        return getProfileLiveData;
    }


}


















