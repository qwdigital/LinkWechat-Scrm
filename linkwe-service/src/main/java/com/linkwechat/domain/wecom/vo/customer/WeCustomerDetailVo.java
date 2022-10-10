package com.linkwechat.domain.wecom.vo.customer;

import com.linkwechat.domain.wecom.entity.customer.WeCustomerFollowInfoEntity;
import com.linkwechat.domain.wecom.entity.customer.WeCustomerFollowUserEntity;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 获取客户详情返回对象
 * @date 2021/12/2 16:11
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCustomerDetailVo extends WeResultVo {

    /**
     * 客户详情
     */
    private ExternalContact externalContact;


    /**
     * 客户联系人
     */
    private List<WeCustomerFollowUserEntity> followUser;

    /**
     * 客户联系人
     */
    private WeCustomerFollowInfoEntity followInfo;


    @Data
    public static class ExternalContact {
        /**
         * 外部联系人userId
         */
        private String externalUserId;
        /**
         * 外部联系人名称
         */
        private String name;
        /**
         * 外部联系人职位
         */
        private String position;
        /**
         * 外部联系人头像
         */
        private String avatar;
        /**
         * 外部联系人所在企业简称
         */
        private String corpName;
        /**
         * 外部联系人所在企业全称
         */
        private String corpFullName;
        /**
         * 外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户
         */
        private Integer type;
        /**
         * 外部联系人性别 0-未知 1-男性 2-女性
         */
        private Integer gender;

        /**
         * 外部联系人在微信开放平台的唯一身份标识（微信unionid），通过此字段企业可将外部联系人与公众号/小程序用户关联起来。
         */
        private String unionId;
    }


    @Data
    public static class ExternalUserTag {
        /**
         * 标签的分组名称
         */
        private String groupName;
        /**
         * 标签名称
         */
        private String tagName;
        /**
         * 企业标签的id，用户自定义类型标签（type=2）不返回
         */
        private String tagId;
        /**
         * 标签类型, 1-企业设置，2-用户自定义，3-规则组标签（仅系统应用返回）
         */
        private Integer type;
    }

}
