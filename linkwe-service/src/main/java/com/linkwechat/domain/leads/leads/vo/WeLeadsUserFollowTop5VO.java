package com.linkwechat.domain.leads.leads.vo;

import lombok.Data;

/**
 * 员工线索跟进top5
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 16:11
 */
@Data
public class WeLeadsUserFollowTop5VO {

    /**
     * 跟进员工
     */
    private String userName;

    /**
     * 跟进人数
     */
    private Integer followNum;

}
