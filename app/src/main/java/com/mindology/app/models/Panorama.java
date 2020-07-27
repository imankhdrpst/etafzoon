package com.mindology.app.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.network.ListResponse;

public class Panorama extends BaseModel implements Comparable<Panorama> {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("image")
    @Expose
    private Image image;

    @SerializedName("index")
    @Expose
    private int index;

//    @SerializedName("hotspots")
//    @Expose
    private ListResponse<HotSpot> hotspots = new ListResponse<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ListResponse<HotSpot> getHotspots() {
        return hotspots;
    }

    public void setHotspots(ListResponse<HotSpot> hotspots) {
        this.hotspots = hotspots;
    }

    public boolean isSynced()
    {
        return !(TextUtils.isEmpty(id) && TextUtils.isEmpty(image.getId()));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(Panorama panorama) {
        return index - panorama.index;
    }
}
