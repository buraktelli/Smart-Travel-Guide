package com.guide.tezproject.api;

import com.guide.tezproject.entity.Sonuc;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface SearchCountryExceptCity {

    @GET("api/gezilecekyer/country/Türkiye/{city}")
    Call<ArrayList<Sonuc>> getGezilecekYerler(@Header("Authorization") String token, @Path(value = "city") String city);

}
