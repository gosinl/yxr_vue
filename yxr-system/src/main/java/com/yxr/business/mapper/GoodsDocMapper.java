package com.yxr.business.mapper;

import com.yxr.API.poJo.ResponseMedicalStock;
import com.yxr.business.domain.GoodsDoc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxr.common.annotation.DataSource;
import com.yxr.common.enums.DataSourceType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2024-08-19
 */
@DataSource(DataSourceType.SLAVE)
public interface GoodsDocMapper extends BaseMapper<GoodsDoc> {
    //insuItemCode
    List<ResponseMedicalStock> selectByCode(Map map);

}
