package com.yxr.API.poJo;

import lombok.Data;

@Data
public class RequestMedicalStock {
    private String corpid;
    private String msgSignature;
    private String timestamp;
    private String nonce;
    private String echostr;

    private String drugStoreNo;
    private String insuItemCode;

}

