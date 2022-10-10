package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 素材收藏表(WeChatCollection)
 *
 * @author danmo
 * @since 2022-05-25 17:56:59
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_chat_collection")
public class WeChatCollection extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 素材ID
     */
    @ApiModelProperty(value = "素材ID")
    @TableField("material_id")
    private Long materialId;


    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    @TableField("user_id")
    private Long userId;


    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;
}
