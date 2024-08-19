package com.yxr.API.response;

public class RespEntity<T> {

    private int code;
    private String msg;
    private T data;


    public RespEntity(RespCode respCode) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
    }

    public RespEntity(RespCode respCode, T data) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
        this.data = data;
    }

    public RespEntity(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RespEntity(String msg) {
        this.code = RespCode.CUSTOM.getCode();
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
