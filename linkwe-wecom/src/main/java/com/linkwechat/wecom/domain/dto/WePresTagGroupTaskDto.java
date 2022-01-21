package com.linkwechat.wecom.domain.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 社群运营 老客户标签建群
 */

@Data
public class WePresTagGroupTaskDto {

    /**
     * 任务名称
     */
    @NotNull(message = "任务名称不能为空")
    private String taskName;

    /**
     * 发送方式 0: 企业群发 1：个人群发
     */
    @NotNull(message = "发送方式不能为空")
    private Integer sendType;

    /**
     * 加群引导语
     */
    @Size(max = 64, message = "引导与不能超过64字符")
    @NotBlank(message = "引导语不能为空")
    private String welcomeMsg;

    /**
     * 群活码id
     */
    @NotNull(message = "活码不能为空")
    private Long groupCodeId;

    /**
     * 客户标签
     */
    private List<String> tagList;

    /**
     * 选择员工
     */
    private List<String> scopeList;

    /**
     * 发送范围 0: 全部客户 1：部分客户
     */
    private Integer sendScope;

    /**
     * 发送性别 0: 全部 1： 男 2： 女 3：未知
     */
    private Integer sendGender;

    /**
     * 目标客户被添加起始时间
     */
    private String cusBeginTime;

    /**
     * 目标客户被添加结束时间
     */
    private String cusEndTime;
}
