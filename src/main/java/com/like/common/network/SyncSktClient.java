package com.like.common.network;

import com.like.common.utils.JsonUtil;

import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Like
 */

@SuppressWarnings({"ALL"})
public class SyncSktClient {
    private volatile static SyncSktClient mInstance;
    private ExecutorService mExecutorService;
    private Dispatcher mDispatcher;
    private Hashtable<Integer, RequestCall> mRequestCalls;

    private SyncSktClient(ExecutorService executorService) {
        mDispatcher = new Dispatcher();
        mRequestCalls = new Hashtable<>();
        if (executorService != null) {
            mExecutorService = executorService;
        } else {
            mExecutorService = Executors.newFixedThreadPool(5000);
        }
    }

    public static SyncSktClient initClient(ExecutorService executorService) {
        if (mInstance == null) {
            synchronized (SyncSktClient.class) {
                if (mInstance == null) {
                    mInstance = new SyncSktClient(executorService);
                }
            }
        }
        return mInstance;
    }

    public static SyncSktClient getInstance() {
        return initClient(null);
    }

    public void shutdown(RequestCall call) {
        call.shutdown();
        mRequestCalls.remove(call.getId());
    }

    public void remove(RequestCall call) {
        mRequestCalls.remove(call.getId());
    }

    void execute(RequestCall call) {
        if (call != null) {
            if (call.getDataPackage() != null && !call.getDataPackage().isResponse()) {
                mRequestCalls.put(call.getId(), call);
            }
            mExecutorService.execute(call);
        }
    }

    void executeBlocked(RequestCall call) {
        if (call != null) {
            if (call.getDataPackage() != null && !call.getDataPackage().isResponse()) {
                mRequestCalls.put(call.getId(), call);
            }
            call.run();
        }
    }

    public void onResponse(Response response) {
        RequestCall call = getCallById(response.getId());
        if (call != null) {
            call.onResponse(JsonUtil.toJson(response.getParams()));
        }
    }

    public void newDataPackage(Postman postman, String message) {
        DataPackage dataPackage = JsonUtil.parse(message, DataPackage.class);
        if (dataPackage.isResponse()) {
            dataPackage = JsonUtil.parse(message, Response.class);
        } else {
            dataPackage = JsonUtil.parse(message, Request.class);
        }
        mDispatcher.newMessage(postman, dataPackage);
    }

    RequestCall getCallById(int id) {
        return mRequestCalls.get(id);
    }

    public void setRequestHandler(RequestHandler handler) {
        setOnNewRequestListener(new RequestListener(handler));
    }

    private void setOnNewRequestListener(OnNewRequestListener mOnNewRequestListener) {
        this.mDispatcher.setOnDispatcherListener(mOnNewRequestListener);
    }

    public ExecutorService getExecutorService() {
        return mExecutorService;
    }

    public static RequestCall.RequestCallBuilder getCallBuilder() {
        return new RequestCall.RequestCallBuilder();
    }

    public static RequestBuilder request() {
        return new RequestBuilder();
    }
}
