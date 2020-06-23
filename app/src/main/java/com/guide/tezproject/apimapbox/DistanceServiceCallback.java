package com.guide.tezproject.apimapbox;

import androidx.annotation.NonNull;

public interface DistanceServiceCallback {

    void onSuccess1(@NonNull double value, int l);

    void onError1(@NonNull Throwable throwable);

}
