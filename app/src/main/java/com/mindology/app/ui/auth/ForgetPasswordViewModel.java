package com.mindology.app.ui.auth;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.TokenResponse;
import com.mindology.app.models.User;
import com.mindology.app.network.auth.AuthApi;
import com.mindology.app.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class ForgetPasswordViewModel extends ViewModel {

    private static final String TAG = "ForgetPasswordViewModel";

    // inject
    private final SessionManager sessionManager; // @Singleton scoped dependency
    private final AuthApi authApi; // @ForgetPasswordScope scoped dependency


    @Inject
    public ForgetPasswordViewModel(AuthApi authApi, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.authApi = authApi;
        Log.d(TAG, "AuthViewModel: viewmodel is working...");
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return sessionManager.getAuthUser();
    }

//    public void forgetPassword(String username) {
//        Log.d(TAG, "attemptLogin: attempting to login.");
//        sessionManager.authenticateWithId(queryForgetPassword(username));
//    }
//
//    private LiveData<AuthResource<User>> queryForgetPassword(final String username) {
//
//        return LiveDataReactiveStreams.fromPublisher(
//                authApi.forgetPassword(Constants.CLIENT_ID, Constants.GRANT_TYPE, username)
//
//                        // instead of calling onError, do this
//                        .onErrorReturn(new Function<Throwable, TokenResponse>() {
//                            @Override
//                            public TokenResponse apply(Throwable throwable) throws Exception {
//                                try {
//                                    JSONObject obj = new JSONObject(((HttpException) throwable).response().errorBody().string());
//                                    return new TokenResponse(obj.getString("error"), obj.getString("error_description"));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                return new TokenResponse();
//                            }
//                        })
//
//                        // wrap User object in AuthResource
//                        .map(new Function<TokenResponse, AuthResource<User>>() {
//                            @Override
//                            public AuthResource<User> apply(TokenResponse tokenResponse) throws Exception {
//
//                                if (TextUtils.isEmpty(tokenResponse.getRefreshToken())) {
//                                    return AuthResource.error(tokenResponse.getErrorDescription(), null);
//                                }
//                                sessionManager.saveToken(tokenResponse);
//                                return AuthResource.authenticated(new User(tokenResponse.getRefreshToken(), tokenResponse.getAccessToken()));
//                            }
//                        })
//                        .subscribeOn(Schedulers.io()));
//    }


}









