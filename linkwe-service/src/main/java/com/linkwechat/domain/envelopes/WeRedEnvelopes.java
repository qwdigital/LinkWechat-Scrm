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
    private Long id;

    /**
     * 金额
     */
    private Integer money;

    /**
     * 1:客户;2:客群;3:客户与客群
     */
    private Integer sceneType;

    /**
     * 红包名称
     */
    private String name;

    /**
     * 0:启用;1:停用
     */
    private Integer status;

    /**
     * 发送次数
     */
    private Integer sendTimes;

    /**
     * 红包类型:0:企业红包;1:个人红包
     */
    private Integer redEnvelopesType;

    /**
     * 0:正常;1:删除;
     */
    @TableLogic
    private Integer delFlag;

}

