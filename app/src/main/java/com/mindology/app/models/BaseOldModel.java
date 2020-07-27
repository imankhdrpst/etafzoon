package com.mindology.app.models;

import com.google.gson.annotations.Expose;

public class BaseOldModel {

    @Expose(deserialize = false, serialize = false)
    private ErrorResponse errorResponse;

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }


}
