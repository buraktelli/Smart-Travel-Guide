package com.guide.tezproject.api;

import com.guide.tezproject.api.model.ImagesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface SearchImages {

    @GET("api/images/{name}/{city}")
    Call<ArrayList<ImagesResponse>> getImages(@Header("Authorization") String token, @Path(value = "name") String name, @Path(value = "city") String city);

}
