package com.mindology.app.ui.main.inspections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InspectionResource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public InspectionResource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> InspectionResource<T> propertyUpdated(@Nullable T data) {
        return new InspectionResource<>(Status.PROPERTY_UPDATED, data, null);
    }

    public static <T> InspectionResource<T> error(@NonNull String msg, @Nullable T data) {
        return new InspectionResource<>(Status.ERROR, data, msg);
    }

    public static <T> InspectionResource<T> loading(@Nullable T data) {
        return new InspectionResource<>(Status.LOADING, data, null);
    }

    public static <T> InspectionResource<T> updateUI(@Nullable T data) {
        return new InspectionResource<>(Status.UPDATE_UI, data, null);
    }

    public static <T> InspectionResource<T> inspectionUpdated(@Nullable T data) {
        return new InspectionResource<>(Status.INSPECTION_UPDATED, data, null);
    }

    public static <T> InspectionResource<T> inspectionCreated(@Nullable T data) {
        return new InspectionResource<>(Status.INSPECTION_CREATED, data, null);
    }

    public enum Status {PROPERTY_UPDATED, ERROR, LOADING, UPDATE_UI, INSPECTION_UPDATED, INSPECTION_CREATED}
}