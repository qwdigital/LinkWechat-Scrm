package com.linkwechat.domain.community.query;

import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class WeCommunityKeyWordGroupTableQuery extends BaseEntity {

    /**
     * 客户名
     */
    private String customerName;

    /**
     * 渠道标识
     */
    private List<String> states;


    /**
     * 是否是好友
     * null 查询全部
     * true 是好友
     * false 不是好友
     */
    private Boolean friend;


    /**
     * 关键词主表主键
     */
    private Long keywordGroupId;



    /**
     * 客户id
     */
    private String externalUserid;


}
