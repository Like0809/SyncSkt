package com.like.common.network;

import java.util.Map;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public class RequestBuilder {

    private Request request;
    private Postman postman;
    private long timeout = 8000;

    public RequestBuilder client(Postman postman) {
        this.postman = postman;
        return this;
    }

    private Request getRequest() {
        if (request == null) {
            request = new Request();
        }
        return request;
    }

    public RequestBuilder request(Request request) {
        request.getParams().putAll(request.getParams());
        this.request = request;
        return this;
    }

    public RequestBuilder param(String key, Object value) {
        getRequest().addParam(key, value);
        return this;
    }

    public RequestBuilder params(Map<String, Object> params) {
        getRequest().getParams().putAll(params);
        return this;
    }

    public RequestBuilder timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public void execute() {
        execute(null);
    }

    public void execute(RequestCallback callback) {
        RequestCall call = SyncSktClient.getCallBuilder()
                .client(postman)
                .callback(callback)
                .data(getRequest())
                .timeout(timeout)
                .build();
        SyncSktClient.getInstance().execute(call);
    }

    public void executeBlocked() {
        executeBlocked(null);
    }

    public void executeBlocked(RequestCallback callback) {
        RequestCall call = SyncSktClient.getCallBuilder()
                .client(postman)
                .callback(callback)
                .data(getRequest())
                .timeout(timeout)
                .build();
        SyncSktClient.getInstance().executeBlocked(call);
    }
}
