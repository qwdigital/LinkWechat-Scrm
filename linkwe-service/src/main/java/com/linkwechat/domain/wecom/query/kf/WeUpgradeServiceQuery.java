package com.linkwechat.domain.wecom.query.kf;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @Description 为客户升级为专员或客户群服务入参
 * @date 2021/12/13 14:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeUpgradeServiceQuery extends WeBaseQuery {

    /**
     * 外部联系人id/客户id
     */
    private String external_userid;

    /**
     * 客服帐号ID
     */
    private String open_kfid;

    /**
     * 升级类型。1:专员服务。2:客户群服务
     */
    private Integer type;


    /**
     * 推荐的服务专员
     */
    private Member member;

    /**
     * 推荐的客户群
     */
    private GroupChat groupchat;

    @Data
    public static class Member {
        /**
         * 服务专员的userid
         */
        private String userid;

        /**
         * 推荐语
         */
        private String wording;
    }

    @Data
    public static class GroupChat {
        /**
         * 客户群id
         */
        private String chat_id;

        /**
         * 推荐语
         */
        private String wording;
    }
}
