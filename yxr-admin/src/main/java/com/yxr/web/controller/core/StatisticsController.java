package com.yxr.web.controller.core;


import com.yxr.common.core.controller.BaseController;
import com.yxr.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/core/statistics")
public class StatisticsController extends BaseController {



    //查看库存
    @PostMapping("medicalStock")
    public AjaxResult medicalStock(@RequestBody String jsonData, HttpServletRequest request) {
        //AjaxResult result = goodsDocService.medicalStock(jsonData);
        return null;
        //return AjaxResult.success(jsonObject);
    }

    //买药
    //@Log(title = "陕西PICC", businessType = BusinessType.OTHER)  外部未登录的接口不能用@LOG
    @PostMapping("medicalBuy")
    public AjaxResult medicalBuy(@RequestBody String jsonData, HttpServletRequest request) {
        //AjaxResult result = goodsDocService.medicalBuy(jsonData);
        return null;
        //return AjaxResult.success(jsonObject);
    }

}
