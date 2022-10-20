package com.linkwechat.common.core.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 行政区划(SysArea)
 *
 * @author danmo
 * @since 2022-06-27 11:01:07
 */
@ApiModel
@Data
@SuppressWarnings("serial")
public class SysAreaVo implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 区域ID
     */
    @ApiModelProperty(value = "区域ID")
    private Integer id;


    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    private Integer parentId;


    /**
     * 层级
     */
    @ApiModelProperty(value = "层级")
    private Integer level;


    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String name;


    /**
     * 拼音首字母
     */
    @ApiModelProperty(value = "拼音首字母")
    private String ePrefix;


    /**
     * 拼音名称
     */
    @ApiModelProperty(value = "拼音名称")
    private String eName;


    /**
     * 对外区域ID
     */
    @ApiModelProperty(value = "对外区域ID")
    private Long extId;


    /**
     * 区域对外名称
     */
    @ApiModelProperty(value = "区域对外名称")
    private String extName;

}
