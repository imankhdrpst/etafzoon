package com.mindology.app.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.models.Alarm;
import com.mindology.app.models.Area;
import com.mindology.app.models.Attachment;
import com.mindology.app.models.BaseModel;
import com.mindology.app.models.Meter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListResponse<T> extends BaseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private List<T> data = new ArrayList<>();

    public ListResponse(List<T> data) {
        this.data = data;
    }

    public ListResponse(){}

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isEqualTo(ListResponse<T> inspections) {
        boolean res = data.size() == inspections.getData().size();
        if (res) {
            for (int i = 0 ; i < data.size() ; i++)
            {
                res &= isEqual(data.get(i), inspections.data.get(i));
            }
        }
        return res;
    }

    private boolean isEqual(Object t, Object t1) {
        if (t instanceof  Meter && t1 instanceof Meter)
        {
            return (((Meter)t).isEqualTo((Meter)t1));
        }
        else if(t instanceof Alarm && t1 instanceof Alarm)
        {
            return (((Alarm)t).isEqualTo((Alarm)t1));
        }
        else if (t instanceof Area && t1 instanceof Area)
        {
            return ((Area)t).isEqualTo((Area)t1);
        }
        else if (t instanceof Attachment && t1 instanceof Attachment)
        {
            return ((Attachment)t).isEqualTo((Attachment)t1);
        }
        return false;
    }
}
