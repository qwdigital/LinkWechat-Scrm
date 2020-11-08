package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;

import java.util.Date;


/**
 * 员工活码使用人对象 we_emple_code_use_scop
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Data
@TableName("we_emple_code_use_scop")
public class WeEmpleCodeUseScop
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private Long id = SnowFlakeUtil.nextId();

    /** 员工活码id */
    private Long empleCodeId;

    /** 活码类型下业务使用人的id */
    private Long businessId;

    /** 0:正常;2:删除; */
    private Integer delFlag=new Integer(0);

    /** 活码使用人员名称 */
    @TableField(exist = false)
    private String userUserName;


}
