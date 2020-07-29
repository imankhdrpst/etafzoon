package com.mindology.app.ui.auth;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.ClientLoginDTO;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.TokenResponse;
import com.mindology.app.models.User;
import com.mindology.app.models.VerificationRequestDTO;
import com.mindology.app.network.auth.AuthApi;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {
    // inject
    private final SessionManager sessionManager;
    private final AuthApi authApi;
    private String tokenReceived = "";
    private boolean userAlreadyRegisteredAndNoNeedToRegister = false;


    @Inject
    public AuthViewModel(AuthApi authApi, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.authApi = authApi;
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return sessionManager.getAuthUser();
    }

    public boolean authenticateWithSavedToken() {
        try {
            return !TextUtils.isEmpty(sessionManager.getSavedToken());
        } catch (SessionManager.TokenNotSaved tokenNotSaved) {
            tokenNotSaved.printStackTrace();
            return false;
        }
    }

    private LiveData<AuthResource<User>> queryUserPhoneNumber(final String mobileNumber) {
        ClientLoginDTO clientLoginDTO = new ClientLoginDTO(mobileNumber);
        return LiveDataReactiveStreams.fromPublisher(
                authApi.loginUser(clientLoginDTO)

                        // instead of calling onError, do this
                        .onErrorReturn(new Function<Throwable, TokenResponse>() {
                            @Override
                            public TokenResponse apply(Throwable throwable) throws Exception {
                                TokenResponse error = new TokenResponse();
                                error.setMessage("خطا در برقراری ارتباط");
                                return error;
                            }
                        })

                        // wrap User object in AuthResource
                        .map(new Function<TokenResponse, AuthResource<User>>() {
                            @Override
                            public AuthResource<User> apply(TokenResponse tokenResponse) throws Exception {

                                if (!TextUtils.isEmpty(tokenResponse.getMessage())) {
                                    return AuthResource.error(tokenResponse.getMessage(), null);
                                } else if (tokenResponse.getAuthenticated() != null && tokenResponse.getAuthenticated().equals("1")) {
                                    userAlreadyRegisteredAndNoNeedToRegister = true;
                                }
                                return AuthResource.phoneNumberValid(new User());
                            }
                        })
                        .subscribeOn(Schedulers.io()));
    }

    private LiveData<AuthResource<User>> queryActivationCode(final String mobileNumber, final String activationCode) {
        VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO(mobileNumber, activationCode);
        return LiveDataReactiveStreams.fromPublisher(
                authApi.verifyCode(verificationRequestDTO)

                        // instead of calling onError, do this
                        .onErrorReturn(new Function<Throwable, TokenResponse>() {
                            @Override
                            public TokenResponse apply(Throwable throwable) throws Exception {
                                TokenResponse error = new TokenResponse();
                                error.setMessage("خطا در برقراری ارتباط");
                                return error;
                            }
                        })

                        // wrap User object in AuthResource
                        .map(new Function<TokenResponse, AuthResource<User>>() {
                            @Override
                            public AuthResource<User> apply(TokenResponse tokenResponse) throws Exception {

                                if (TextUtils.isEmpty(tokenResponse.getToken())) {
                                    return AuthResource.error(tokenResponse.getMessage(), null);
                                }
                                tokenReceived = tokenResponse.getToken();
                                if (userAlreadyRegisteredAndNoNeedToRegister) {
                                    sessionManager.saveToken(tokenResponse.getToken());
                                    sessionManager.saveMobileNumber(verificationRequestDTO.getMobileNumber());
                                    return AuthResource.profileFilled(new User(tokenResponse.getToken()));
                                } else {
                                    return AuthResource.authenticated(new User(tokenResponse.getToken()));
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io()));
    }

    private LiveData<AuthResource<User>> fillProfile(ClientUserDTO dto) {
        return LiveDataReactiveStreams.fromPublisher(
                authApi.signup(dto)

                        // instead of calling onError, do this
                        .onErrorReturn(new Function<Throwable, ClientUserDTO>() {
                            @Override
                            public ClientUserDTO apply(Throwable throwable) throws Exception {
                                ClientUserDTO error = new ClientUserDTO();
                                error.setMessage("خطا در برقراری ارتباط");
                                return new ClientUserDTO();
                            }
                        })

                        // wrap User object in AuthResource
                        .map(new Function<ClientUserDTO, AuthResource<User>>() {
                            @Override
                            public AuthResource<User> apply(ClientUserDTO t) throws Exception {

                                if (!TextUtils.isEmpty(t.getMessage())) {
                                    return AuthResource.error(t.getMessage(), null);
                                }
                                sessionManager.saveToken(tokenReceived);
                                sessionManager.saveMobileNumber(dto.getMobileNumber());
                                return AuthResource.profileFilled(new User());
                            }
                        })
                        .subscribeOn(Schedulers.io()));
    }


    public void authenticateWithActivationCode(String phone, String activationCode) {
        sessionManager.authenticateWithId(queryActivationCode(phone, activationCode));
    }

    public void authenticateWithPhoneNumber(String phone) {
        sessionManager.authenticateWithId(queryUserPhoneNumber(phone));
    }

    public void authenticateWithProfile(String name, String family, String mobileNumber, String age, String city) {
        ClientUserDTO dto = new ClientUserDTO();
        dto.setFirstName(name);
        dto.setLastName(family);
        dto.setAge(age);
        dto.setLivingCity(city);
        dto.setMobileNumber(mobileNumber);
        sessionManager.authenticateWithId(fillProfile(dto));
    }

}









