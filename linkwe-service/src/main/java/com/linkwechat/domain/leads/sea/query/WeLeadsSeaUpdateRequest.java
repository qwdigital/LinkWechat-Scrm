package com.linkwechat.domain.leads.sea.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 线索公海
 *
 * @author WangYX
 * @since 2023-04-03
 */
@Data
public class WeLeadsSeaUpdateRequest {


    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 公海名称
     */
    @Size(max = 20, message = "公海名称不能超过20个字符")
    @ApiModelProperty(value = "公海名称")
    private String name;

    /**
     * 是否自动回收（1 表示是，0 表示否）
     */
    @ApiModelProperty(value = "是否自动回收（1 表示是，0 表示否）")
    private Integer isAutoRecovery;

    /**
     * 公海线索数
     */
    @ApiModelProperty(value = "公海线索数")
    private Integer num;

    /**
     * 成员领取线索超过A天未能实现几次跟进，则回收至公海池。且必须在前几天内完成首次跟进。(A)
     */
    @ApiModelProperty(value = "成员领取线索超过A天未能实现几次跟进，则回收至公海池。且必须在前几天内完成首次跟进。(A)")
    private Integer first;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private VisibleRange.DeptRange deptRange;

    /**
     * 岗位
     */
    @ApiModelProperty(value = "岗位")
    private VisibleRange.PostRange postRange;

    /**
     * 员工
     */
    @ApiModelProperty(value = "员工")
    private VisibleRange.UserRange userRange;

}
