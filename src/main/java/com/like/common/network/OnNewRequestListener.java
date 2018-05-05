package com.like.common.network;

/**
 * @author Like
 */
public interface OnNewRequestListener {
    /**
     * 用于广播一个新的请求
     *
     * @param postman     请求发送工具
     * @param request 请求数据
     */
    void newRequest(Postman postman, Request request);
}
