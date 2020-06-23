package com.guide.tezproject.apimapbox;

import androidx.annotation.NonNull;

import com.guide.tezproject.entity.Sonuc;

import java.util.ArrayList;

public interface RouteServiceCallback {

    void onSuccess(@NonNull ArrayList<Sonuc> value);

    void onError(@NonNull Throwable throwable);

}
