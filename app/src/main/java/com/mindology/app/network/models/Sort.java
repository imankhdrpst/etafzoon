package com.mindology.app.network.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.util.Enums;

public class Sort {
    @SerializedName("field")
    @Expose
    private String field;

    @SerializedName("order")
    @Expose
    private Enums.SortOrder order = Enums.SortOrder.DESC;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Enums.SortOrder getOrder() {
        return order;
    }

    public void setOrder(Enums.SortOrder order) {
        this.order = order;
    }

    @NonNull
    @Override
    public String toString() {
        String str = order.name().toLowerCase() + "(" + field + ")";
        return str;
    }
}
