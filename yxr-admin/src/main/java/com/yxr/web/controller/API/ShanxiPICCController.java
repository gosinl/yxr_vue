package com.yxr.web.controller.API;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yxr.API.poJo.ResponseMedicalStock;
import com.yxr.API.response.RespEntity;
import com.yxr.API.response.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("shanxiPICC")
public class ShanxiPICCController {

    @PostMapping("savePayOrder")
    public RespEntity uploadJointDebt(String json) {
        if (StringUtils.isEmpty(json)) {
            return RespUtil.nullParameter();
        }
        JSONObject jsonObject = JSONUtil.parseObj(json);
        jsonObject.getStr("name");




        return RespUtil.success(jsonObject);
    }

}
