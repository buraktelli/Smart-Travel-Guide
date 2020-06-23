package com.guide.tezproject.api.callback;

import com.guide.tezproject.entity.GezilecekYer;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public interface SearchCityCallback {
    void getGezilecekYerler(@NonNull ArrayList<GezilecekYer> gezilecekYerler);

    void onError2(@NonNull Throwable throwable);
}
