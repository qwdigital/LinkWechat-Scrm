package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 裂变任务记录对象 we_task_fission_record
 *
 * @author ruoyi
 * @date 2021-01-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeTaskFissionRecord extends BaseEntity {
    private static final long serialVersionUID = 8770538385789110599L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 任务裂变表id
     */
    @Excel(name = "任务裂变表id")
    private Long taskFissionId;

    /**
     * 裂变任务客户id
     */
    @Excel(name = "裂变任务客户id")
    private String customerId;

    /**
     * 裂变任务客户姓名
     */
    @Excel(name = "裂变任务客户姓名")
    private String customerName;

    /**
     * 裂变客户数量
     */
    @Excel(name = "裂变客户数量")
    private Long fissNum;

    private String configId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskFissionId", getTaskFissionId())
                .append("customerId", getCustomerId())
                .append("customerName", getCustomerName())
                .append("fissNum", getFissNum())
                .append("configId", getConfigId())
                .toString();
    }
}
