package com.example.supernova.pfe.tasks;

public class Response {
    int code;
    String body;

    public Response(int code, String body){
        this.code = code;
        this.body = body;
    }

    public String getBody() {
        return body;
    }
    public Response setBody(String body) {
        this.body = body;
        return this;
    }
    public int getCode() {
        return code;
    }
    public Response setCode(int code) {
        this.code = code;
        return this;
    }
}
