package com.mindology.app.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.network.ListResponse;
import com.mindology.app.repo.TempDataHolder;
import com.mindology.app.util.Enums;

public class Area extends BaseModel implements Comparable<Area> {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private Enums.AssetType type = Enums.AssetType.AREA;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("index")
    @Expose
    private int index;

    @SerializedName("attachment")
    @Expose
    private ListResponse<Attachment> attachments;

//    @SerializedName("panoramas")
//    @Expose
    private ListResponse<Panorama> panoramas = new ListResponse<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Enums.AssetType getType() {
        return type;
    }

    public void setType(Enums.AssetType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ListResponse<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ListResponse<Attachment> attachments) {
        this.attachments = attachments;
    }

    public ListResponse<Panorama> getPanoramas() {
        return panoramas;
    }

    public void setPanoramas(ListResponse<Panorama> panoramas) {
        this.panoramas = panoramas;
    }

    public String isValid()
    {
        if (TextUtils.isEmpty(title))
        {
            return "An area has no title";
        }
        try {
            int count = 0;
            for (Area area : TempDataHolder.getCurrentInspection().getAssets().getAreas().getData())
            {
                if (!area.equals(this) && area.getTitle().toLowerCase().equals(title.toLowerCase()))
                {
                    count ++;
                }
            }
            if (count >= 2)
            {
                return "Two areas has same title";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isEqualTo(Area t1) {
        return ((TextUtils.isEmpty(id) && TextUtils.isEmpty(t1.id)) || (id.equals(t1.id))) &&
                ((TextUtils.isEmpty(title) && TextUtils.isEmpty(t1.title)) || (title.equals(t1.title))) &&
                ((index == t1.index)) &&
                ((type == null && t1.type == null) || (type.equals(t1.type))) &&
                ((panoramas == null && t1.panoramas == null) || (panoramas.isEqualTo(t1.panoramas)));
    }

    @Override
    public int compareTo(Area o) {
        return  index - o.index;
    }
}
