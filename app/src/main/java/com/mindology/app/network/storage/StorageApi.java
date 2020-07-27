package com.mindology.app.network.storage;

import com.mindology.app.models.UploadResponse;
import com.mindology.app.network.ServicePath;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface StorageApi {

    @Multipart
    @POST(ServicePath.FILES)
    Flowable<UploadResponse> upload(@Part MultipartBody.Part file);


}