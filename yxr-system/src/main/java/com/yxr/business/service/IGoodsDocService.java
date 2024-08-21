package com.yxr.business.service;

import com.yxr.business.domain.GoodsDoc;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxr.common.core.domain.AjaxResult;
import com.yxr.core.domain.SysStudent;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * @since 2024-08-19
 */
public interface IGoodsDocService extends IService<GoodsDoc> {


    /**
     * 查询商品库存信息
     *
     */
    public AjaxResult medicalStock(String json);
}
