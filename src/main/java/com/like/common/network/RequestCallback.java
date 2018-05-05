package com.like.common.network;

@SuppressWarnings("ALL")
public interface RequestCallback {

    void onSuccess(String response);

    void onFailed(Throwable throwable);

}
