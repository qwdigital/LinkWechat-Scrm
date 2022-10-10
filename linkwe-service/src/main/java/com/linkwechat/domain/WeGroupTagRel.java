package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群标签关系(WeGroupTagRel)
 *
 * @author danmo
 * @since 2022-04-06 11:09:57
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_group_tag_rel")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeGroupTagRel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 群id
     */
    @ApiModelProperty(value = "群id")
    @TableField("chat_id")
    private String chatId;


    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    @TableField("tag_id")
    private String tagId;


    /**
     * 删除标志 0-正常 1-删除
     */
    @ApiModelProperty(value = "删除标志 0-正常 1-删除")
    @TableField("del_flag")
    private Integer delFlag;


    
    
    
}
