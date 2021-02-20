package com.linkwechat.wecom.domain;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 裂变任务员工列对象 we_task_fission_staff
 *
 * @author leejoker
 * @date 2021-01-20
 */
@ApiModel
@Data
public class WeTaskFissionStaff extends BaseEntity {
    private static final long serialVersionUID = -8615355081305655759L;
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
     * 员工或机构，1 组织机构 2 成员 3 全部
     */
    @Excel(name = "员工或机构，1 组织机构 2 成员 3 全部")
    @ApiModelProperty("员工或机构，1 组织机构 2 成员 3 全部")
    private Integer staffType;

    /**
     * 员工或组织机构id,为全部时为空
     */
    @Excel(name = "员工或组织机构id,为全部时为空")
    @ApiModelProperty("员工或组织机构id,为全部时为空")
    private String staffId;

    /**
     * 员工或组织机构姓名，类型为全部时，为空
     */
    @Excel(name = "员工或组织机构姓名，类型为全部时，为空")
    @ApiModelProperty("员工或组织机构姓名，类型为全部时，为空")
    private String staffName;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskFissionId", getTaskFissionId())
                .append("staffType", getStaffType())
                .append("staffId", getStaffId())
                .append("staffName", getStaffName())
                .toString();
    }
}
