package com.yxr.API;


import ch.qos.logback.core.encoder.EchoEncoder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yxr.API.utils.RandomUtil;
import com.yxr.API.utils.WXBizJsonMsgCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShanxiPICC {

    public static final String token = "duijieruanjianpeizhi";
    public static final String corpid = "duijieruanjianpeizhi01";
    public static final String encodingAesKey = "ayk5kdXDs7DfkKyFGFTJgBS5bod3qswBhgFgD0warBY";
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

       /* 加密结果：{"echostr":"tV2MUicQ9/IXaZOYt/g9qmBBmW8dJ4G0iBXajIzx7kePdzjjmEoRhjYj/gW6jHkErAjzZIajms5Fd85Iy4nofknmKIPRrYrGJvh9eeg73Sg25heGY9+2v6WpRsg2E/3zV3Hc6ESHy62WW/lk2Ii2LzJbaKzvjuDjj/VZbd8MKcvprFsEm941Nn89TJuKMQuxKg7TVzfPRrNzmHEBkJjB9c1YMf1C4zp4T4k7l8b50SST/DDYs1ntrY/dpnUe/BzpP/NYS+FIxjwq7h1QNOB7yRlHQpM/ZfVh7NrOlO/ybfgcRivkp/H9BU7qVhr3ksxXcK3oNFBUfWfNfe4szvvVsflgUW916Kpx7qQGCCSe272aHAfy/VYytPVJypxYrVVX","msgSignature":"6bb6d488c48e548b00a7199ffa4a128f7ef88dff","timestamp":"1724031836787","nonce":"0605559019","corpid":"duijieruanjianpeizhi01"}
返回：{"code":"200","message":"","data":{"echostr":"tV2MUicQ9/IXaZOYt/g9qmBBmW8dJ4G0iBXajIzx7kePdzjjmEoRhjYj/gW6jHkErAjzZIajms5Fd85Iy4nofknmKIPRrYrGJvh9eeg73Sg25heGY9+2v6WpRsg2E/3zV3Hc6ESHy62WW/lk2Ii2LzJbaKzvjuDjj/VZbd8MKcvprFsEm941Nn89TJuKMQuxKg7TVzfPRrNzmHEBkJjB9c1YMf1C4zp4T4k7l8b50SST/DDYs1ntrY/dpnUe/BzpP/NYS+FIxjwq7h1QNOB7yRlHQpM/ZfVh7NrOlO/ybfgcRivkp/H9BU7qVhr3ksxXcK3oNFBUfWfNfe4szvvVsflgUW916Kpx7qQGCCSe272aHAfy/VYytPVJypxYrVVX","msgSignature":"6bb6d488c48e548b00a7199ffa4a128f7ef88dff","timestamp":"1724031836787","nonce":"0605559019","corpid":"duijieruanjianpeizhi01"}}
解密：[{"itemCode":"16761","insuItemCode":"XC08CAZ067A001010205491","insuItemName":"感冒灵颗粒","saleprice":"28","kcl":"100","factory":"北京法莫斯达制药有限公司","nationalNO":"国药准字Z44022063","barCode":"69340547711251"}]
*/




        String testData = "{\"code\":\"200\",\"message\":\"\",\"data\":{\"echostr\":\"tV2MUicQ9/IXaZOYt/g9qmBBmW8dJ4G0iBXajIzx7kePdzjjmEoRhjYj/gW6jHkErAjzZIajms5Fd85Iy4nofknmKIPRrYrGJvh9eeg73Sg25heGY9+2v6WpRsg2E" +
                "/3zV3Hc6ESHy62WW/lk2Ii2LzJbaKzvjuDjj/VZbd8MKcvprFsEm941Nn89TJuKMQuxKg7TVzfPRrNzmHEBkJjB9c1YMf1C4zp4T4k7l8b50SST/DDYs1ntrY/dpnUe/BzpP/NYS+FIxjwq7h1QNOB7yRlHQpM/ZfVh7NrOlO/ybfgcRivkp" +
                "/H9BU7qVhr3ksxXcK3oNFBUfWfNfe4szvvVsflgUW916Kpx7qQGCCSe272aHAfy/VYytPVJypxYrVVX\",\"msgSignature\":\"6bb6d488c48e548b00a7199ffa4a128f7ef88dff\",\"timestamp\":\"1724031836787\"," +
                "\"nonce\":\"0605559019\",\"corpid\":\"duijieruanjianpeizhi01\"}}";
        JSONObject jsonObject = JSONUtil.parseObj(testData);
        System.out.println("json：" + jsonObject);
        JSONObject dataJson = jsonObject.getJSONObject("data");
        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(token, encodingAesKey, corpid);
        String sMsg = wxcpt.DecryptMsg(dataJson.getStr("msgSignature"), dataJson.getStr("timestamp"), dataJson.getStr("nonce"), dataJson.getStr("echostr"));
        System.out.println("解密：" + sMsg);
        JSONArray array = JSONUtil.parseArray(sMsg);
        System.out.println("array：" + array.get(0));
        //{"itemCode":"16761","insuItemCode":"XC08CAZ067A001010205491","insuItemName":"感冒灵颗粒","saleprice":"28","kcl":"100","factory":"北京法莫斯达制药有限公司","nationalNO":"国药准字Z44022063","barCode":"69340547711251"}
        ArrayList responseEchostr = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            JSONObject jo = array.getJSONObject(i);
            String drugStoreNo = jo.getStr("drugStoreNo"); //药品医保项目名称
            String insuItemCode = jo.getStr("insuItemCode"); //药品医保项目编码
            //去数据库查找数据
            Map responseMap = new HashMap();
            responseMap.put("itemCode","");  //Erp药品编码
            responseMap.put("insuItemCode",""); //药品医保项目名称
            responseMap.put("insuItemName","");  //药品医保项目编码
            responseMap.put("saleprice",""); //售价
            responseMap.put("kcl","");  //库存量
            responseMap.put("factory",""); //生产厂家
            responseMap.put("nationalNO","");//国药准字
            responseMap.put("spec","");  //规格
            responseMap.put("barCode",""); //条形码
            responseEchostr.add(JSONUtil.parseObj(responseMap));
        }
        String sReqTimeStamp = Long.toString(System.currentTimeMillis());
        String sReqNonce = RandomUtil.randomNumbers(10);
        String dataEchostr = wxcpt.EncryptMsg(responseEchostr.toString(), sReqTimeStamp, sReqNonce, corpid);
        Map resultDataMap = new HashMap();
        resultDataMap.put("msgSignature",dataJson.getStr("msgSignature"));
        resultDataMap.put("timestamp",sReqTimeStamp);
        resultDataMap.put("nonce",sReqNonce);
        resultDataMap.put("echostr",dataEchostr);
        System.out.println(resultDataMap.toString());


        /*String token = "duijieruanjianpeizhi";
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
        System.out.println("解密：" + sMsg);*/
    }

}
