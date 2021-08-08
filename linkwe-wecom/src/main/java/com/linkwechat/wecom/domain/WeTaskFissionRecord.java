package com.linkwechat.wecom.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 裂变任务记录对象 we_task_fission_record
 *
 * @author ruoyi
 * @date 2021-01-27
 */
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeTaskFissionRecord {
    private static final long serialVersionUID = 8770538385789110599L;
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
     * 裂变任务客户id
     */
    @Excel(name = "裂变任务客户id")
    @ApiModelProperty("裂变任务客户id")
    private String customerId;

    /**
     * 裂变任务客户姓名
     */
    @Excel(name = "裂变任务客户姓名")
    @ApiModelProperty("裂变任务客户姓名")
    private String customerName;

    /**
     * 裂变客户数量
     */
    @Excel(name = "裂变客户数量")
    @ApiModelProperty("裂变客户数量")
    private Long fissNum;

    @ApiModelProperty("二维码链接")
    private String qrCode;

    @ApiModelProperty("海报链接")
    private String poster;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime = new Date();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "完成时间")
    private Date completeTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskFissionId", getTaskFissionId())
                .append("customerId", getCustomerId())
                .append("customerName", getCustomerName())
                .append("fissNum", getFissNum())
                .append("qrCode", getQrCode())
                .append("poster", getPoster())
                .append("createTime", getCreateTime())
                .append("completeTime", getCompleteTime())
                .toString();
    }
}
