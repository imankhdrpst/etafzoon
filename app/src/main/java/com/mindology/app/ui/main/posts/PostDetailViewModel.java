package com.mindology.app.ui.main.posts;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.BookmarkPostDTO;
import com.mindology.app.models.HelpfulCreatePostDTO;
import com.mindology.app.models.HelpfulPostDTO;
import com.mindology.app.models.Post;
import com.mindology.app.models.User;
import com.mindology.app.network.main.MainApi;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.auth.AuthResource;
import com.mindology.app.ui.main.Resource;
import com.mindology.app.util.SharedPrefrencesHelper;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PostDetailViewModel extends ViewModel {

    private MediatorLiveData<Resource<Post>> postDetailsLiveData;
    private MediatorLiveData<Resource<HelpfulPostDTO>> helpfulLiveData;
    private MediatorLiveData<Resource<Object>> bookmarkLiveData;
    private Post post;


    // inject
    private final MainApi mainApi;
    private final SessionManager sessionManager;
    private final SharedPrefrencesHelper sharedPrefs;

    @Inject
    public PostDetailViewModel(MainApi mainApi, SessionManager sessionManager, SharedPrefrencesHelper helper) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        this.sharedPrefs = helper;
    }

    public LiveData<AuthResource<User>> getAuthenticatedUser() {
        return sessionManager.getAuthUser();
    }


    public LiveData<Resource<Post>> queryPostDetails() {
        if (postDetailsLiveData == null) {
            postDetailsLiveData = new MediatorLiveData<>();
        }
        post = TempDataHolder.getSelectedPost();
        if (post == null) {
            post = new Post();
        }
        postDetailsLiveData.setValue(Resource.loading(post));

        final LiveData<Resource<Post>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.getPostDetails(post.getId())
                        .onErrorReturn(new Function<Throwable, Post>() {
                            @Override
                            public Post apply(Throwable throwable) throws Exception {
                                Post res = new Post();
                                res.setMessage("خطا در بازیابی اطلاعات");
                                return res;
                            }
                        })

                        .map(new Function<Post, Resource<Post>>() {
                            @Override
                            public Resource<Post> apply(Post response) throws Exception {

                                if (response == null || !TextUtils.isEmpty(response.getMessage()))
                                    return Resource.error(response.getMessage(), response);
                                return Resource.success(response);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );

        postDetailsLiveData.addSource(source, new Observer<Resource<Post>>() {
            @Override
            public void onChanged(Resource<Post> listResource) {
                postDetailsLiveData.setValue(listResource);
                postDetailsLiveData.removeSource(source);
            }
        });

        return postDetailsLiveData;

    }

    public LiveData<Resource<HelpfulPostDTO>> observerHelpful()
    {
        if (helpfulLiveData == null) {
            helpfulLiveData = new MediatorLiveData<>();
        }
        return helpfulLiveData;
    }

    public LiveData<Resource<Object>> observerBookmark()
    {
        if (bookmarkLiveData == null) {
            bookmarkLiveData = new MediatorLiveData<>();
        }
        return bookmarkLiveData;
    }

    public void setBookmark(boolean add)
    {
        if (post == null) return;

        if (bookmarkLiveData == null)
        {
            bookmarkLiveData = new MediatorLiveData<>();
        }

        bookmarkLiveData.setValue(Resource.loading(null));

        BookmarkPostDTO dto = new BookmarkPostDTO();
        dto.setPostId(post.getId());
        dto.setUserMobile(TempDataHolder.getCurrentUser().getMobileNumber());

        final LiveData<Resource<Object>> source;

        if (add) {

            source = LiveDataReactiveStreams.fromPublisher(

                    mainApi.setBookmark(dto)
                            .onErrorReturn(new Function<Throwable, BookmarkPostDTO>() {
                                @Override
                                public BookmarkPostDTO apply(Throwable throwable) throws Exception {
                                    BookmarkPostDTO res = new BookmarkPostDTO();
                                    res.setMessage("خطا در بازیابی اطلاعات");
                                    return res;
                                }
                            })

                            .map(new Function<Object, Resource<Object>>() {
                                @Override
                                public Resource<Object> apply(Object response) throws Exception {

//                                    if (response == null || !TextUtils.isEmpty(response.getMessage()))
//                                        return Resource.error(response.getMessage(), response);

                                    post.setBookmarked(true);
                                    return Resource.success(response);
                                }
                            })

                            .subscribeOn(Schedulers.io())
            );
        }
        else
        {
            source = LiveDataReactiveStreams.fromPublisher(

                    mainApi.deleteBookmark(dto)
                            .onErrorReturn(new Function<Throwable, BookmarkPostDTO>() {
                                @Override
                                public BookmarkPostDTO apply(Throwable throwable) throws Exception {
                                    BookmarkPostDTO res = new BookmarkPostDTO();
                                    res.setMessage("خطا در بازیابی اطلاعات");
                                    return res;
                                }
                            })

                            .map(new Function<Object, Resource<Object>>() {
                                @Override
                                public Resource<Object> apply(Object response) throws Exception {

//                                    if (response == null || !TextUtils.isEmpty(response.getMessage()))
//                                        return Resource.error(response.getMessage(), response);

                                    post.setBookmarked(false);
                                    return Resource.success(response);
                                }
                            })

                            .subscribeOn(Schedulers.io())
            );
        }

        bookmarkLiveData.addSource(source, new Observer<Resource<Object>>() {
            @Override
            public void onChanged(Resource<Object> listResource) {
                bookmarkLiveData.setValue(listResource);
                bookmarkLiveData.removeSource(source);
                postDetailsLiveData.setValue(Resource.updated(post));
            }
        });
    }

    public void sharePost()
    {
        if (post == null) return;

        mainApi.sharePost(post.getId()).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                int a  =0;
                a++;
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                int a  =0;
                a++;

            }
        });
    }

    public void setHelpful(boolean helpful) {
        if (post == null) return;

        if (helpfulLiveData == null) {
            helpfulLiveData = new MediatorLiveData<>();
        }

        helpfulLiveData.setValue(Resource.loading(null));

        HelpfulCreatePostDTO dto = new HelpfulCreatePostDTO();
        dto.setClientUserId(TempDataHolder.getCurrentUser().getId());
        dto.setPostId(post.getId());
        dto.setHelpful(helpful);

        final LiveData<Resource<HelpfulPostDTO>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.setPostUseful(dto)
                        .onErrorReturn(new Function<Throwable, HelpfulPostDTO>() {
                            @Override
                            public HelpfulPostDTO apply(Throwable throwable) throws Exception {
                                HelpfulPostDTO res = new HelpfulPostDTO();
                                res.setMessage("خطا در بازیابی اطلاعات");
                                return res;
                            }
                        })

                        .map(new Function<HelpfulPostDTO, Resource<HelpfulPostDTO>>() {
                            @Override
                            public Resource<HelpfulPostDTO> apply(HelpfulPostDTO response) throws Exception {

                                if (response == null || !TextUtils.isEmpty(response.getMessage()))
                                    return Resource.error(response.getMessage(), response);

                                post.setHelpful(response.isHelpful() ? "1" : "0");
                                return Resource.success(response);
                            }
                        })

                        .subscribeOn(Schedulers.io())
        );

        helpfulLiveData.addSource(source, new Observer<Resource<HelpfulPostDTO>>() {
            @Override
            public void onChanged(Resource<HelpfulPostDTO> listResource) {
                helpfulLiveData.setValue(listResource);
                helpfulLiveData.removeSource(source);
                postDetailsLiveData.setValue(Resource.updated(post));
            }
        });
    }

}



















