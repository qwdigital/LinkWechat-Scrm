package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 朋友圈互动列表(WeMomentsInteracte)
 *
 * @author danmo
 * @since 2023-04-18 18:41:59
 */
@ApiModel
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@TableName("we_moments_interacte")
public class WeMomentsInteracte extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 互动人员名称id
     */
    @ApiModelProperty(value = "互动人员名称id")
    @TableField("interacte_user_id")
    private String interacteUserId;


    /**
     * 互动类型:0:评论；1:点赞
     */
    @ApiModelProperty(value = "互动类型:0:评论；1:点赞")
    @TableField("interacte_type")
    private Integer interacteType;


    /**
     * 朋友圈id
     */
    @ApiModelProperty(value = "朋友圈id")
    @TableField("moment_id")
    private String momentId;


    /**
     * 互动人员类型:0:员工；1:客户
     */
    @ApiModelProperty(value = "互动人员类型:0:员工；1:客户")
    @TableField("interacte_user_type")
    private Integer interacteUserType;


    /**
     * 互动时间
     */
    @ApiModelProperty(value = "互动时间")
    @TableField("interacte_time")
    private Date interacteTime;

    //朋友圈创建人id
    @ApiModelProperty(value = "朋友圈创建人id")
    @TableField(exist = false)
    private String momentCreteOrId;

    private Integer delFlag;

}
