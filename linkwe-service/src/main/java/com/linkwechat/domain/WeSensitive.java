package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 敏感词设置表(WeSensitive)
 *
 * @author danmo
 * @since 2022-06-10 10:38:46
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_sensitive")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeSensitive extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称")
    @TableField("strategy_name")
    private String strategyName;


    /**
     * 匹配词
     */
    @ApiModelProperty(value = "匹配词")
    @TableField("pattern_words")
    private String patternWords;


    /**
     * 审计人id
     */
    @ApiModelProperty(value = "审计人id")
    @TableField("audit_user_id")
    private String auditUserId;


    /**
     * 审计人
     */
    @ApiModelProperty(value = "审计人")
    @TableField("audit_user_name")
    private String auditUserName;


    /**
     * 消息通知,1 开启 0 关闭
     */
    @ApiModelProperty(value = "消息通知,1 开启 0 关闭")
    @TableField("alert_flag")
    private Integer alertFlag;


    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 审计范围，敏感词审计需要覆盖的用户或机构
     */
    //@NotEmpty(message = "审计范围不能为空")
    @TableField(exist = false)
    @ApiModelProperty(value = "审计范围")
    private List<WeSensitiveAuditScope> auditUserScope;
}
