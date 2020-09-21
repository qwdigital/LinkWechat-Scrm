package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/21 0021 23:55
 */
@Data
public class WeGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String groupName;

    private Long memberNum;

    private String groupLeader;

    private String notice;

    private String groupLeaderUserId;
}
