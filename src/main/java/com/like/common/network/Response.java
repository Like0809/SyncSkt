package com.like.common.network;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public final class Response extends DataPackage {

    Response() {
        this.type = RESPONSE;
    }

    Response(int id) {
        this.id = id;
        this.type = RESPONSE;
    }

}
