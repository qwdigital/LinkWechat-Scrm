package com.linkwechat.domain.leads.record.vo;

import lombok.Data;

import java.util.List;

/**
 * 移动端跟进记录详情
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/24 14:37
 */
@Data
public class WeLeadsFollowRecordDetailVO {

    /**
     * 跟进记录Id
     */
    private Long recordId;

    /**
     * 线索客户名称
     */
    private String leadsName;

    /**
     * 跟进人名称
     */
    private String followerName;

    /**
     * 性别 0 = 未知, 1 = 男, 2 = 女
     *
     * @see com.linkwechat.common.enums.SexEnums
     */
    private Integer sex;

    /**
     * 跟进记录内容
     */
    private List<WeLeadsFollowRecordContentVO> contents;
}
