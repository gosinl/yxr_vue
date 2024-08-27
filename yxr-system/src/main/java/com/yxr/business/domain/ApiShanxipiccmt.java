package com.yxr.business.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;





/**
 * <p>
 * shanxi接口汇总表
 * </p>
 *
 * @author GS
 * @since 2024-08-22
 */
@Data
@TableName("API_SHANXIPICCMT")
public class ApiShanxipiccmt implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键seq_api_shanxipicc.nextval
     */
    @TableId(type = IdType.AUTO)
    private Long billNo;

    /**
     * 药店编码
     */
    private String fixmedinsCode;

    /**
     * 交易编号
     */
    private String feedetlNo;

    /**
     * 总金额
     */
    private String medfeeSumamt;

    /**
     * 统筹支付
     */
    private String hifpPay;

    /**
     * 个账支付
     */
    private String acctPay;

    /**
     * 现金支付
     */
    private String psnCashpay;

    /**
     * 结算购药时间(yyyy-MM-dd HH:mm:ss)
     */
    private String infoTime;

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 证件号码
     */
    private String certNo;

    /**
     * 退单状态 1,正常 2，已退单
     */
    private String status;

    /**
     * 人员姓名
     */
    private String psnName;

    /**
     * 请求数据
     */
    private String request;

    /**
     * 创建时间
     */
    //@TableField(value = "CREATE_TIME", fill = FieldFill.INSERT,jdbcType = JdbcType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    //@TableField(fill = FieldFill.UPDATE,jdbcType = JdbcType.DATE)
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /*收银台是否已提帐 Y 已提帐 N 未提帐*/
    @TableField(value = "isDone")
    private String isDone;
}
