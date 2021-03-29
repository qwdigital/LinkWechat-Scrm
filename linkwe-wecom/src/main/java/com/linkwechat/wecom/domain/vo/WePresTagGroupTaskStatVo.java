package com.linkwechat.wecom.domain.vo;

import lombok.Data;


/**
 * 老客户标签建群任务客户统计Vo
 */
@Data
public class WePresTagGroupTaskStatVo {

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 发送状态
     */
    private String status;

    /**
     * 是否已在群
     */
    private boolean isInGroup = false;
}
