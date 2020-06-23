package com.guide.tezproject.api.callback;

import androidx.annotation.NonNull;

import com.guide.tezproject.api.model.ImagesResponse;

import java.util.ArrayList;

public interface SearchImagesCallback {
    void getImages(@NonNull ArrayList<ImagesResponse> resimler);

    void errorGetImages(@NonNull Throwable throwable);
}
