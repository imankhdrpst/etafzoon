package com.mindology.app.network.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Filters {

    @SerializedName("filters")
    @Expose
    List<Filter> filters = new ArrayList<>();

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    @NonNull
    @Override
    public String toString() {
        String res = "";
        for (Filter filter : filters)
        {
            res += filter.toString() + ",";
        }
        return res.substring(0, res.lastIndexOf(',')) + "";
    }

    public void add(Filter filter) {
        filters.add(filter);
    }
}
