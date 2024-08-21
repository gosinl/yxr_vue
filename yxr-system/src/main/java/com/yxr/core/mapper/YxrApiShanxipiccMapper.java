package com.yxr.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxr.core.domain.YxrApiShanxipicc;

import java.util.List;


/**
 * 陕西PICC请求接口Mapper接口
 *
 * @author ruoyi
 * @date 2024-08-21
 */
public interface YxrApiShanxipiccMapper extends BaseMapper<YxrApiShanxipicc>
{
    /**
     * 查询陕西PICC请求接口
     *
     * @param id 陕西PICC请求接口主键
     * @return 陕西PICC请求接口
     */
    public YxrApiShanxipicc selectYxrApiShanxipiccById(Long id);

    public YxrApiShanxipicc selectYxrApiShanxipiccByTimestamp(String timestamp);

    public Long selectIdByTimestamp(String timestamp);

    /**
     * 查询陕西PICC请求接口列表
     *
     * @param yxrApiShanxipicc 陕西PICC请求接口
     * @return 陕西PICC请求接口集合
     */
    public List<YxrApiShanxipicc> selectYxrApiShanxipiccList(YxrApiShanxipicc yxrApiShanxipicc);

    /**
     * 新增陕西PICC请求接口
     *
     * @param yxrApiShanxipicc 陕西PICC请求接口
     * @return 结果
     */
    public int insertYxrApiShanxipicc(YxrApiShanxipicc yxrApiShanxipicc);

    /**
     * 修改陕西PICC请求接口
     *
     * @param yxrApiShanxipicc 陕西PICC请求接口
     * @return 结果
     */
    public int updateYxrApiShanxipicc(YxrApiShanxipicc yxrApiShanxipicc);

    /**
     * 删除陕西PICC请求接口
     *
     * @param id 陕西PICC请求接口主键
     * @return 结果
     */
    public int deleteYxrApiShanxipiccById(Long id);

    /**
     * 批量删除陕西PICC请求接口
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteYxrApiShanxipiccByIds(Long[] ids);
}
