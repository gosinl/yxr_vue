package com.yxr.API.response;

public enum RespCode {
    SUCCESS(0, "success"),
    AUTHENTICATION_NOPASS(1, "认证不通过"),
    FAIL(2, "请求失败"),
    RISK(5, "异常风控,请联系客服处理"),
    EXCEPTION_VALIDATION(10, "参数异常"),

    //500-699,业务代码
    INTECEPTOR_WHITE_IP(600, "IP没有权限访问该功能"),
    NULL_PARAMETERS(650, "参数不能为空"),

    //700-750，风控代码

    CUSTOM(999);

    private int code;
    private String msg;

    RespCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    RespCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
