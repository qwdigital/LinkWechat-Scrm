package com.linkwechat.domain.leads.leads.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author caleb
 * @since 2023/5/4
 */
@Data
public class SeaUserCountVo {

    @ApiModelProperty(value = "员工id")
    private Long userId;

    @ApiModelProperty(value = "员工姓名")
    private String userName;

    @ApiModelProperty(value = "所选部门/组织id")
    private Long deptId;

    @ApiModelProperty(value = "所属部门名称")
    private String deptLevelName;

    @ApiModelProperty(value = "上门转化率")
    private Double conversion;

    @ApiModelProperty("加V客户数")
    private Integer newContactCnt;

    @ApiModelProperty("线索退回率")
    private Double leadsReturnRate;

    @ApiModelProperty("平均跟进次数")
    private Double followUpNum;
}
