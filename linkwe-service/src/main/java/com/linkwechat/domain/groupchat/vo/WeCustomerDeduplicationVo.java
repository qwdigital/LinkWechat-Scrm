package com.linkwechat.domain.groupchat.vo;


import lombok.Data;

@Data
public class WeCustomerDeduplicationVo {


    /**
     * 0-未知 1-男性 2-女性
     */
    private Integer gender;

    /**
     * 客户头像
     */
    private String avatar;


    /**
     * 客户名称
     */
    private String customerName;


    /**
     * 客户id
     */
    private String externalUserid;

    /**
     * 根据员工，多个使用逗号隔开（目前该字段使用场景，客户去重）
     */
    private String addWeUserNames;


    /**
     * 当前客户加入的群名,多个使用逗号隔开
     */
    private String joinGroupNames;



    /**
     * 0:加入黑名单;1:不加入黑名单;
     */
    private Integer isJoinBlacklist;
}
