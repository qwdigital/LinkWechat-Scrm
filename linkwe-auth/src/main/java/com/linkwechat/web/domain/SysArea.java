package com.linkwechat.web.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 行政区划(SysArea)
 *
 * @author danmo
 * @since 2022-06-27 11:01:07
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("sys_area")
public class SysArea implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 区域ID
     */
    @ApiModelProperty(value = "区域ID")
    @TableId(type = IdType.INPUT)
    @TableField("id")
    private Integer id;


    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    @TableField("parent_id")
    private Integer parentId;


    /**
     * 层级
     */
    @ApiModelProperty(value = "层级")
    @TableField("level")
    private Integer level;


    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    @TableField("name")
    private String name;


    /**
     * 拼音首字母
     */
    @ApiModelProperty(value = "拼音首字母")
    @TableField("e_prefix")
    private String ePrefix;


    /**
     * 拼音名称
     */
    @ApiModelProperty(value = "拼音名称")
    @TableField("e_name")
    private String eName;


    /**
     * 对外区域ID
     */
    @ApiModelProperty(value = "对外区域ID")
    @TableField("ext_id")
    private Long extId;


    /**
     * 区域对外名称
     */
    @ApiModelProperty(value = "区域对外名称")
    @TableField("ext_name")
    private String extName;


    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    @TableField("create_by_id")
    private Long createById;


    /**
     * 更新人id
     */
    @ApiModelProperty(value = "更新人id")
    @TableField("update_by_id")
    private Long updateById;

    /** 创建者 */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private String createBy;


    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;
}
