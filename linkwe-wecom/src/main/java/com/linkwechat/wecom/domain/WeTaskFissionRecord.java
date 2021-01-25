package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 裂变任务完成记录对象 we_task_fission_record
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Data
public class WeTaskFissionRecord extends BaseEntity {
    private static final long serialVersionUID = -3547099425154996193L;
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
     * 发成员工id
     */
    @Excel(name = "发成员工id")
    private String staffId;

    /**
     * 裂变客户id
     */
    @Excel(name = "裂变客户id")
    private String customerId;

    /**
     * 客户姓名
     */
    @Excel(name = "客户姓名")
    private String customerName;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskFissionId", getTaskFissionId())
                .append("staffId", getStaffId())
                .append("customerId", getCustomerId())
                .append("customerName", getCustomerName())
                .toString();
    }
}
