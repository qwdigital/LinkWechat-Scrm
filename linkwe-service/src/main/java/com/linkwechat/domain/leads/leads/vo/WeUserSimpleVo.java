package com.linkwechat.domain.leads.leads.vo;

import lombok.Data;

import java.util.List;

/**
 * 用户
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/07 17:50
 */
@Data
public class WeUserSimpleVo {

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 企微用户Id
     */
    private String weUserId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 转化率
     */
    private String conversion;

    /**
     * 分配数量
     */
    private Integer num;

    /**
     * 线索Id集合
     */
    private List<Long> leadsList;

}
