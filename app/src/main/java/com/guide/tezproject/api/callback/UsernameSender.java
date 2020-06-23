package com.guide.tezproject.api.callback;

import androidx.annotation.NonNull;

public interface UsernameSender {

    void getUsername(@NonNull String username);

    void errorGetUsername(@NonNull Throwable throwable);
}
