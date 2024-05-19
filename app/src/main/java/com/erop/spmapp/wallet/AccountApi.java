package com.erop.spmapp.wallet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface AccountApi {

    @GET("api/public/accounts/me")
    Call<AccountInfo> getCurrentAccountInfo(@Header("Authorization") String authorization);
}
