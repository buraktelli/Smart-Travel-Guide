package com.guide.tezproject.api;

import com.guide.tezproject.api.model.MostFrequentAranan;
import com.guide.tezproject.entity.GezilecekYer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ArananYer {
    @GET("api/arananyer/frequent")
    Call<ArrayList<MostFrequentAranan>> getMostFrequent(@Header("Authorization") String tokden);

}
