package com.diadema.catalogproducts.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergio Ayestas on 30/03/2019.
 */

public class Producto {

    @SerializedName("create_date")
    private String create_date;

    @SerializedName("creater")
    private String[] creater;

    //SKU
    @SerializedName("default_code")
    private String default_code;

    @SerializedName("id")
    private String id;

    @SerializedName("image_small")
    private String image_small;

    @SerializedName("image_mediun")
    private String image_mediun;

    @SerializedName("image")
    private String image;

    @SerializedName("name")
    private String name;

    @SerializedName("product_tmpl_id")
    private String[] product_tmpl_id;

    public Producto(String default_code, String[] product_tmpl_id, String image, String name, String id) {
        this.default_code = default_code;
        this.product_tmpl_id = product_tmpl_id;
        this.image = image;
        this.name = name;
        this.id = id;
    }

    public String getDefault_code() {
        return default_code;
    }

    public void setDefault_code(String default_code) {
        this.default_code = default_code;
    }

    public String getImage_small() {
        return image_small;
    }

    public void setImage_small(String image_small) {
        this.image_small = image_small;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String[] getCreater() {
        return creater;
    }

    public void setCreater(String[] creater) {
        this.creater = creater;
    }

    public String getImage_mediun() {
        return image_mediun;
    }

    public void setImage_mediun(String image_mediun) {
        this.image_mediun = image_mediun;
    }

    public String[] getProduct_tmpl_id() {
        return product_tmpl_id;
    }

    public void setProduct_tmpl_id(String[] product_tmpl_id) {
        this.product_tmpl_id = product_tmpl_id;
    }
}