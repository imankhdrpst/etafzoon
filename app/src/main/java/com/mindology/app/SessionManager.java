package com.mindology.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.TokenResponse;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.auth.AuthResource;
import com.mindology.app.util.SharedPrefrencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private SharedPrefrencesHelper preferences;

    // data
    private MediatorLiveData<AuthResource<ClientUserDTO>> cachedUser = new MediatorLiveData<>();
    private AuthResource.AuthStatus currentAuthState = AuthResource.AuthStatus.PHONE_NOT_VALID;

    @Inject
    public SessionManager(SharedPrefrencesHelper helper) {
        this.preferences = helper;
        this.cachedUser.setValue(AuthResource.init(new ClientUserDTO()));
    }

    public void authenticateWithId(final LiveData<AuthResource<ClientUserDTO>> source) {
        if (cachedUser != null) {
            cachedUser.setValue(AuthResource.loading((ClientUserDTO) null));
            cachedUser.addSource(source, new Observer<AuthResource<ClientUserDTO>>() {
                @Override
                public void onChanged(AuthResource<ClientUserDTO> userAuthResource) {
                    cachedUser.setValue(userAuthResource);
                    cachedUser.removeSource(source);

                    if (userAuthResource.status.equals(AuthResource.AuthStatus.ERROR)) {
                        cachedUser.setValue(AuthResource.<ClientUserDTO>logout());
                    }
                }
            });
        }
    }


    public void logOut() {
        clearTokenOnPrefs();
        TempDataHolder.resetAllData();
        preferences.clearAll();
        cachedUser.setValue(AuthResource.<ClientUserDTO>logout());
    }


    public LiveData<AuthResource<ClientUserDTO>> getAuthUser() {
        return cachedUser;
    }

    public AuthResource.AuthStatus getCurrentAuthState()
    {
        return currentAuthState;
    }

    public void setCurrentAuthState(AuthResource.AuthStatus state) {
        currentAuthState = state;
    }


    public static class TokenRefreshInvalid extends Exception {
        TokenRefreshInvalid() {
            super();
        }
    }

    public static class TokenNotSaved extends Exception {
        TokenNotSaved() {
            super();
        }
    }

    public static class TokenInvalidException extends Exception {
        String refreshToken;

        TokenInvalidException(String refreshToken) {
            super();
            this.refreshToken = refreshToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }

    private void clearTokenOnPrefs() {
        preferences.clearTokenOnPrefs();
    }

    public String getSavedToken() throws TokenNotSaved {
        String token = preferences.getToken();
        if (token != null) {
            return token;
        } else throw new TokenNotSaved();
    }

    public void saveToken(TokenResponse token) {

        preferences.saveToken(token);
    }

    public void saveToken(String token) {

        preferences.saveToken(token);
    }

    public void saveMobileNumber(String number)
    {
        preferences.saveMobileNumber(number);
    }


}
















