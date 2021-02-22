package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/12 17:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "we_sensitive_act")
@ApiModel(value = "敏感行为")
public class WeSensitiveAct extends BaseEntity {
    private static final long serialVersionUID = -7550455294164753520L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "敏感行为id")
    private Long id;

    /**
     * 敏感行为名称
     */
    @TableField(value = "act_name")
    @NotBlank(message = "敏感行为名称不能为空")
    @ApiModelProperty(value = "敏感行为名称")
    private String actName;

    /**
     * 排序字段
     */
    @TableField(value = "order_num")
    @ApiModelProperty(value = "排序字段")
    private Integer orderNum;

    /**
     * 是否开启记录
     */
    @TableField(value = "enable_flag")
    @ApiModelProperty(value = "1 开启 0 关闭")
    private Integer enableFlag;

    /**
     * 删除状态
     */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "1 已删除 0 未删除")
    private Integer delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("actName", getActName())
                .append("orderNum", getOrderNum())
                .append("enableFlag", getEnableFlag())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
