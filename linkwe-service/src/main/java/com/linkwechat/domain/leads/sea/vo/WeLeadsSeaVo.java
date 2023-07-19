package com.linkwechat.domain.leads.sea.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.leads.sea.query.VisibleRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 公海
 *
 * @author WangYX
 * @since 2023-04-03
 */
@Data
public class WeLeadsSeaVo {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 公海名称
     */
    private String name;

    /**
     * 是否自动回收（1 表示是，0 表示否）
     */
    private Integer isAutoRecovery;

    /**
     * 成员领取线索超过A天未能实现几次跟进，则回收至公海池。且必须在前几天内完成首次跟进。(A)
     */
    private Integer first;

    /**
     * 公海线索数
     */
    private Integer num = 0;

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

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;



}
