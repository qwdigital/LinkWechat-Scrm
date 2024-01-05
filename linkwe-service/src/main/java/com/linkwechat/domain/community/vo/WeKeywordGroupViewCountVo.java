package com.linkwechat.domain.community.vo;

import lombok.Data;

/**
 * 关键词统计
 */
@Data
public class WeKeywordGroupViewCountVo {

    /**
     * 日期
     */
    private String date;
    /**
     * 总访问数量
     */
    private Integer totalViewNumber;

    /**
     * 总入群数量
     */
    private Integer totalJoinGroupNmber;

    /**
     * 今日问数量
     */
    private Integer tdViewNumber;

    /**
     * 今日入群数量
     */
    private Integer tdJoinGroupNmber;

    public WeKeywordGroupViewCountVo(){
        this.totalViewNumber=0;
        this.totalJoinGroupNmber=0;
        this.tdViewNumber=0;
        this.tdJoinGroupNmber=0;
    }
}
