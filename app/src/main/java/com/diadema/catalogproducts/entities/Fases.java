package com.diadema.catalogproducts.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergio Ayestas on 11/04/2019.
 */

public class Fases {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("numero_fase")
    private String numero_fase;

    @SerializedName("img_fase1")
    private String img_fase1;

    public Fases(String id, String name, String numero_fase, String img_fase1) {
        this.id = id;
        this.name = name;
        this.numero_fase = numero_fase;
        this.img_fase1 = img_fase1;
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

    public String getNumero_fase() {
        return numero_fase;
    }

    public void setNumero_fase(String numero_fase) {
        this.numero_fase = numero_fase;
    }

    public String getImg_fase1() {
        return img_fase1;
    }

    public void setImg_fase1(String img_fase1) {
        this.img_fase1 = img_fase1;
    }
}
