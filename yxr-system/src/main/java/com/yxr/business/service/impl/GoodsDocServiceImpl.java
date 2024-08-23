package com.yxr.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxr.API.ShanxiPICC;
import com.yxr.API.poJo.AesException;
import com.yxr.API.poJo.ResponseMedicalStock;
import com.yxr.business.domain.ApiShanxipiccdt;
import com.yxr.business.domain.ApiShanxipiccmt;
import com.yxr.business.domain.GoodsDoc;
import com.yxr.business.mapper.GoodsDocMapper;
import com.yxr.business.service.IApiShanxipiccmtService;
import com.yxr.business.service.IGoodsDocService;
import com.yxr.common.core.domain.AjaxResult;
import com.yxr.common.utils.ip.IpUtils;
import com.yxr.core.domain.YxrApiShanxipicc;
import com.yxr.core.service.IYxrApiShanxipiccService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 作者
 * @since 2024-08-19
 */
@Service
public class GoodsDocServiceImpl extends ServiceImpl<GoodsDocMapper, GoodsDoc> implements IGoodsDocService {
    @Autowired
    private GoodsDocMapper goodsDocMapper;

    @Autowired
    ShanxiPICC shanxiPICC;

    @Autowired
    private IYxrApiShanxipiccService yxrApiShanxipiccService;

    @Autowired
    private IApiShanxipiccmtService iApiShanxipiccmtService;

