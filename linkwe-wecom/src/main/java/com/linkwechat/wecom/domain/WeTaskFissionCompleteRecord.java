package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 裂变任务完成记录对象 we_task_fission_complete_record
 *
 * @author ruoyi
 * @date 2021-01-27
 */
@ApiModel
@Data
public class WeTaskFissionCompleteRecord extends BaseEntity {
    private static final long serialVersionUID = -9170275723334248435L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 任务裂变表id
     */
    @Excel(name = "任务裂变表id")
    @ApiModelProperty("任务裂变表id")
    private Long taskFissionId;

    /**
     * 任务裂变记录表id
     */
    @Excel(name = "任务裂变记录表id")
    @ApiModelProperty("任务裂变记录表id")
    private Long fissionRecordId;

    /**
     * 裂变客户id
     */
    @Excel(name = "裂变客户id")
    @ApiModelProperty("裂变客户id")
    private String customerId;

    /**
     * 裂变客户姓名
     */
    @Excel(name = "裂变客户姓名")
    @ApiModelProperty("裂变客户姓名")
    private String customerName;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskFissionId", getTaskFissionId())
                .append("fissionRecordId", getFissionRecordId())
                .append("customerId", getCustomerId())
                .append("customerName", getCustomerName())
                .toString();
    }
}
