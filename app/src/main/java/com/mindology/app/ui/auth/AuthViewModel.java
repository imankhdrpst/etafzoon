package com.mindology.app.ui.auth;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.mindology.app.SessionManager;
import com.mindology.app.models.ClientLoginDTO;
import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.TokenResponse;
import com.mindology.app.models.VerificationRequestDTO;
import com.mindology.app.network.auth.AuthApi;
import com.mindology.app.util.Enums;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {
    // inject
    private final SessionManager sessionManager;
    private final AuthApi authApi;
    private String tokenReceived = "";


    @Inject
    public AuthViewModel(AuthApi authApi, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.authApi = authApi;
    }

    public LiveData<AuthResource<ClientUserDTO>> observeAuthState() {
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

    private LiveData<AuthResource<ClientUserDTO>> queryUserPhoneNumber(final String mobileNumber) {
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
                        .map(new Function<TokenResponse, AuthResource<ClientUserDTO>>() {
                            @Override
                            public AuthResource<ClientUserDTO> apply(TokenResponse tokenResponse) throws Exception {

                                if (!TextUtils.isEmpty(tokenResponse.getMessage())) {
                                    return AuthResource.error(tokenResponse.getMessage(), null);
                                }
                                ClientUserDTO response = sessionManager.getAuthUser().getValue().data;
                                if (response == null)
                                {
                                    response = new ClientUserDTO();
                                }
                                response.setMobileNumber(mobileNumber);

                                if (tokenResponse.getAuthenticated() != null && tokenResponse.getAuthenticated().equals("1")) {
                                    return AuthResource.phoneNumberValidRegistered(response);
                                } else {
                                    return AuthResource.phoneNumberValidNotRegistered(response);
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io()));
    }

    private LiveData<AuthResource<ClientUserDTO>> queryActivationCode(final String mobileNumber, final String activationCode) {
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
                        .map(new Function<TokenResponse, AuthResource<ClientUserDTO>>() {
                            @Override
                            public AuthResource<ClientUserDTO> apply(TokenResponse tokenResponse) throws Exception {

                                if (TextUtils.isEmpty(tokenResponse.getToken())) {
                                    return AuthResource.error(tokenResponse.getMessage(), null);
                                }
                                tokenReceived = tokenResponse.getToken();
                                if (sessionManager.getAuthUser().getValue().status == AuthResource.AuthStatus.PHONE_VALID_REGISTERED) {
                                    sessionManager.saveToken(tokenResponse.getToken());
                                    sessionManager.saveMobileNumber(verificationRequestDTO.getMobileNumber());
                                    return AuthResource.codeEntered(sessionManager.getAuthUser().getValue().data);
                                } else {
                                    return AuthResource.activated(sessionManager.getAuthUser().getValue().data);
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io()));
    }

    private LiveData<AuthResource<ClientUserDTO>> fillProfile(ClientUserDTO dto) {
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
                        .map(new Function<ClientUserDTO, AuthResource<ClientUserDTO>>() {
                            @Override
                            public AuthResource<ClientUserDTO> apply(ClientUserDTO t) throws Exception {

                                if (!TextUtils.isEmpty(t.getMessage())) {
                                    return AuthResource.error(t.getMessage(), null);
                                }
                                sessionManager.saveToken(tokenReceived);
                                sessionManager.saveMobileNumber(dto.getMobileNumber());
                                return AuthResource.profileFilled(new ClientUserDTO());
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

    public String getLatestMobileAttempted() {
        return sessionManager.getAuthUser().getValue().data.getMobileNumber();
    }
}









