package com.linkwechat.domain.qr;

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
 * 活码标签关联表(WeQrTagRel)表实体类
 *
 * @author makejava
 * @since 2021-11-07 01:29:13
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_qr_tag_rel")
public class WeQrTagRel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 活码id
     */
    @ApiModelProperty(value = "活码id")
    @TableField("qr_id")
    private Long qrId;


    /**
     * 业务类型 1-员工活码 2-门店导购 3-拉新活码
     */
    @ApiModelProperty(value = "业务类型 1-员工活码 2-门店导购 3-拉新活码 ...")
    @TableField("business_type")
    private Integer businessType;

    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    @TableField("tag_id")
    private String tagId;

    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除",hidden = true)
    @TableField("del_flag")
    private Integer delFlag;
}
