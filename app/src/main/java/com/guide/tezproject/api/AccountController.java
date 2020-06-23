package com.guide.tezproject.api;


import com.guide.tezproject.api.model.CheckResponse;
import com.guide.tezproject.api.model.LoginRequest;
import com.guide.tezproject.api.model.LoginResponse;
import com.guide.tezproject.api.model.RegistrationRequest;
import com.guide.tezproject.api.model.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountController {

    @POST("api/token")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/token/register")
    Call<RegistrationResponse> register(@Body RegistrationRequest registrationRequest);

    @GET("api/token/check/{username}/{token}")
    Call<CheckResponse> check(@Path(value = "username") String username, @Path(value = "token") String token);

}
