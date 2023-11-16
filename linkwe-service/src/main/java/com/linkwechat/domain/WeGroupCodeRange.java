package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 客户群活码范围(WeGroupCodeRange)
 *
 * @author danmo
 * @since 2023-06-26 17:47:12
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_group_code_range")
public class WeGroupCodeRange extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1


    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 活码ID
     */
    @ApiModelProperty(value = "活码ID")
    @TableField("code_id")
    private Long codeId;


    /**
     * 群聊ID
     */
    @ApiModelProperty(value = "群聊ID")
    @TableField("chat_id")
    private String chatId;

    /**
     * 关联状态 0-未关联 1-关联
     */
    private Integer status;

    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;
}
