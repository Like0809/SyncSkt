package com.like.common.network;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public class ResponseBuilder {

    private Response response;
    private Postman postman;

    public ResponseBuilder(Request request) {
        response = request.buildResponse();
    }

    public ResponseBuilder client(Postman postman) {
        this.postman = postman;
        return this;
    }

    private Response getResponse() {
        return response;
    }

    public ResponseBuilder addParam(String key, Object value) {
        getResponse().addParam(key, value);
        return this;
    }

    public void execute() {
        execute(null);
    }

    public void execute(RequestCallback callback) {
        RequestCall call = SyncSktClient.getCallBuilder()
                .client(postman)
                .callback(callback)
                .data(getResponse())
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
                .data(getResponse())
                .build();
        SyncSktClient.getInstance().executeBlocked(call);
    }
}
