package com.yxr.web.controller.API;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yxr.API.ShanxiPICC;
import com.yxr.API.poJo.AesException;
import com.yxr.business.service.IGoodsDocService;
import com.yxr.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

}
