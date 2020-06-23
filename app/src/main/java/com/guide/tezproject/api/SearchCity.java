package com.guide.tezproject.api;

import com.guide.tezproject.entity.GezilecekYer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface SearchCity {

    @GET("api/gezilecekyer/city/{city}")
    Call<ArrayList<GezilecekYer>> getGezilecekYerler(@Header ("Authorization") String token, @Path(value = "city") String city);

}
