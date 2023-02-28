package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客服接待人员表(WeKfServicer)
 *
 * @author danmo
 * @since 2022-04-15 15:53:39
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_kf_servicer")
public class WeKfServicer extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    @TableField("corp_id")
    private String corpId;


    /**
     * 客服id
     */
    @ApiModelProperty(value = "客服id")
    @TableField("kf_id")
    private Long kfId;


    /**
     * 客服帐号ID
     */
    @ApiModelProperty(value = "客服帐号ID")
    @TableField("open_kf_id")
    private String openKfId;


    /**
     * 接待人员userid
     */
    @ApiModelProperty(value = "接待人员userid")
    @TableField("user_id")
    private String userId;

    /**
     * 接待人员部门的id
     */
    @ApiModelProperty(value = "接待人员部门的id")
    @TableField("department_id")
    private Integer departmentId;

    /**
     * 接待人员的接待状态。0:接待中,1:停止接待
     */
    @ApiModelProperty(value = "接待人员的接待状态。0:接待中,1:停止接待")
    @TableField("status")
    private Integer status;


    /**
     * 接待人数
     */
    @ApiModelProperty(value = "接待人数")
    @TableField("reception_num")
    private Integer receptionNum;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
