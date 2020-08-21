package com.mindology.app.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AuthResource<T> {

    @NonNull
    public final AuthStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public AuthResource(@NonNull AuthStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> AuthResource<T> error(@NonNull String msg, @Nullable T data) {
        return new AuthResource<>(AuthStatus.ERROR, data, msg);
    }

    public static <T> AuthResource<T> loading(@Nullable T data) {
        return new AuthResource<>(AuthStatus.LOADING, data, null);
    }

    public static <T> AuthResource<T> phoneNumberValidRegistered(@Nullable T data) {
        return new AuthResource<>(AuthStatus.PHONE_VALID_REGISTERED, data, null);
    }

    public static <T> AuthResource<T> phoneNumberValidNotRegistered(@Nullable T data) {
        return new AuthResource<>(AuthStatus.PHONE_VALID_NOT_REGISTERED, data, null);
    }

    public static <T> AuthResource<T> codeEntered(@Nullable T data) {
        return new AuthResource<>(AuthStatus.CODE_ENTERED, data, null);
    }

    public static <T> AuthResource<T> activated(@Nullable T data) {
        return new AuthResource<>(AuthStatus.ACTIVATED, data, null);
    }

    public static <T> AuthResource<T> profileFilled(@Nullable T data) {
        return new AuthResource<>(AuthStatus.PROFILE_FILLED, data, null);
    }

    public static <T> AuthResource<T> logout() {
        return new AuthResource<>(AuthStatus.PHONE_NOT_VALID, null, null);
    }

    public enum AuthStatus {
        ERROR,
        LOADING,
        PHONE_NOT_VALID,
        PHONE_VALID_REGISTERED,
        PHONE_VALID_NOT_REGISTERED,
        CODE_ENTERED,
        ACTIVATED,
        PROFILE_FILLED
    }

}