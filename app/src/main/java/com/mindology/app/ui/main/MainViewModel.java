package com.mindology.app.ui.main;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.repo.TempDataHolder;
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

    private MediatorLiveData<Resource<ClientUserDTO>> getProfileLiveData;
    private MediatorLiveData<Resource<Object>> devicesLiveData;

    private MediatorLiveData<Resource<ClientUserDTO>> profileLiveData;
    private ClientUserDTO myProfile;


    @Inject
    public MainViewModel(MainApi mainApi, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
    }

    public LiveData<AuthResource<ClientUserDTO>> observeAuthState() {
        return sessionManager.getAuthUser();
    }

    public LiveData<Resource<ClientUserDTO>> queryMyProfile() {

        if (profileLiveData == null) {
            profileLiveData = new MediatorLiveData<>();
        }
        if (myProfile != null) {
            profileLiveData.setValue(Resource.success(myProfile));
        } else {
            String mobileNumber = SharedPrefrencesHelper.getSavedMobileNumber();
            profileLiveData.setValue(Resource.loading(null));
            final LiveData<Resource<ClientUserDTO>> source = LiveDataReactiveStreams.fromPublisher(

                    mainApi.getProfile(mobileNumber)
                            .onErrorReturn(new Function<Throwable, ClientUserDTO>() {
                                @Override
                                public ClientUserDTO apply(Throwable throwable) throws Exception {
                                    ClientUserDTO res = new ClientUserDTO();
                                    res.setMessage(Utils.fetchError(throwable).getMessage());
                                    return res;
                                }
                            })

                            .map(new Function<ClientUserDTO, Resource<ClientUserDTO>>() {
                                @Override
                                public Resource<ClientUserDTO> apply(ClientUserDTO response) throws Exception {

                                    if (response == null || !TextUtils.isEmpty(response.getMessage()))
                                        return Resource.error(response.getMessage(), response);

                                    TempDataHolder.setCurrentUser(response);
                                    return Resource.success(response);
                                }
                            })

                            .subscribeOn(Schedulers.io())
            );

            profileLiveData.addSource(source, new Observer<Resource<ClientUserDTO>>() {
                @Override
                public void onChanged(Resource<ClientUserDTO> listResource) {
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









