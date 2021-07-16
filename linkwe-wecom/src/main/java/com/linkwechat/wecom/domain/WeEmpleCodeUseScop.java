package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 员工活码使用人对象 we_emple_code_use_scop
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@ApiModel
@Data
@TableName("we_emple_code_use_scop")
public class WeEmpleCodeUseScop
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private Long id = SnowFlakeUtil.nextId();

    /** 员工活码id */
    @ApiModelProperty("员工活码id")
    private Long empleCodeId;

    /** 业务id类型1:组织机构id,2:成员id */
    @ApiModelProperty("业务id类型1:组织机构id,2:成员id")
    private Integer businessIdType;

    /** 活码类型下业务使用人的id */
    @ApiModelProperty("活码类型下业务使用人的id")
    private String businessId;

    /** 0:正常;2:删除; */
    @ApiModelProperty("0:正常;2:删除")
    private Integer delFlag = 0;

    /** 活码使用人员名称 */
    @ApiModelProperty("活码使用人员名称")
    private String businessName;

    /** 活码使用人员手机号 */
    @ApiModelProperty("活码使用人员手机号")
    @TableField(exist = false)
    private String mobile;


}
