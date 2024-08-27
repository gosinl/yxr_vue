package com.yxr.web.controller.API;


import com.yxr.business.service.IGoodsDocService;
import com.yxr.common.annotation.Log;
import com.yxr.common.core.domain.AjaxResult;
import com.yxr.common.enums.BusinessType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("yxr/api/shanxiPICC")
public class ShanxiPICCController {
    @Autowired
    private IGoodsDocService goodsDocService;


    //查看库存
    @PostMapping("medicalStock")
    public AjaxResult medicalStock(@RequestBody String jsonData, HttpServletRequest request) {
        AjaxResult result = goodsDocService.medicalStock(jsonData);
        return result;
        //return AjaxResult.success(jsonObject);
    }

    //买药
    //@Log(title = "陕西PICC", businessType = BusinessType.OTHER)  外部未登录的接口不能用@LOG
    @PostMapping("medicalBuy")
    public AjaxResult medicalBuy(@RequestBody String jsonData, HttpServletRequest request) {
        AjaxResult result = goodsDocService.medicalBuy(jsonData);
        return result;
        //return AjaxResult.success(jsonObject);
    }

    //退药
    //@Log(title = "陕西PICC", businessType = BusinessType.OTHER)
    @PostMapping("medicalRefund")
    public AjaxResult medicalRefund(@RequestBody String jsonData, HttpServletRequest request) {
        AjaxResult result = goodsDocService.medicalRefund(jsonData);
        return result;
        //return AjaxResult.success(jsonObject);
    }
}
