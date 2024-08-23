package com.yxr.business.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxr.business.domain.ApiShanxipiccmt;
import com.yxr.common.annotation.DataSource;
import com.yxr.common.enums.DataSourceType;

/**
 * <p>
 * shanxi接口汇总表 Mapper 接口
 * </p>
 *
 * @author GS
 * @since 2024-08-22
 */
@DataSource(DataSourceType.SLAVE)
public interface ApiShanxipiccmtMapper extends BaseMapper<ApiShanxipiccmt> {

    Long selectBillNo();

    void updateByfeedetlNo(ApiShanxipiccmt apiShanxipiccmt);
}
