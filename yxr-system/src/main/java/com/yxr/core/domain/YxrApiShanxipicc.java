package com.yxr.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yxr.common.annotation.Excel;
import com.yxr.common.core.domain.BaseEntity;

/**
 * 陕西PICC请求接口对象 yxr_api_shanxipicc
 *
 * @author ruoyi
 * @date 2024-08-21
 */
@TableName(value = "yxr_api_shanxipicc")
public class YxrApiShanxipicc extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** ip地址 */
    @Excel(name = "ip地址")
    private String ip;

    /** 加密签名 */
    @Excel(name = "加密签名")
    private String msgSignature;

    /** 时间戳 */
    @Excel(name = "时间戳")
    private String timestamp;

    /** 1-成功;0-失败 */
    @Excel(name = "1-成功;0-失败")
    private Integer status;

    /** 1.查看库存接口 2.门慢购药接口 3.门慢退药接口 4.购药记录查询 */
    @Excel(name = "1.查看库存接口 2.门慢购药接口 3.门慢退药接口 4.购药记录查询")
    private Integer interfaceType;

    /** 请求数据 */
    @Excel(name = "请求数据")
    private String requestBody;

    /** 返回数据 */
    @Excel(name = "返回数据")
    private String responseBody;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }
    public void setMsgSignature(String msgSignature)
    {
        this.msgSignature = msgSignature;
    }

    public String getMsgSignature()
    {
        return msgSignature;
    }
    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getTimestamp()
    {
        return timestamp;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setInterfaceType(Integer interfaceType)
    {
        this.interfaceType = interfaceType;
    }

    public Integer getInterfaceType()
    {
        return interfaceType;
    }
    public void setRequestBody(String requestBody)
    {
        this.requestBody = requestBody;
    }

    public String getRequestBody()
    {
        return requestBody;
    }
    public void setResponseBody(String responseBody)
    {
        this.responseBody = responseBody;
    }

    public String getResponseBody()
    {
        return responseBody;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ip", getIp())
            .append("msgSignature", getMsgSignature())
            .append("timestamp", getTimestamp())
            .append("status", getStatus())
            .append("interfaceType", getInterfaceType())
            .append("requestBody", getRequestBody())
            .append("responseBody", getResponseBody())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
