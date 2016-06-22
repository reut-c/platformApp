package com.platform.supersonic.platformapplication;

/**
 * Created by chen.l on 22/06/16.
 */
public class Request {
    private String url;
    private String auth;
    private String method;

    public Request(String url, String auth, String method) {
        this.url = url;
        this.auth = auth;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public String getAuth() {
        return auth;
    }

    public String getMethod() {
        return method;
    }
}
