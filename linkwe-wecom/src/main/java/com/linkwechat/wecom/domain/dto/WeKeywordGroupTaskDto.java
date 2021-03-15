package com.linkwechat.wecom.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 社区运营 - 关键词拉群Dto
 */
@Data
public class WeKeywordGroupTaskDto {

    /**
     * 任务名
     */
    @NotNull(message = "任务名不能为空")
    private String taskName;

    /**
     * 群活码id
     */
    @NotNull(message = "群活码不能为空")
    private Long groupCodeId;

    /**
     * 加群引导语
     */
    @NotNull(message = "引导语不可为空")
    private String welcomeMsg;

    /**
     * 关键词
     */
    @NotNull(message = "关键词不可为空")
    private String keywords;
}
