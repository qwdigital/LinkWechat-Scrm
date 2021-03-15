package com.linkwechat.wecom.domain.vo;

import lombok.Data;


/**
 * 老客户标签建群任务客户统计Vo
 */
@Data
public class WePresTagGroupTaskStatVo {

    /**
     * 客户id
     */
    private String customerName;

    /**
     * 是否已发送
     */
    private boolean isSent = false;

    /**
     * 是否已在群
     */
    private boolean isInGroup = false;
}
