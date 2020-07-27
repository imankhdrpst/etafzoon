package com.mindology.app.network.auth;

import com.mindology.app.models.VerificationRequestDTO;
import com.mindology.app.models.ClientLoginDTO;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.TokenResponse;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {


//    @POST("auth/login")
//    Flowable<BaseResponse<User>> loginUser(
//            @Body UsernamePassword user
//    );

//    @FormUrlEncoded
    @POST("auth/client/login")
    Flowable<TokenResponse> loginUser(@Body ClientLoginDTO clientLoginDTO);


    @POST("auth/client/login/verify/code")
    Flowable<TokenResponse> verifyCode(@Body VerificationRequestDTO verificationRequestDTO);

    @POST("user/signup")
    Flowable<ClientUserDTO> signup(@Body ClientUserDTO dto);

}
