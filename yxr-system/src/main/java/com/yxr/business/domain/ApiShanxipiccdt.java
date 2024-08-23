package com.yxr.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * shanxi接口明细表
 * </p>
 *
 * @author GS
 * @since 2024-08-22
 */
@Data
@TableName("API_SHANXIPICCDT")
public class ApiShanxipiccdt implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long billNo;

    /**
     * 医保编码
     */
    private String insuitemcode;

    /**
     * 药品名称
     */
    private String insuitemname;

    /**
     * 药品数量
     */
    private String itemnum;

    /**
     * 药品单价
     */
    private String price;


}
