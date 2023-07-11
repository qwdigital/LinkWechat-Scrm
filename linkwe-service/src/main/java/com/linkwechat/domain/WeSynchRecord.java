package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 同步记录表
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_synch_record")
public class WeSynchRecord extends BaseEntity {

    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /**
     * 同步类型
     *
     * @see com.linkwechat.common.constant.SynchRecordConstants
     */
    @ApiModelProperty("同步类型:1:客户模块同步 2-客群")
    private Integer synchType;

    @ApiModelProperty("同步时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date synchTime;

}
