package com.mindology.app.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.network.ListResponse;

public class Client extends BaseModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("inspections")
    @Expose
    private ListResponse<Inspection> inspections;

    public Client(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ListResponse<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(ListResponse<Inspection> inspections) {
        this.inspections = inspections;
    }

    public Client copyOf() {
        Client client = new Client(getId());
        client.setName(getName());
        client.setInspections(getInspections());

        return client;
    }


    public boolean isEqualTo(Client client) {
        return (client != null) &&
                (TextUtils.isEmpty(id) && TextUtils.isEmpty(client.getId()) || (id.equals(client.getId()))) &&
                ((TextUtils.isEmpty(name) && TextUtils.isEmpty(client.getName())) || (name.equals(client.getName()))) &&
                ((inspections == null && client.getInspections() == null) || (inspections.isEqualTo(client.getInspections())));
    }
}
