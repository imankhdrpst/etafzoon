package com.mindology.app.ui.main.main;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.Post;
import com.mindology.app.models.PostGroup;
import com.mindology.app.models.Profile;
import com.mindology.app.models.User;
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

public class MainPageViewModel extends ViewModel {

    private MediatorLiveData<Resource<List<PostGroup>>> data;
    private MediatorLiveData<Resource<List<Post>>> postsLiveData;
    private MediatorLiveData<Resource<Profile>> profileLiveData;

    private List<PostGroup> postGroupList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();
    private Profile myProfile;

    // inject
    private final MainApi mainApi;
    private final SessionManager sessionManager;
    private final SharedPrefrencesHelper sharedPrefs;

    @Inject
    public MainPageViewModel(MainApi mainApi, SessionManager sessionManager, SharedPrefrencesHelper helper) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        this.sharedPrefs = helper;
    }

    public LiveData<AuthResource<User>> getAuthenticatedUser() {
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

    public LiveData<Resource<List<Post>>> queryLatestPosts() {
        if (postsLiveData == null) {
            postsLiveData = new MediatorLiveData<>();
        }
        postsLiveData.setValue(Resource.loading(postList));

        final LiveData<Resource<List<Post>>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.getPosts()
                        .onErrorReturn(new Function<Throwable, List<Post>>() {
                            @Override
                            public List<Post> apply(Throwable throwable) throws Exception {
                                List<Post> res = new ArrayList<>();
                                Post error = new Post();
                                error.setMessage(Utils.fetchError(throwable).getMessage());
                                res.add(error);
                                return res;
                            }
                        })

                        .map(new Function<List<Post>, Resource<List<Post>>>() {
                            @Override
                            public Resource<List<Post>> apply(List<Post> response) throws Exception {

                                if (response == null || (response.size() == 1 && !TextUtils.isEmpty(response.get(0).getMessage())))
                                    return Resource.error(response.get(0).getMessage(), response);
                                else return Resource.success(response);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );

        postsLiveData.addSource(source, new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(Resource<List<Post>> listResource) {
                postsLiveData.setValue(listResource);
                postsLiveData.removeSource(source);
            }
        });

        return postsLiveData;

    }

    public LiveData<Resource<List<PostGroup>>> queryPostGroups() {
        if (data == null) {
            data = new MediatorLiveData<>();
        }
        data.setValue(Resource.loading(postGroupList));

        final LiveData<Resource<List<PostGroup>>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.getPostGroups()
                        .onErrorReturn(new Function<Throwable, List<PostGroup>>() {
                            @Override
                            public List<PostGroup> apply(Throwable throwable) throws Exception {
                                List<PostGroup> res = new ArrayList<>();
                                PostGroup error = new PostGroup();
                                error.setMessage(Utils.fetchError(throwable).getMessage());
                                res.add(error);
                                return res;
                            }
                        })

                        .map(new Function<List<PostGroup>, Resource<List<PostGroup>>>() {
                            @Override
                            public Resource<List<PostGroup>> apply(List<PostGroup> response) throws Exception {

                                if (response == null || (response.size() == 1 && !TextUtils.isEmpty(response.get(0).getMessage())))
                                    return Resource.error(response.get(0).getMessage(), response);
                                else return Resource.success(response);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );

        data.addSource(source, new Observer<Resource<List<PostGroup>>>() {
            @Override
            public void onChanged(Resource<List<PostGroup>> listResource) {
                data.setValue(listResource);
                data.removeSource(source);
            }
        });

        return data;

    }

}



















