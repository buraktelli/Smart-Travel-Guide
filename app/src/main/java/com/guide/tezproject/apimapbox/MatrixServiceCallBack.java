package com.guide.tezproject.apimapbox;

import androidx.annotation.NonNull;

public interface MatrixServiceCallBack {
    void mat(@NonNull double value[][],int boyut);

    void onError2(@NonNull Throwable throwable);
}
