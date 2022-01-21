package com.linkwechat.wecom.domain.vo;

import lombok.Data;

/**
 * 社群运营相关任务执行人信息
 */
@Data
public class WeCommunityTaskEmplVo {

    /**
     * 员工id
     */
    private String userId;

    /**
     * 员工名称
     */
    private String name;

    /**
     * 员工头像
     */
    private String avatar;

    /**
     * 是否已完成任务
     */
    private boolean isDone;
}
