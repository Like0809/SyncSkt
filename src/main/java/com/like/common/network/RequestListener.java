package com.like.common.network;

import com.like.common.utils.Log;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public class RequestListener implements OnNewRequestListener {
    private RequestHandler handler;

    public RequestListener(RequestHandler handler) {
        this.handler = handler;
    }

    @Override
    public void newRequest(Postman postman, Request request) {

        Response response = handler.handlerRequest(postman, request);

        RequestCall call = SyncSktClient.getCallBuilder()
                .client(postman)
                .data(response)
                .async()
                .callback(new RequestCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.withTime("response success:" + response);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        Log.withTime("response failed:" + throwable.getMessage());
                    }
                })
                .build();

        SyncSktClient.getInstance().execute(call);
    }
}
