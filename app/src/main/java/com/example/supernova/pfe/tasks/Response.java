package com.example.supernova.pfe.tasks;

public class Response {
    private int code;
    private String body;

    public Response(int code, String body){
        this.code = code;
        this.body = body;
    }

    public Response(){ this.body = null;}

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

    public static boolean isSuccess(int code){
        return !(code < 200 || code > 399);
    }

    public boolean isSuccess(){
        return !(code < 200 || code > 399);
    }
}
