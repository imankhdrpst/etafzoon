package com.mindology.app.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.Devices;
import com.mindology.app.models.ErrorResponse;
import com.mindology.app.models.User;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.ui.auth.AuthResource;
import com.mindology.app.util.Utils;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    // inject
    private final SessionManager sessionManager; // @Singleton scoped dependency
    private final MainApi mainApi; // @ForgetPasswordScope scoped dependency

    private MediatorLiveData<Resource<User>> getProfileLiveData;
    private MediatorLiveData<Resource<Object>> devicesLiveData;


    @Inject
    public MainViewModel(MainApi mainApi, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return sessionManager.getAuthUser();
    }

    public LiveData<Resource<User>> observerGetProfile() {
        if (getProfileLiveData == null) {
            getProfileLiveData = new MediatorLiveData<>();
        }
        getProfileLiveData.setValue(Resource.loading(null));
        final LiveData<Resource<User>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.getProfile()

                        .onErrorReturn(new Function<Throwable, User>() {
                            @Override
                            public User apply(Throwable throwable) throws Exception {
                                ErrorResponse errorResponse = Utils.fetchError(throwable);

                                User result = new User();
//                                result.setErrorResponse(errorResponse);
                                return result;
                            }
                        })

                        .map(new Function<User, Resource<User>>() {
                            @Override
                            public Resource<User> apply(User result) throws Exception {

//                                if (result == null) {
//                                    return Resource.error("Something went wrong", null);
//                                } else if (result.getErrorResponse() != null) {
//                                    return Resource.error(result.getErrorResponse().getMessage(), null);
//                                }
                                return Resource.success(result);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );

        getProfileLiveData.addSource(source, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> listResource) {
                getProfileLiveData.setValue(listResource);
                getProfileLiveData.removeSource(source);
            }
        });
        return getProfileLiveData;
    }

    public LiveData<Resource<Object>> observerDevices(Devices devices) {
        if (devicesLiveData == null) {
            devicesLiveData = new MediatorLiveData<>();
        }
        devicesLiveData.setValue(Resource.loading(null));


        final LiveData<Resource<Object>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.devices(devices)

                        .onErrorReturn(new Function<Throwable, User>() {
                            @Override
                            public User apply(Throwable throwable) throws Exception {
                                ErrorResponse errorResponse = Utils.fetchError(throwable);

                                User result = new User();
//                                result.setErrorResponse(errorResponse);
                                return result;
                            }
                        })

                        .map(new Function<Object, Resource<Object>>() {
                            @Override
                            public Resource<Object> apply(Object result) throws Exception {

                                if (result == null) {
                                    return Resource.error("Something went wrong", null);
                                }
                                return Resource.success(result);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );

        devicesLiveData.addSource(source, new Observer<Resource<Object>>() {
            @Override
            public void onChanged(Resource<Object> listResource) {
                devicesLiveData.setValue(listResource);
                devicesLiveData.removeSource(source);
            }
        });
        return devicesLiveData;
    }


    public String getToken() {
        try {
            return sessionManager.getSavedToken();
        } catch (SessionManager.TokenNotSaved tokenNotSaved) {
            return "";
        }
    }
}









