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

    public LiveData<Resource<ClientUserDTO>> observerGetProfile() {
        if (getProfileLiveData == null) {
            getProfileLiveData = new MediatorLiveData<>();
        }
        getProfileLiveData.setValue(Resource.loading((ClientUserDTO) null));

        final LiveData<Resource<ClientUserDTO>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.getProfile(SharedPrefrencesHelper.getSavedMobileNumber())

                        .onErrorReturn(new Function<Throwable, ClientUserDTO>() {
                            @Override
                            public ClientUserDTO apply(Throwable throwable) throws Exception {
                                ErrorResponse errorResponse = Utils.fetchError(throwable);
                                ClientUserDTO result = new ClientUserDTO();
                                result.setMessage(errorResponse.getMessage());
                                return result;
                            }
                        })

                        .map(new Function<ClientUserDTO, Resource<ClientUserDTO>>() {
                            @Override
                            public Resource<ClientUserDTO> apply(ClientUserDTO result) throws Exception {

                                if (result == null) {
                                    return Resource.error("خطا در بازیابی اطلاعات", null);
                                } else if (!TextUtils.isEmpty(result.getMessage())) {
                                    return Resource.error(result.getMessage(), null);
                                }
                                return Resource.success(result);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );

        getProfileLiveData.addSource(source, new Observer<Resource<ClientUserDTO>>() {
            @Override
            public void onChanged(Resource<ClientUserDTO> listResource) {
                getProfileLiveData.setValue(listResource);
                getProfileLiveData.removeSource(source);
            }
        });
        return getProfileLiveData;
    }


}



















