package com.guide.tezproject.api.callback;

import androidx.annotation.NonNull;

import com.guide.tezproject.api.model.MostFrequentAranan;
import com.guide.tezproject.entity.GezilecekYer;

import java.util.ArrayList;

public interface ArananYerCallback {
    void getMostFrequentAranan(@NonNull ArrayList<MostFrequentAranan> mostFrequentAranans);

    void onError2(@NonNull Throwable throwable);
}
