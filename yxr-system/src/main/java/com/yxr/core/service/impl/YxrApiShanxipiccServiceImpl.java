package com.yxr.core.service.impl;

import java.util.List;
import com.yxr.common.utils.DateUtils;
import com.yxr.core.domain.YxrApiShanxipicc;
import com.yxr.core.mapper.YxrApiShanxipiccMapper;
import com.yxr.core.service.IYxrApiShanxipiccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 陕西PICC请求接口Service业务层处理
 *
 * @author ruoyi
 * @date 2024-08-21
 */
@Service
public class YxrApiShanxipiccServiceImpl implements IYxrApiShanxipiccService
{
    @Autowired
    private YxrApiShanxipiccMapper yxrApiShanxipiccMapper;

    /**
     * 查询陕西PICC请求接口
     *
     * @param id 陕西PICC请求接口主键
     * @return 陕西PICC请求接口
     */
    @Override
    public YxrApiShanxipicc selectYxrApiShanxipiccById(Long id)
    {
        return yxrApiShanxipiccMapper.selectYxrApiShanxipiccById(id);
    }

    @Override
    public YxrApiShanxipicc selectYxrApiShanxipiccByTimestamp(String timestamp) {
        return yxrApiShanxipiccMapper.selectYxrApiShanxipiccByTimestamp(timestamp);
    }

    /**
     * 查询陕西PICC请求接口列表
     *
     * @param yxrApiShanxipicc 陕西PICC请求接口
     * @return 陕西PICC请求接口
     */
    @Override
    public List<YxrApiShanxipicc> selectYxrApiShanxipiccList(YxrApiShanxipicc yxrApiShanxipicc)
    {
        return yxrApiShanxipiccMapper.selectYxrApiShanxipiccList(yxrApiShanxipicc);
    }

    /**
     * 新增陕西PICC请求接口
     *
     * @param yxrApiShanxipicc 陕西PICC请求接口
     * @return 结果
     */
    @Override
    public int insertYxrApiShanxipicc(YxrApiShanxipicc yxrApiShanxipicc)
    {
        yxrApiShanxipicc.setCreateTime(DateUtils.getNowDate());
        return yxrApiShanxipiccMapper.insertYxrApiShanxipicc(yxrApiShanxipicc);
    }

    @Override
    public Long insertYxrApiShanxipiccReturenId(YxrApiShanxipicc yxrApiShanxipicc) {
        yxrApiShanxipicc.setCreateTime(DateUtils.getNowDate());
        yxrApiShanxipiccMapper.insertYxrApiShanxipicc(yxrApiShanxipicc);
        return yxrApiShanxipiccMapper.selectIdByTimestamp(yxrApiShanxipicc.getTimestamp());
    }





    /**
     * 修改陕西PICC请求接口
     *
     * @param yxrApiShanxipicc 陕西PICC请求接口
     * @return 结果
     */
    @Override
    public int updateYxrApiShanxipicc(YxrApiShanxipicc yxrApiShanxipicc)
    {
        yxrApiShanxipicc.setUpdateTime(DateUtils.getNowDate());
        return yxrApiShanxipiccMapper.updateYxrApiShanxipicc(yxrApiShanxipicc);
    }

    /**
     * 批量删除陕西PICC请求接口
     *
     * @param ids 需要删除的陕西PICC请求接口主键
     * @return 结果
     */
    @Override
    public int deleteYxrApiShanxipiccByIds(Long[] ids)
    {
        return yxrApiShanxipiccMapper.deleteYxrApiShanxipiccByIds(ids);
    }

    /**
     * 删除陕西PICC请求接口信息
     *
     * @param id 陕西PICC请求接口主键
     * @return 结果
     */
    @Override
    public int deleteYxrApiShanxipiccById(Long id)
    {
        return yxrApiShanxipiccMapper.deleteYxrApiShanxipiccById(id);
    }
}
