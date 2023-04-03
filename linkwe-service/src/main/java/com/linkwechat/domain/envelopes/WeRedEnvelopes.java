package com.linkwechat.domain.envelopes;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


/**
 * 红包
 */
@ApiModel
@Data
@Builder
@TableName("we_red_envelopes")
@AllArgsConstructor
@NoArgsConstructor
public class WeRedEnvelopes extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Integer money;

    /**
     * 1:客户;2:客群;3:客户与客群
     */
    @ApiModelProperty("1:客户;2:客群;3:客户与客群")
    private Integer sceneType;

    /**
     * 红包名称
     */
    @ApiModelProperty("红包名称")
    private String name;

    /**
     * 0:启用;1:停用
     */
    @ApiModelProperty("状态 0:启用;1:停用")
    private Integer status;

    /**
     * 发送次数
     */
    @ApiModelProperty("发送次数")
    private Integer sendTimes;

    /**
     * 红包类型:0:企业红包;1:个人红包
     */
    @ApiModelProperty("红包类型:0:企业红包;1:个人红包")
    private Integer redEnvelopesType;

    /**
     * 0:正常;1:删除;
     */
    @TableLogic
    @ApiModelProperty("删除标识 0:正常;1:删除;")
    private Integer delFlag;

}

