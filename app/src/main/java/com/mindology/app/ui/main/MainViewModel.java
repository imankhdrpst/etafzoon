package com.mindology.app.ui.main;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.ErrorResponse;
import com.mindology.app.models.Notification;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.ui.auth.AuthResource;
import com.mindology.app.util.SharedPrefrencesHelper;
import com.mindology.app.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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
    private MediatorLiveData<Resource<List<Notification>>> notificationsLiveDate;


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

                                    myProfile = response;
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

        return profileLiveData;

    }

    public void saveProfile(ClientUserDTO user) {
        profileLiveData.setValue(Resource.loading((ClientUserDTO) null));

        final LiveData<Resource<ClientUserDTO>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.editProfile(user)

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

        profileLiveData.addSource(source, new Observer<Resource<ClientUserDTO>>() {
            @Override
            public void onChanged(Resource<ClientUserDTO> listResource) {
                profileLiveData.setValue(listResource);
                profileLiveData.removeSource(source);
            }
        });
    }


    public String getToken() {
        try {
            return sessionManager.getSavedToken();
        } catch (SessionManager.TokenNotSaved tokenNotSaved) {
            return "";
        }
    }

    public ClientUserDTO getMyProfile() {
        return myProfile;
    }

    public LiveData<Resource<List<Notification>>> observeNotifications() {

        if (notificationsLiveDate == null) {
            notificationsLiveDate = new MediatorLiveData<>();
        }
        notificationsLiveDate.setValue(Resource.loading(null));
//        final LiveData<Resource<ClientUserDTO>> source = LiveDataReactiveStreams.fromPublisher(
//
//                mainApi.getProfile(mobileNumber)
//                        .onErrorReturn(new Function<Throwable, ClientUserDTO>() {
//                            @Override
//                            public ClientUserDTO apply(Throwable throwable) throws Exception {
//                                ClientUserDTO res = new ClientUserDTO();
//                                res.setMessage(Utils.fetchError(throwable).getMessage());
//                                return res;
//                            }
//                        })
//
//                        .map(new Function<ClientUserDTO, Resource<ClientUserDTO>>() {
//                            @Override
//                            public Resource<ClientUserDTO> apply(ClientUserDTO response) throws Exception {
//
//                                if (response == null || !TextUtils.isEmpty(response.getMessage()))
//                                    return Resource.error(response.getMessage(), response);
//
//                                myProfile = response;
//                                return Resource.success(response);
//                            }
//                        })
//
//                        .subscribeOn(Schedulers.io())
//        );

//        notificationsLiveDate.addSource(source, new Observer<Resource<ClientUserDTO>>() {
//            @Override
//            public void onChanged(Resource<ClientUserDTO> listResource) {
//                notificationsLiveDate.setValue(listResource);
//                notificationsLiveDate.removeSource(source);
//            }
//        });

        List<Notification> res = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Notification notif = new Notification();
            notif.setCreatedDate(Calendar.getInstance().getTime());
            notif.setContent("پیام شماره " + i);
            notif.setRead(i % 3 == 1);

            res.add(notif);
        }

        Collections.sort(res);

        notificationsLiveDate.setValue(Resource.success(res));

        return notificationsLiveDate;
    }


}









