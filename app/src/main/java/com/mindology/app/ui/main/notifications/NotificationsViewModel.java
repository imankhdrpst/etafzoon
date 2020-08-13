package com.mindology.app.ui.main.notifications;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.Notification;
import com.mindology.app.models.Post;
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

public class NotificationsViewModel extends ViewModel {

//    private MediatorLiveData<Resource<List<Post>>> postsLiveData;

//    private List<Post> postList = new ArrayList<>();

    // inject
    private final MainApi mainApi;
    private final SessionManager sessionManager;
    private final SharedPrefrencesHelper sharedPrefs;

    @Inject
    public NotificationsViewModel(MainApi mainApi, SessionManager sessionManager, SharedPrefrencesHelper helper) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        this.sharedPrefs = helper;
    }

    public LiveData<AuthResource<ClientUserDTO>> getAuthenticatedUser() {
        return sessionManager.getAuthUser();
    }



//    public LiveData<Resource<List<Post>>> queryLatestPosts() {
//        if (postsLiveData == null) {
//            postsLiveData = new MediatorLiveData<>();
//        }
//        postsLiveData.setValue(Resource.loading(postList));
//
//        final LiveData<Resource<List<Post>>> source = LiveDataReactiveStreams.fromPublisher(
//
//                mainApi.getPosts()
//                        .onErrorReturn(new Function<Throwable, List<Post>>() {
//                            @Override
//                            public List<Post> apply(Throwable throwable) throws Exception {
//                                List<Post> res = new ArrayList<>();
//                                Post error = new Post();
//                                error.setMessage(Utils.fetchError(throwable).getMessage());
//                                res.add(error);
//                                return res;
//                            }
//                        })
//
//                        .map(new Function<List<Post>, Resource<List<Post>>>() {
//                            @Override
//                            public Resource<List<Post>> apply(List<Post> response) throws Exception {
//
//                                if (response == null || (response.size() == 1 && !TextUtils.isEmpty(response.get(0).getMessage())))
//                                    return Resource.error(response.get(0).getMessage(), response);
//                                else return Resource.success(response);
//                            }
//                        })
//
//                        .subscribeOn(Schedulers.io())
//        );
//
//        postsLiveData.addSource(source, new Observer<Resource<List<Post>>>() {
//            @Override
//            public void onChanged(Resource<List<Post>> listResource) {
//                postsLiveData.setValue(listResource);
//                postsLiveData.removeSource(source);
//            }
//        });
//
//        return postsLiveData;
//
//    }

}



















