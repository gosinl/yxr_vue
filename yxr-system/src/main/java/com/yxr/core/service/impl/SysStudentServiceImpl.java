package com.yxr.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxr.common.utils.StringUtils;
import com.yxr.core.domain.SysStudent;
import com.yxr.core.mapper.SysStudentMapper;
import com.yxr.core.service.ISysStudentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学生信息Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysStudentServiceImpl extends ServiceImpl<SysStudentMapper, SysStudent> implements ISysStudentService
{
    @Override
    public List<SysStudent> queryList(SysStudent sysStudent)
    {
        // 注意：mybatis-plus lambda 模式不支持 eclipse 的编译器
        // LambdaQueryWrapper<SysStudent> queryWrapper = Wrappers.lambdaQuery();
        // queryWrapper.eq(SysStudent::getStudentName, sysStudent.getStudentName());
        //此处为查询条件添加
        QueryWrapper<SysStudent> queryWrapper = Wrappers.query();
        if (StringUtils.isNotEmpty(sysStudent.getStudentName()))
        {
            queryWrapper.eq("student_name", sysStudent.getStudentName());
        }
        if (StringUtils.isNotNull(sysStudent.getStudentAge()))
        {
            queryWrapper.eq("student_age", sysStudent.getStudentAge());
        }
        if (StringUtils.isNotEmpty(sysStudent.getStudentHobby()))
        {
            queryWrapper.eq("student_hobby", sysStudent.getStudentHobby());
        }
        return this.list(queryWrapper);
    }
}
