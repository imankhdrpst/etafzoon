package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoqateRetrieveResult {
    @SerializedName("Items")
    @Expose
    private List<LoqateRetrievedAddress> items;

    public List<LoqateRetrievedAddress> getItems() {
        return items;
    }

    public void setItems(List<LoqateRetrievedAddress> items) {
        this.items = items;
    }
}
