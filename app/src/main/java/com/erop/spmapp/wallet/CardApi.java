package com.erop.spmapp.wallet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CardApi {

    @GET("api/public/card")
    Call<CardInfo> getCardInfo(@Header("Authorization") String authorization);
}

