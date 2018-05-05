package com.like.common.network;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public final class Request extends DataPackage {
    public Request() {
        super();
        type = REQUEST;
    }

    public Response buildResponse() {
        return new Response(id);
    }
}
