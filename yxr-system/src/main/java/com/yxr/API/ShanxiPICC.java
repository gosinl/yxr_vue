package com.yxr.API;


import ch.qos.logback.core.encoder.EchoEncoder;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yxr.API.utils.RandomUtil;
import com.yxr.API.utils.WXBizJsonMsgCrypt;
import com.yxr.common.utils.ip.IpUtils;
import com.yxr.core.domain.YxrApiShanxipicc;
import com.yxr.core.mapper.YxrApiShanxipiccMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ShanxiPICC {

    public static final String token = "yongxinrenruanjian";
    public static final String corpid = "yongxinrenruanjian01";
    public static final String encodingAesKey = "akyxrp6109DfkKyFGFTJgBS5bod3qswBhgFgD0warAK";

    //{"echostr":"df1pZY5H90x0k4ETuwnraa02ggAO9kBGPRrxOKN0rqeGZMRR7qoTlQRbP2M2IkBvNhTwN7dtpoVueJrh+FzVKxqN7MWeb0Cknbgp5B1GR/EgRvgYkgxAJrE8n7H/OGcRQbcxImBhsS6D8rlNDZY5Mj1QnRoKkqZ+PPpyNj3a+vA=","msgsignature":"eb32af104c55e26bf79526bd3de192ec393e06d3","timestamp":"1724143363705","nonce":"1367198282","corpid":"duijieruanjianpeizhi01"}
    public JSONObject medicalDecryption(JSONObject jsonObject) throws Exception{
        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(token, encodingAesKey, corpid);
        String dpResult = wxcpt.DecryptMsg(jsonObject.get("msgSignature").toString(),jsonObject.get("timestamp").toString(),jsonObject.get("nonce").toString(),jsonObject.get("echostr").toString());
        log.info("解密：" + dpResult);
        JSONObject echostrJson = JSONUtil.parseObj(dpResult);
        return echostrJson;
    }

    public String medicalEncryption(String jsonString) throws Exception{
        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(token, encodingAesKey, corpid);
        String sReqTimeStamp = Long.toString(System.currentTimeMillis());
        String sReqNonce = RandomUtil.randomNumbers(10);
        String data = wxcpt.EncryptMsg(jsonString, sReqTimeStamp, sReqNonce, corpid);
        log.info("加密：" + data);
        return data;
    }

    //陕西人保财险门慢药店进销存接口文档v1.2
    public static void main(String[] args) throws Exception{

      /* String token = "ylhaidiansystem";
        String corpid = "ylhaidiansystem01";
        String encodingAesKey = "dyk7kdXDs7DfkJsFGFTJgBS5bod9kswBheFcD0warBY";

       加密参数： {"echostr":"8Se4UFzRTxRzpAqDLD3W9jxh+b01CG6b7kkMoXEA87uE+okSY/f9qQ+tunzvfVeSzvFq0rXn8Bc2mwlWwcgMxZZ5enHa2gr+8tNmByM/4D67mCcAmtWQ5igxN+DMix+iKhiAWtmahiBXOiGnp5+zEUAZKumE6viNsj76IFKmbGY=","msgsignature":"686135f54d19c061f2850b743ecc42d9ba677b4f","timestamp":"1724137726418","nonce":"0575526621"}
        解密参数： {"drugStoreNo":"P6103010101","medicCode":"XB01ACA056A012010104990"}*/
        try {

        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(token, encodingAesKey, corpid);
        String sRespData = "{\"drugStoreNo\":\"P61092900003\",\"feedetlNo\":\"P61092900003202408231511580096\"}";
       // String sRespData = "{\"drugStoreNo\":\"P61092900003\",\"insuItemCode\":\"XD07BBQ085V003010100443\"}";

            //RandomUtil.randomNumbers(10)采用hutool 的随机生成十位数
            //Long.toString(System.currentTimeMillis()) 生成时间戳
            //sRespData  将下方参数说明以json形式生成
            String timeStamp = Long.toString(System.currentTimeMillis());
            String nonce = RandomUtil.randomNumbers(10);
            String sEncryptMsg = wxcpt.EncryptMsg(sRespData, timeStamp, nonce);
            System.out.println("加密参数： " + sEncryptMsg);

            /*String jie = "{\"echostr\":\"/nG2mmAZEo951Yg0dmL58ihI39+eoczjQAZBLUiScF3KAL2p14S" +
                    "/0VjCe1UV0SUxNpeGO8pjgKcj6DI1jcrfdnBSVJHnOTVrvf8npCJgCF1AX9rnfm1leti6WXFqc1dJmENL3YiT3ebwPAxuDZeeH5lIWOmJZSAajL4cCU0Be5J2IYi8eA" +
                    "+8j1XQPNJH08O6UcM7M2QQtTL4fUfBBdKwhzFLv1lcJeIH9oFOJ1j7x0Ct75RIfBo4sluSOMq7YHgHWEGzxeNbTLPk1CllFI3RZ1JAOHGSk1zALvsv" +
                    "+MU9iVsW5E7q8qjJgxI0NCVpKSuZbp0FdUBpERTzjTJpqRpV1OqP5WNtUSNgkL0ymL9an3k9k3OcfhoUTaerZ5td6Ad4Z1Eo0aBAK0SheE3eaM5emmKeJ/iJPH8mJRh9HAefOq3hEV3PG68mq1hDArmo7wVgpXRwH3mMb1yeRCC" +
                    "+Lw5GIUDyuJh93XZIEn4tJhZrF/0tbTuPJ5vkiil+MB+R+Ss88lW6IohUi+ymFuwOJ/oqFJ1jak4Dw8VCa9+pkY5USwy+9+kMygIK9V8JlOrUk1jv5J9LSuHVmzNeBN5LXmncwR2CD0cOEsaq4dHHjicAbdkCkBJXvLpGGnNMu9xJR6ZYTVVSbMGOU4" +
                    "/nyzugOEExpyVl3Juz+Vd1rPQ2WlQbFLw3L6VtullptCY90JpsczI5cVqjT7tgKud6YmE6YzBIzQ==\",\"msgSignature\":\"40da5168010a30a2041c060e1aaac28e419cf7a4\",\"timestamp\":\"1724202918121\"," +
                    "\"nonce\":\"1913051095\",\"corpid\":\"duijieruanjianpeizhi01\"}";
            JSONObject o = JSONUtil.parseObj(jie);*/

            JSONObject o = JSONUtil.parseObj(sEncryptMsg);
            o.put("corpid",corpid);
            System.out.println(o);


            String dpResult = wxcpt.DecryptMsg(o.get("msgSignature").toString(),o.get("timestamp").toString(),o.get("nonce").toString(),o.get("echostr").toString());
            System.out.println("解密参数： " +dpResult);

        } catch (Exception e) {
            e.printStackTrace();
        }



    //


       /* String test = "{\"code\":\"200\",\"message\":\"\",\"data\":[{\"itemCode\":\"16761\",\"insuItemCode\":\"XC08CAZ067A001010205491\",\"insuItemName\":\"感冒灵颗粒\",\"saleprice\":\"28\",\"kcl\":\"100\"," +
                "\"factory\":\"北京法莫斯达制药有限公司\",\"nationalNO\":\"国药准字Z44022063\",\"barCode\":\"69340547711251\"}]}";


        String stock = "{\"msg\":\"\",\"code\":200,\"data\":{\"echostr\":\"8DKMJWJUfcyVrrVnToz8M5b/y9TPO8CeuUo4y1tAzixHMwT1d5w0x2bvHKoQecwVb6ZrvlubhV0aUl+OmOPA7A==\"," +
                "\"msgSignature\":\"85aba33739c55688f7020aec2b986c98d0b401bb\",\"timestamp\":\"1724200534249\",\"nonce\":\"3275175120\",\"corpid\":\"duijieruanjianpeizhi01\"}}";
        JSONObject respObj = JSONUtil.parseObj(stock);
        *//*JSONArray data = respObj.getJSONArray("data");
        String strData = data.toString();
        strData = strData.replace("\r\n","");

        String sReqTimeStamp = Long.toString(System.currentTimeMillis());
        String sReqNonce = RandomUtil.randomNumbers(10);
*//*
        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(token, encodingAesKey, corpid);

        //String encMsg = wxcpt.EncryptMsg(strData, sReqTimeStamp, sReqNonce, corpid);
        //System.out.println("加密结果：" + encMsg);

        *//*JSONObject encObj = JSONUtil.parseObj(encMsg);
        respObj.put("data", encObj);
        String response = encObj.toString();
        System.out.println("返回：" + response);*//*

        JSONObject data = respObj.getJSONObject("data");
        String sMsg = wxcpt.DecryptMsg(data.getStr("msgSignature"), data.getStr("timestamp"), data.getStr("nonce"),  data.getStr("echostr"));
        System.out.println("解密：" + sMsg);*/
    }

}
