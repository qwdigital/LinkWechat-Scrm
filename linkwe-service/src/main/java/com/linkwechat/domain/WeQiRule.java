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
 * 质检规则表(WeQiRule)
 *
 * @author danmo
 * @since 2023-05-05 16:57:30
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_qi_rule")
public class WeQiRule extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    @TableField("name")
    private String name;


    /**
     * 超时时间
     */
    @ApiModelProperty(value = "超时时间")
    @TableField("time_out")
    private Integer timeOut;


    /**
     * 会话类型 1-全部 2-客户会话 3-客群会话
     */
    @ApiModelProperty(value = "会话类型 1-全部 2-客户会话 3-客群会话")
    @TableField("chat_type")
    private Integer chatType;


    /**
     * 督导人员（逗号相隔）
     */
    @ApiModelProperty(value = "督导人员（逗号相隔）")
    @TableField("manage_user")
    private String manageUser;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;

}
