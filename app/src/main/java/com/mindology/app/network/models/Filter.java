package com.mindology.app.network.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.util.Enums;

public class Filter
{
    @SerializedName("field")
    @Expose
    private String field;

    @SerializedName("operator")
    @Expose
    private Enums.FilterOperators operator;

    @SerializedName("value")
    @Expose
    private String value;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Enums.FilterOperators getOperator() {
        return operator;
    }

    public void setOperator(Enums.FilterOperators operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "(";
        str += field + ")(";
        str += operator.name() + ":%" + value + "%)";
//        return (new Gson()).toJson(this);
        return str;
    }
}