package com.diadema.catalogproducts.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import com.diadema.catalogproducts.entities.EstructuraResponse;
import com.diadema.catalogproducts.entities.FasesResponse;
import com.diadema.catalogproducts.entities.ProductoResponse;

/**
 * Created by CEK on 23/03/2019.
 */

public interface ApiService {

    @POST("getProducts")
    @FormUrlEncoded
    Call<ProductoResponse> productos(@Field("region") String region);

    @POST("getPhases")
    @FormUrlEncoded
    Call<FasesResponse> fases(@Field("productId") String productId);

    @POST("getBOM")
    @FormUrlEncoded
    Call<EstructuraResponse> estructura(@Field("templateId") String templateId);
}