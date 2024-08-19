package com.yxr.API.poJo;

import lombok.Data;

@Data
public class ResponseMedicalStock {
    private String msgSignature; //签名
    private String timestamp;  //时间戳
    private String nonce; //随机数
    private String echostr; //返回结果的加密数据
        //echostr所包含的参数类型以及说明:
    private String itemCode; //Erp药品编码
    private String insuItemName; //药品医保项目名称
    private String insuItemCode; //药品医保项目编码
    private String salePrice; //售价
    private String kcl; //库存量
    private String factory; //生产厂家
    private String nationalNo; //国药准字
    private String spec;  //规格
    private String barCode; //条形码
}
