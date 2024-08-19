package com.yxr.API;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yxr.API.utils.RandomUtil;
import com.yxr.API.utils.WXBizJsonMsgCrypt;

public class ShanxiPICC {
    //陕西人保财险门慢药店进销存接口文档v1.2
    public static void main(String[] args) throws Exception{
//        String token = "duijieruanjianpeizhi";
//        String corpid = "duijieruanjianpeizhi01";
//        String encodingAesKey = "ayk5kdXDs7DfkKyFGFTJgBS5bod3qswBhgFgD0warBY";
//        try {
////        String token = "ylhaidiansystem";
////        String corpid = "ylhaidiansystem01";
////        String encodingAesKey = "dyk7kdXDs7DfkJsFGFTJgBS5bod9kswBheFcD0warBY";
//        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(token, encodingAesKey, corpid);
//        String sRespData = "{\"drugStoreNo\":\"P61082600353\",\"medicCode\":\"XCO7ABM062A001010405024\"}";
//
//            //RandomUtil.randomNumbers(10)采用hutool 的随机生成十位数
//            //Long.toString(System.currentTimeMillis()) 生成时间戳
//            //sRespData  将下方参数说明以json形式生成
//            String sEncryptMsg = wxcpt.EncryptMsg(sRespData,
//                    Long.toString(System.currentTimeMillis()), RandomUtil.randomNumbers(10));
//            System.out.println("加密参数： " + sEncryptMsg);
//
//            JSONObject o = JSONObject.parseObject(sEncryptMsg);
//            String dpResult = wxcpt.DecryptMsg(o.get("msgsignature").toString(),o.get("timestamp").toString(),o.get("nonce").toString(),o.get("echostr").toString());
//            System.out.println("解密参数： " +dpResult);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }

        String token = "duijieruanjianpeizhi";
        String corpid = "duijieruanjianpeizhi01";
        String encodingAesKey = "ayk5kdXDs7DfkKyFGFTJgBS5bod3qswBhgFgD0warBY";
        JSONObject respObj = JSONObject.parseObject("{\"code\":\"200\",\"message\":\"\",\"data\":[{\"itemCode\":\"16761\",\"insuItemCode\":\"XC08CAZ067A001010205491\",\"insuItemName\":\"感冒灵颗粒\",\"saleprice\":\"28\",\"kcl\":\"100\",\"factory\":\"北京法莫斯达制药有限公司\",\"nationalNO\":\"国药准字Z44022063\",\"barCode\":\"69340547711251\"}]}");
        JSONArray data = respObj.getJSONArray("data");
        String strData = JSONObject.toJSONString(data);
        strData = strData.replace("\r\n","");

        String sReqTimeStamp = Long.toString(System.currentTimeMillis());
        String sReqNonce = RandomUtil.randomNumbers(10);

        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(token, encodingAesKey, corpid);

        String encMsg = wxcpt.EncryptMsg(strData, sReqTimeStamp, sReqNonce, corpid);
        System.out.println("加密结果：" + encMsg);

        JSONObject encObj = JSONObject.parseObject(encMsg);
        respObj.put("data", encObj);
        String response = JSONObject.toJSONString(respObj);
        System.out.println("返回：" + response);

        String echostr = encObj.getString("echostr");
        String sMsg = wxcpt.DecryptMsg(encObj.getString("msgSignature"), encObj.getString("timestamp"), encObj.getString("nonce"), echostr);
        System.out.println("解密：" + sMsg);
    }

}
