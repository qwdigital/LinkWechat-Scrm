package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.utils.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 员工活码标签对象 we_emple_code_tag
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Data
@TableName("we_emple_code_tag")
public class WeEmpleCodeTag
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private Long id=SnowFlakeUtil.nextId();

    /** 标签id */
    @ApiModelProperty("标签id")
    private String tagId;

    /** 员工活码id */
    @ApiModelProperty("员工活码id")
    private Long empleCodeId;

    /** 0:正常;2:删除; */
    @ApiModelProperty("0:正常;1:删除")
    @TableLogic
    private Integer delFlag = 0;

    /** 标签名 */
    @ApiModelProperty("标签名")
    private String tagName;


}
