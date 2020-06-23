package com.guide.tezproject.api.callback;

import com.guide.tezproject.entity.Sonuc;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public interface SearchCountryExceptCityCallback {
    void data2(@NonNull ArrayList<Sonuc> turkiye);

    void onError2(@NonNull Throwable throwable);
}
