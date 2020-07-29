package com.mindology.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.mindology.app.models.TokenResponse;
import com.mindology.app.models.User;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.ui.auth.AuthResource;
import com.mindology.app.util.SharedPrefrencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private SharedPrefrencesHelper preferences;

    // data
    private MediatorLiveData<AuthResource<User>> cachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager(SharedPrefrencesHelper helper) {
        this.preferences = helper;
    }

    public void authenticateWithId(final LiveData<AuthResource<User>> source) {
        if (cachedUser != null) {
            cachedUser.setValue(AuthResource.loading((User) null));
            cachedUser.addSource(source, new Observer<AuthResource<User>>() {
                @Override
                public void onChanged(AuthResource<User> userAuthResource) {
                    cachedUser.setValue(userAuthResource);
                    cachedUser.removeSource(source);

                    if (userAuthResource.status.equals(AuthResource.AuthStatus.ERROR)) {
                        cachedUser.setValue(AuthResource.<User>logout());
                    }
                }
            });
        }
    }


    public void logOut() {
        clearTokenOnPrefs();
        TempDataHolder.resetAllData();
        preferences.clearAll();
        cachedUser.setValue(AuthResource.<User>logout());
    }


    public LiveData<AuthResource<User>> getAuthUser() {
        return cachedUser;
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
















