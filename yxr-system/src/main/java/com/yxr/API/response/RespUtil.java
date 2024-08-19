package com.yxr.API.response;

public class RespUtil {

    public static RespEntity success() {
        return RespUtil.success(null);
    }

    public static RespEntity authenticationNopass() {
        return new RespEntity(RespCode.AUTHENTICATION_NOPASS);
    }

    public static RespEntity success(Object data) {
        return new RespEntity(RespCode.SUCCESS, data);
    }

    public static RespEntity nullParameter() {
        return new RespEntity(RespCode.NULL_PARAMETERS);
    }

    public static RespEntity custom(String message) {
        return new RespEntity(message);
    }

    public static RespEntity whiteIp() {
        return new RespEntity(RespCode.INTECEPTOR_WHITE_IP);
    }

    public static RespEntity risk() {
        return new RespEntity(RespCode.RISK);
    }

    public static RespEntity fail() {
        return new RespEntity(RespCode.FAIL);
    }
}
