package com.mindology.app.network.main;

import com.mindology.app.models.BookmarkPostDTO;
import com.mindology.app.models.Client;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.ContactMessageResponse;
import com.mindology.app.models.Devices;
import com.mindology.app.models.HelpfulCreatePostDTO;
import com.mindology.app.models.HelpfulPostDTO;
import com.mindology.app.models.HotSpot;
import com.mindology.app.models.Inspection;
import com.mindology.app.models.InspectionCreate;
import com.mindology.app.models.Panorama;
import com.mindology.app.models.Post;
import com.mindology.app.models.PostGroup;
import com.mindology.app.models.Property;
import com.mindology.app.models.PropertyCreate;
import com.mindology.app.models.User;
import com.mindology.app.network.ListResponse;
import com.mindology.app.network.ServicePath;
import com.mindology.app.network.models.Filters;
import com.mindology.app.network.models.Sort;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    // properties
    @GET(ServicePath.PROPERTIES)
    Flowable<ListResponse<Property>> getProperties(@Query("fq") Filters filters, @Query("sort") Sort sortModel);

    @POST(ServicePath.PROPERTIES)
    Flowable<Property> createProperty(@Body PropertyCreate property);

    @PUT(ServicePath.PROPERTIES + "/{id}")
    Flowable<Property> editProperty(@Path("id") String id, @Body Property property);

    // inspections
    @GET(ServicePath.INSPECTIONS)
    Flowable<ListResponse<Inspection>> getInspections(@Query("fq") Filters filters, @Query("sort") Sort sortModel);

    @POST(ServicePath.INSPECTIONS)
    Flowable<Inspection> createInspection(@Body InspectionCreate inspection);

    @GET(ServicePath.INSPECTIONS + "/{id}")
    Flowable<Inspection> getInspection(@Path("id") String id);

    @PUT(ServicePath.INSPECTIONS + "/{id}")
    Flowable<Inspection> editInspection(@Path("id") String id, @Body Inspection inspection);

    @PATCH(ServicePath.INSPECTIONS + "/{id}")
    Flowable<Inspection> changeInspectionState(@Path("id") String id, @Body Inspection.InspectionStateJsonable body);

    @GET(ServicePath.INSPECTIONS + "/suggested-link")
    Flowable<ListResponse<Inspection>> getLinkInspectionSuggestions(@Query("propertyId") String propertyId, @Query("type") String type);

    @GET(ServicePath.INSPECTIONS + "/inspection-summary")
    Flowable<ListResponse<Inspection>> getSummaryInspections(@Query("state") String state ,@Query("offset") int offset, @Query("limit") int limit);


    @PUT(ServicePath.PROFILE)
    Flowable<User> editProfile(@Body User user);

    // contact us messages
    @POST(ServicePath.CONTACT_MESSAGES)
    Flowable<ContactMessageResponse> sendMessage(@Body ContactMessageResponse message);

    // LOGS
    @POST(ServicePath.LOGS + "")
    Call<ResponseBody> insertLog(@Body String id);

    //Clients
    @GET(ServicePath.CLIENTS)
    Flowable<ListResponse<Client>> getClients(@Query("state") String state);

    // panoramas
//    @POST(ServicePath.PANORAMAS)
//    Single<Panorama> postPanorama(@Body InspectionDetailsFragment.PanoramaToUpload image);

    @GET(ServicePath.PANORAMAS)
    Flowable<ListResponse<Panorama>> getAllPanoramas(@Query("areaId") String areaId);

    //hotspots
//    @POST(ServicePath.PANORAMAS + "/{id}/hotspots")
//    Flowable<HotSpot> postHotspot(@Body HotSpot hotSpot, @Path("id") String panoramaId);

//    @PATCH(ServicePath.PANORAMAS + "/{id}/hotspots")
//    Flowable<HotSpot> postHotspots(@Body ListResponse<HotSpot> hotSpots, @Path("id") String panoramaId);

//    @PATCH(ServicePath.PANORAMAS + "/{panoramaId}")
//    Flowable<ResponseBody> bindPanoramaToId(@Path("panoramaId") String panoramaId, @Body InspectionDetailsFragment.PanoramaToArea panoramaToArea);

    @GET(ServicePath.PANORAMAS + "/{panoramaId}/hotspots")
    Flowable<ListResponse<HotSpot>> getAllHotspots(@Path("panoramaId") String id);

    // os version properties
    @PUT(ServicePath.DEVICES)
    Flowable<Object> devices(@Body Devices devices);

}