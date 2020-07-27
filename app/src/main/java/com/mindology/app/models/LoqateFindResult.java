package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoqateFindResult {
    @SerializedName("Items")
    @Expose
    private List<LoqateAddress> items;

    public List<LoqateAddress> getItems() {
        return items;
    }

    public void setItems(List<LoqateAddress> items) {
        this.items = items;
    }
}
