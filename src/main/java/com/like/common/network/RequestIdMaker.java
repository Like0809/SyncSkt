package com.like.common.network;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public class RequestIdMaker {

    private static int id = 0;

    public synchronized static int getId() {
        return id++;
    }
}
