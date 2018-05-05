package com.like.common.network;

/**
 * @author Administrator
 */
@SuppressWarnings("ALL")
public interface RequestHandler {

    /**
     * 处理请求
     *
     * @param request 请求
     * @param postman 发送者
     * @return 响应
     */
    Response handlerRequest(Postman postman, Request request);
}
