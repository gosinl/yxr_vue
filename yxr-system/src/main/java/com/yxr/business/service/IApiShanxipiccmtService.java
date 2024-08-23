package com.yxr.business.service;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yxr.business.domain.ApiShanxipiccmt;
import com.yxr.common.core.domain.AjaxResult;

/**
 * <p>
 * shanxi接口汇总表 服务类
 * </p>
 *
 * @author GS
 * @since 2024-08-22
 */
public interface IApiShanxipiccmtService extends IService<ApiShanxipiccmt> {

    public AjaxResult addApiShanxipicc(JSONObject json);



    public Long selectBillNo();


    public AjaxResult updateByfeedetlNo(JSONObject json);
}
