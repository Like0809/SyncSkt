package com.like.common.network;

import java.util.Map;

@SuppressWarnings("ALL")
public interface Postman {

    Map<String, String> getParams();

    Postman collect(String key, String value);

    void send(DataPackage message) throws Exception;

}
