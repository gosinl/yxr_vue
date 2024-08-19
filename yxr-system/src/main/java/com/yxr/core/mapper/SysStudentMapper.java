package com.yxr.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxr.core.domain.SysStudent;

/**
 * 学生信息Mapper接口
 *
 * @author ruoyi
 */
public interface SysStudentMapper extends BaseMapper<SysStudent>
{

    int updateByStudentId(SysStudent sysStudent);

}
