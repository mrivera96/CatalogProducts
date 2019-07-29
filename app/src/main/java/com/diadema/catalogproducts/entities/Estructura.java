package com.diadema.catalogproducts.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergio Ayestas on 10/04/2019.
 */

public class Estructura {
    @SerializedName("id")
    private String id;

    @SerializedName("product_id")
    private String[] product_id;

    public Estructura(String id, String[] product_id) {
        this.id = id;
        this.product_id = product_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String codigo) {
        this.id = codigo;
    }

    public String[] getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String[] product_id) {
        this.product_id = product_id;
    }

}
