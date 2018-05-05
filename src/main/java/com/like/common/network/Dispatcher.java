package com.like.common.network;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public class Dispatcher {

    private OnNewRequestListener mOnNewRequestListener;

    public void setOnDispatcherListener(OnNewRequestListener mOnNewRequestListener) {
        this.mOnNewRequestListener = mOnNewRequestListener;
    }

    Dispatcher() {
    }

    public void newMessage(Postman postman, DataPackage dataPackage) {
        SyncSktClient.getInstance().getExecutorService().execute(new MessageHandler(postman, dataPackage));
    }

    private class MessageHandler implements Runnable {

        private Postman postman;
        private DataPackage dataPackage;

        public MessageHandler(Postman postman, DataPackage dataPackage) {
            this.postman = postman;
            this.dataPackage = dataPackage;
        }

        @Override
        public void run() {
            if (dataPackage.getType() == DataPackage.RESPONSE) {
                SyncSktClient.getInstance().onResponse((Response) dataPackage);
            }
            if (dataPackage.getType() == DataPackage.REQUEST) {
                if (mOnNewRequestListener != null) {
                    mOnNewRequestListener.newRequest(postman, (Request) dataPackage);
                }
            }
        }
    }
}