    //@Transactional
    @Override
    public AjaxResult medicalStock(String json) {
        if (StringUtils.isEmpty(json)) {
            return AjaxResult.error(AesException.ParseJsonError,new AesException().getMessage(AesException.ParseJsonError));
        }

        JSONObject jsonObject = JSONUtil.parseObj(json);
        //{"msg":"","code":"200","data":{"echostr":"ZBObIZqmS5nD18aA+Bzw96ByI68KUE4/cW2r1/JXECgOSPuDtlgjN33+KRBjXrVQwnVkyqWIKg80zV7bM48t6GZovaxAcW9Dgk/O0LCSeLgM/5xGlzyjZF1c5nhHv48C43355xuKc6oayJiVwtSs5YUi3dJOj5GjSTuLxDmbASQ=","msgsignature":"1a0dfcbcc24c9211e3eab83140c56608f7f005e5","timestamp":"1724139113088","nonce":"4009422517"}}
        if(!jsonObject.getStr("corpid").equals(ShanxiPICC.corpid)){
            return AjaxResult.error(AesException.ValidateCorpidError,new AesException().getMessage(AesException.ValidateCorpidError));
        }

        try {
            //{
            //"drugStoreNo":"P6103010101",  药店编码
            //"insuItemCode":"XB01ACA056A012010104990" 药品医保目录编码
            //}
            JSONObject  echostrJson = shanxiPICC.medicalDecryption(jsonObject);
            //查数据返回，下面要接入库，表还没创建，记得创建
            Map mapSql = new HashMap();
            mapSql.put("insuItemCode",echostrJson.getStr("insuItemCode"));
            mapSql.put("drugStoreNo",echostrJson.getStr("drugStoreNo"));

            //根据时间戳判断是否添加过
            YxrApiShanxipicc yxrApiShanxipicc = new YxrApiShanxipicc();
            yxrApiShanxipicc = yxrApiShanxipiccService.selectYxrApiShanxipiccByTimestamp(jsonObject.getStr("timestamp"));
            if(ObjectUtil.isEmpty(yxrApiShanxipicc)){
                yxrApiShanxipicc = new YxrApiShanxipicc();
            }
            yxrApiShanxipicc.setIp(IpUtils.getIpAddr());
            yxrApiShanxipicc.setInterfaceType(1);
            yxrApiShanxipicc.setStatus(0);
            yxrApiShanxipicc.setMsgSignature(jsonObject.getStr("msgSignature"));
            yxrApiShanxipicc.setTimestamp(jsonObject.getStr("timestamp"));
            yxrApiShanxipicc.setRequestBody(jsonObject.put("echostr",echostrJson).toString());
            Long yxrApiShanxipiccId;

            if(ObjectUtil.isEmpty(yxrApiShanxipicc.getId())){
                yxrApiShanxipiccId = yxrApiShanxipiccService.insertYxrApiShanxipiccReturenId(yxrApiShanxipicc);
            }else {
                yxrApiShanxipiccService.updateYxrApiShanxipicc(yxrApiShanxipicc);
                yxrApiShanxipiccId = yxrApiShanxipicc.getId();
            }

            List<ResponseMedicalStock> responseMedicalStock = goodsDocMapper.selectByCode(mapSql);
            //[{
            //"itemCode":"1001",
            //"insuItemCode":"XB01ACA056A012010104990",
            //	"insuItemName":"阿司匹林肠溶片",
            //	"salePrice":300.25,
            //	"kcl":100
            //}
            //] 返回格式为array
            ArrayList<ResponseMedicalStock> list = CollUtil.newArrayList(responseMedicalStock);
            JSONArray medicalStockList = JSONUtil.parseArray(list);

            YxrApiShanxipicc yxrApiShanxipiccUpdate = new YxrApiShanxipicc();
            yxrApiShanxipiccUpdate.setId(yxrApiShanxipiccId);
            yxrApiShanxipiccUpdate.setResponseBody(medicalStockList.toString());
            yxrApiShanxipiccUpdate.setStatus(1);
            yxrApiShanxipiccService.updateYxrApiShanxipicc(yxrApiShanxipiccUpdate);
            //加密后格式
            /*{"code":"200","message":"","data":{"echostr":"tV2MUicQ9/IXaZOYt/g9qmBBmW8dJ4G0iBXajIzx7kePdzjjmEoRhjYj/gW6jHkErAjzZIajms5Fd85Iy4nofknmKIPRrYrGJvh9eeg73Sg25heGY9+2v6WpRsg2E/3zV3Hc6ESHy62WW/lk2Ii2LzJbaKzvjuDjj/VZbd8MKcvprFsEm941Nn89TJuKMQuxKg7TVzfPRrNzmHEBkJjB9c1YMf1C4zp4T4k7l8b50SST/DDYs1ntrY/dpnUe/BzpP/NYS+FIxjwq7h1QNOB7yRlHQpM/ZfVh7NrOlO/ybfgcRivkp/H9BU7qVhr3ksxXcK3oNFBUfWfNfe4szvvVsflgUW916Kpx7qQGCCSe272aHAfy/VYytPVJypxYrVVX","msgSignature":"6bb6d488c48e548b00a7199ffa4a128f7ef88dff","timestamp":"1724031836787","nonce":"0605559019","corpid":"duijieruanjianpeizhi01"}}*/
            String data = shanxiPICC.medicalEncryption(medicalStockList.toString());
            return AjaxResult.success("",new JSONObject(data));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public AjaxResult medicalBuy(String json) {
        if (StringUtils.isEmpty(json)) {
            return AjaxResult.error(AesException.ParseJsonError,new AesException().getMessage(AesException.ParseJsonError));
        }
        JSONObject jsonObject = JSONUtil.parseObj(json);
        if(!jsonObject.getStr("corpid").equals(ShanxiPICC.corpid)){
            return AjaxResult.error(AesException.ValidateCorpidError,new AesException().getMessage(AesException.ValidateCorpidError));
        }

        try {
            JSONObject  echostrJson = shanxiPICC.medicalDecryption(jsonObject);
            //根据时间戳判断是否添加过
            YxrApiShanxipicc yxrApiShanxipicc ;
            yxrApiShanxipicc = yxrApiShanxipiccService.selectYxrApiShanxipiccByTimestamp(jsonObject.getStr("timestamp"));
            if(ObjectUtil.isEmpty(yxrApiShanxipicc)){
                yxrApiShanxipicc = new YxrApiShanxipicc();
            }
            //YxrApiShanxipicc yxrApiShanxipicc = new YxrApiShanxipicc();
            yxrApiShanxipicc.setIp(IpUtils.getIpAddr());
            yxrApiShanxipicc.setInterfaceType(2);
            yxrApiShanxipicc.setStatus(1);
            yxrApiShanxipicc.setMsgSignature(jsonObject.getStr("msgSignature"));
            yxrApiShanxipicc.setTimestamp(jsonObject.getStr("timestamp"));
            yxrApiShanxipicc.setRequestBody(jsonObject.put("echostr",echostrJson).toString());
            yxrApiShanxipicc.setResponseBody("OK");
            Long yxrApiShanxipiccId;
            if(ObjectUtil.isEmpty(yxrApiShanxipicc.getId())){
                yxrApiShanxipiccId = yxrApiShanxipiccService.insertYxrApiShanxipiccReturenId(yxrApiShanxipicc);
            }else {
                yxrApiShanxipiccService.updateYxrApiShanxipicc(yxrApiShanxipicc);
                yxrApiShanxipiccId = yxrApiShanxipicc.getId();
            }
            //下面业务逻辑不清晰，暂未写完
            //先入库
            return  iApiShanxipiccmtService.addApiShanxipicc(echostrJson);
            //下账

            //减库存



        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public AjaxResult medicalRefund(String json) {
        if (StringUtils.isEmpty(json)) {
            return AjaxResult.error(AesException.ParseJsonError,new AesException().getMessage(AesException.ParseJsonError));
        }
        JSONObject jsonObject = JSONUtil.parseObj(json);
        if(!jsonObject.getStr("corpid").equals(ShanxiPICC.corpid)){
            return AjaxResult.error(AesException.ValidateCorpidError,new AesException().getMessage(AesException.ValidateCorpidError));
        }

        try {
            JSONObject  echostrJson = shanxiPICC.medicalDecryption(jsonObject);
            //根据时间戳判断是否添加过
            YxrApiShanxipicc yxrApiShanxipicc = new YxrApiShanxipicc();
            yxrApiShanxipicc = yxrApiShanxipiccService.selectYxrApiShanxipiccByTimestamp(jsonObject.getStr("timestamp"));
            if(ObjectUtil.isEmpty(yxrApiShanxipicc)){
                yxrApiShanxipicc = new YxrApiShanxipicc();
            }
            //YxrApiShanxipicc yxrApiShanxipicc = new YxrApiShanxipicc();
            yxrApiShanxipicc.setIp(IpUtils.getIpAddr());
            yxrApiShanxipicc.setInterfaceType(3);
            yxrApiShanxipicc.setStatus(1);
            yxrApiShanxipicc.setMsgSignature(jsonObject.getStr("msgSignature"));
            yxrApiShanxipicc.setTimestamp(jsonObject.getStr("timestamp"));
            yxrApiShanxipicc.setRequestBody(jsonObject.put("echostr",echostrJson).toString());
            yxrApiShanxipicc.setResponseBody("OK");
            Long yxrApiShanxipiccId;
            if(ObjectUtil.isEmpty(yxrApiShanxipicc.getId())){
                yxrApiShanxipiccId = yxrApiShanxipiccService.insertYxrApiShanxipiccReturenId(yxrApiShanxipicc);
            }else {
                yxrApiShanxipiccService.updateYxrApiShanxipicc(yxrApiShanxipicc);
                yxrApiShanxipiccId = yxrApiShanxipicc.getId();
            }
            //下面业务逻辑不清晰，暂未写完

            return iApiShanxipiccmtService.updateByfeedetlNo(echostrJson);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
