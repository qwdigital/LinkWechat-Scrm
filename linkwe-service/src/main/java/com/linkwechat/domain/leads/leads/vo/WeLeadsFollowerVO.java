package com.linkwechat.domain.leads.leads.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 跟进人名单
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 14:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeLeadsFollowerVO {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 跟进人Id
     */
    @ApiModelProperty(value = "跟进人Id")
    private Long followerId;

    /**
     * 跟进人名称
     */
    @ApiModelProperty(value = "跟进人名称")
    private String followUserName;

    /**
     * 跟进人所属部门
     */
    @ApiModelProperty(value = "跟进人所属部门")
    private String followUserDeptName;

    /**
     * 跟进状态 0待分配, 1跟进中，2已上门 3已退回
     */
    @ApiModelProperty(value = "跟进状态 0待分配, 1跟进中，2已上门 3已退回")
    private Integer followerStatus;

    /**
     * 跟进状态 0待分配, 1跟进中，2已上门 3已退回
     */
    @ApiModelProperty(value = "跟进状态")
    private String followerStatusStr;

    /**
     * 退回方式 0 成员主动退回 1 过期自动强制回收 2 管理员退回 3 离职退回 4 线索转移
     */
    @ApiModelProperty(value = "退回方式code 0 成员主动退回 1 过期自动强制回收 2 管理员退回 3 离职退回 4 线索转移")
    private Integer backMode;

    /**
     * 退回方式 0 成员主动退回 1 过期自动强制回收 2 管理员退回 3 离职退回 4 线索转移
     */
    @ApiModelProperty(value = "退回方式")
    private String backModeStr;

    /**
     * 客户Id
     */
    private Long customerId;

    /**
     * 绑定客户时间
     */
    private Date bindCustomerTime;
}
