package com.mindology.app.ui.main;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.Devices;
import com.mindology.app.models.ErrorResponse;
import com.mindology.app.models.Profile;
import com.mindology.app.models.User;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.ui.auth.AuthResource;
import com.mindology.app.util.SharedPrefrencesHelper;
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

    private MediatorLiveData<Resource<Profile>> profileLiveData;
    private Profile myProfile;


    @Inject
    public MainViewModel(MainApi mainApi, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return sessionManager.getAuthUser();
    }

    public LiveData<Resource<Profile>> queryMyProfile() {

        if (profileLiveData == null) {
            profileLiveData = new MediatorLiveData<>();
        }
        if (myProfile != null) {
            profileLiveData.setValue(Resource.success(myProfile));
        } else {
            String mobileNumber = SharedPrefrencesHelper.getSavedMobileNumber();
            profileLiveData.setValue(Resource.loading(null));
            final LiveData<Resource<Profile>> source = LiveDataReactiveStreams.fromPublisher(

                    mainApi.getProfile(mobileNumber)
                            .onErrorReturn(new Function<Throwable, Profile>() {
                                @Override
                                public Profile apply(Throwable throwable) throws Exception {
                                    Profile res = new Profile();
                                    res.setMessage(Utils.fetchError(throwable).getMessage());
                                    return res;
                                }
                            })

                            .map(new Function<Profile, Resource<Profile>>() {
                                @Override
                                public Resource<Profile> apply(Profile response) throws Exception {

                                    if (response == null || !TextUtils.isEmpty(response.getMessage()))
                                        return Resource.error(response.getMessage(), response);
                                    else return Resource.success(response);
                                }
                            })

                            .subscribeOn(Schedulers.io())
            );

            profileLiveData.addSource(source, new Observer<Resource<Profile>>() {
                @Override
                public void onChanged(Resource<Profile> listResource) {
                    profileLiveData.setValue(listResource);
                    profileLiveData.removeSource(source);
                }
            });
        }

        return  profileLiveData;

    }


    public String getToken() {
        try {
            return sessionManager.getSavedToken();
        } catch (SessionManager.TokenNotSaved tokenNotSaved) {
            return "";
        }
    }
}









