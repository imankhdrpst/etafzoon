package com.mindology.app.ui.main.mood;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.MoodDTO;
import com.mindology.app.models.MoodStatisticsDTO;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.ui.auth.AuthResource;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.SharedPrefrencesHelper;
import com.mindology.app.util.Utils;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

public class MoodListViewModel extends ViewModel {

    private MediatorLiveData<Resource<MoodStatisticsDTO>> moodListLiveData;
    private MediatorLiveData<Resource<ResponseBody>> addMoodLiveData;

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

    public LiveData<Resource<MoodStatisticsDTO>> observeMoodList(String mobile) {
        if (moodListLiveData == null) {
            moodListLiveData = new MediatorLiveData<>();

            moodListLiveData.setValue(Resource.loading(null));

            final LiveData<Resource<MoodStatisticsDTO>> source = LiveDataReactiveStreams.fromPublisher(

                    mainApi.getMoodList(mobile, 1)
                            .onErrorReturn(new Function<Throwable, MoodStatisticsDTO>() {
                                @Override
                                public MoodStatisticsDTO apply(Throwable throwable) throws Exception {
                                    MoodStatisticsDTO error = new MoodStatisticsDTO();
                                    error.setMessage(Utils.fetchError(throwable).getMessage());
                                    return error;
                                }
                            })

                            .map(new Function<MoodStatisticsDTO, Resource<MoodStatisticsDTO>>() {
                                @Override
                                public Resource<MoodStatisticsDTO> apply(MoodStatisticsDTO response) throws Exception {

                                    if (response == null || !TextUtils.isEmpty(response.getMessage()))
                                        return Resource.error(response.getMessage(), response);
                                    else return Resource.success(response);
                                }
                            })

                            .subscribeOn(Schedulers.io())
            );

            moodListLiveData.addSource(source, new Observer<Resource<MoodStatisticsDTO>>() {
                @Override
                public void onChanged(Resource<MoodStatisticsDTO> listResource) {
                    moodListLiveData.setValue(listResource);
                    moodListLiveData.removeSource(source);
                }
            });

        }

        return moodListLiveData;
    }

    public void getMoodsOfPeriod(String mobile, int period) {
        moodListLiveData.setValue(Resource.loading(null));

        final LiveData<Resource<MoodStatisticsDTO>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.getMoodList(mobile, period)
                        .onErrorReturn(new Function<Throwable, MoodStatisticsDTO>() {
                            @Override
                            public MoodStatisticsDTO apply(Throwable throwable) throws Exception {
                                MoodStatisticsDTO error = new MoodStatisticsDTO();
                                error.setMessage(Utils.fetchError(throwable).getMessage());
                                return error;
                            }
                        })

                        .map(new Function<MoodStatisticsDTO, Resource<MoodStatisticsDTO>>() {
                            @Override
                            public Resource<MoodStatisticsDTO> apply(MoodStatisticsDTO response) throws Exception {

                                if (response == null || !TextUtils.isEmpty(response.getMessage()))
                                    return Resource.error(response.getMessage(), response);
                                else return Resource.success(response);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );

        moodListLiveData.addSource(source, new Observer<Resource<MoodStatisticsDTO>>() {
            @Override
            public void onChanged(Resource<MoodStatisticsDTO> listResource) {
                moodListLiveData.setValue(listResource);
                moodListLiveData.removeSource(source);
            }
        });

    }

    public LiveData<Resource<ResponseBody>> observeAddMood() {
        addMoodLiveData = new MediatorLiveData<>();

        return addMoodLiveData;
    }


    public void addMood(MoodDTO moodDTO) {
        addMoodLiveData.setValue(Resource.loading(null));
        final LiveData<Resource<ResponseBody>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.addMood(moodDTO)
                        .onErrorReturn(new Function<Throwable, ResponseBody>() {
                            @Override
                            public ResponseBody apply(Throwable throwable) throws Exception {
                                MoodDTO error = new MoodDTO();
                                error.setMessage(Utils.fetchError(throwable).getMessage());
                                return ResponseBody.create("error", MediaType.get(""));
                            }
                        })

                        .map(new Function<ResponseBody, Resource<ResponseBody>>() {
                            @Override
                            public Resource<ResponseBody> apply(ResponseBody response) throws Exception {

                                if (response instanceof ResponseBody && !TextUtils.isEmpty(((ResponseBody) response).string()))
                                    return Resource.error("خطا در دریافت اطلاعات", response);
                                else
                                    return Resource.success(response);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );

        addMoodLiveData.addSource(source, new Observer<Resource<ResponseBody>>() {
            @Override
            public void onChanged(Resource<ResponseBody> listResource) {
                addMoodLiveData.setValue(listResource);
                addMoodLiveData.removeSource(source);
                if (listResource.status == Resource.Status.SUCCESS || listResource.status == Resource.Status.UPDATED) {
                    MoodStatisticsDTO dto = moodListLiveData.getValue().data;
                    dto.getMoods().add(moodDTO);
                    moodListLiveData.setValue(Resource.updated(dto));
                }
            }
        });

    }

}



















