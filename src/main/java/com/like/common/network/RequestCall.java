package com.like.common.network;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Like
 */

@SuppressWarnings({"ALL"})
public class RequestCall implements Runnable {
    private int id;
    private Postman mClient;
    private DataPackage mDataPackage;
    private CountDownLatch mLatch;
    private long mTimeout;
    private RequestCallback mCallback;
    private boolean mIsAsync;
    private String mResponse;
    private Throwable mError;
    private boolean mIsSuccess;

    private RequestCall(Postman client, DataPackage dataPackage, long timeout, RequestCallback callback, boolean isAsync) {
        if (dataPackage == null) {
            dataPackage = new DataPackage();
        }
        this.id = dataPackage.getId();
        this.mClient = client;
        this.mDataPackage = dataPackage;
        this.mTimeout = timeout;
        this.mCallback = callback;
        this.mIsAsync = isAsync;
    }

    @Override
    public void run() {
        if (!mIsAsync) {
            mLatch = new CountDownLatch(1);
        }
        try {
            mClient.send(mDataPackage);
        } catch (Exception e) {
            e.printStackTrace();
            mError = new RuntimeException("发送失败！");
            mLatch.countDown();
        }
        if (mError != null) {
            if (mCallback != null) {
                mCallback.onFailed(mError);
            }
        } else {
            try {
                if (mLatch != null) {
                    if (!mLatch.await(mTimeout, TimeUnit.MILLISECONDS)) {
                        if (mCallback != null) {
                            mCallback.onFailed(new RuntimeException("请求超时！Id:" + getId() + "data:" + mDataPackage.toString()));
                        }
                    } else {
                        if (mIsSuccess) {
                            if (mCallback != null) {
                                mCallback.onSuccess(mResponse);
                            }
                        } else {
                            if (mCallback != null && mError != null) {
                                mCallback.onFailed(mError);
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SyncSktClient.getInstance().remove(this);
    }

    public void onResponse(String response) {
        mIsSuccess = true;
        mResponse = response;
        mLatch.countDown();
    }

    public void shutdown() {
        mIsSuccess = false;
        mError = new RuntimeException("请求被终止！");
        mLatch.countDown();
    }

    public static class RequestCallBuilder {
        private Postman client;
        private DataPackage dataPackage;
        private long timeout;
        private RequestCallback callback;
        private boolean isAsync;

        public RequestCallBuilder() {
            timeout = 8000;
            isAsync = false;
        }

        public RequestCall build() {
            if (client == null) {
                throw new RuntimeException("please set up a Postman !");
            }
            return new RequestCall(client, dataPackage, timeout, callback, isAsync);
        }

        public RequestCallBuilder async(boolean isAsync) {
            this.isAsync = isAsync;
            return this;
        }

        public RequestCallBuilder async() {
            this.isAsync = true;
            return this;
        }

        public RequestCallBuilder client(Postman client) {
            this.client = client;
            return this;
        }

        public RequestCallBuilder data(DataPackage dataPackage) {
            this.dataPackage = dataPackage;
            return this;
        }

        public RequestCallBuilder timeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public RequestCallBuilder callback(RequestCallback callback) {
            this.callback = callback;
            return this;
        }
    }

    public int getId() {
        return id;
    }

    public Postman getClient() {
        return mClient;
    }

    public DataPackage getDataPackage() {
        return mDataPackage;
    }

    public CountDownLatch getLatch() {
        return mLatch;
    }

    public long getTimeout() {
        return mTimeout;
    }

    public RequestCallback getCallback() {
        return mCallback;
    }

    @Override
    public String toString() {
        return "RequestCall{" +
                "id=" + id +
                ", mDataPackage=" + mDataPackage.toString() +
                '}';
    }
}
