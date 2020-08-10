package com.mindology.app.network.main;

import com.mindology.app.models.BookmarkPostDTO;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.HelpfulCreatePostDTO;
import com.mindology.app.models.HelpfulPostDTO;
import com.mindology.app.models.MoodDTO;
import com.mindology.app.models.MoodStatisticsDTO;
import com.mindology.app.models.Post;
import com.mindology.app.models.PostGroup;
import com.mindology.app.network.ServicePath;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MainApi {

    // post groups
    @GET(ServicePath.POST_GROUPS)
    Flowable<List<PostGroup>> getPostGroups();

    // posts
    @POST(ServicePath.POSTS + "?postIds=1")
    Flowable<List<Post>> getPosts();

    // post detials
    @GET(ServicePath.POST_DETAILS + "{postId}")
    Flowable<Post> getPostDetails(@Path("postId") int id);

    // post useful
    @POST(ServicePath.POST_USEFUL)
    Flowable<HelpfulPostDTO> setPostUseful(@Body HelpfulCreatePostDTO dto);

    // post bookmark
    @POST(ServicePath.POST_BOOKMARK)
    Flowable<Object> setBookmark(@Body BookmarkPostDTO dto);

    // post bookmark delete
    @POST(ServicePath.POST_BOOKMARK_DELETE)
    Flowable<Object> deleteBookmark(@Body BookmarkPostDTO dto);

    // post share
    @POST(ServicePath.POST_SHARE + "{postId}")
    Flowable<Object> sharePost(@Path("postId") int id);

    // profile
    @GET(ServicePath.PROFILE + "/{mobileNumber}")
    Flowable<ClientUserDTO> getProfile(@Path("mobileNumber") String mobileNumber);

    @POST(ServicePath.EDIT_PROFILE)
    Flowable<ClientUserDTO> editProfile(@Body ClientUserDTO user);

    // mood list
    @POST(ServicePath.MOOD_LIST)
    Flowable<MoodStatisticsDTO> getMoodList(@Query("mobile") String mobile, @Query("period") int period);

    // mood add
    @POST(ServicePath.MOOD_ADD)
    Flowable<ResponseBody> addMood(@Body MoodDTO moodDTO);

}