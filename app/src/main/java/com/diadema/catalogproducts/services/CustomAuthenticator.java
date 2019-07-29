package com.diadema.catalogproducts.services;

import java.io.IOException;
import javax.annotation.Nullable;
import androidx.annotation.NonNull;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import com.diadema.catalogproducts.services.TokenManager;
import com.diadema.catalogproducts.entities.AccessToken;

/**
 * Created by CEK on 23/03/2019.
 */

public class CustomAuthenticator implements Authenticator {
    private String tokenManager;
    private static CustomAuthenticator INSTANCE;

    private CustomAuthenticator(){
        this.tokenManager = "";
    }

    static synchronized CustomAuthenticator getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CustomAuthenticator();
        }

        return INSTANCE;
    }

    /*@Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        if(responseCount(response) >= 3){
            return null;
        }

        AccessToken token = tokenManager.getToken();

        ApiService service = RetrofitBuilder.createService(ApiService.class);
        Call<AccessToken> call = service.refresh(token.getRefreshToken() + "a");
        retrofit2.Response<AccessToken> res = call.execute();

        if(res.isSuccessful()){
            AccessToken newToken = res.body();
            tokenManager.saveToken(newToken);

            return response.request().newBuilder().header("Authorization", "Bearer " + res.body().getAccessToken()).build();
        }else{
            return null;
        }
    }*/

    @Nullable
    @Override
    public Request authenticate(Route route, @NonNull Response response) throws IOException {
        if(responseCount(response) >= 3){
            return null;
        }

        if(response.isSuccessful()){
            return response.request().newBuilder().build();
        }else{
            return null;
        }
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}