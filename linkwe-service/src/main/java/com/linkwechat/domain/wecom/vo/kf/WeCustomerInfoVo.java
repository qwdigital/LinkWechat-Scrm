package com.linkwechat.domain.wecom.vo.kf;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 客服
 * @date 2021/12/13 13:57
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeCustomerInfoVo extends WeResultVo {

    /**
     * 微信客户列表
     */
    private List<WeKfCustomer> customerList;

    /**
     * 无效的客户id
     */
    private List<String> invalidExternalUserId;


    @Data
    public static class WeKfCustomer {
        /**
         * 外部联系人id
         */
        private String externalUserId;

        /**
         * 昵称
         */
        private String nickName;

        /**
         * 头像
         */
        private String avatar;

        /**
         * 性别
         */
        private Integer gender;

        /**
         * unionid
         */
        private String unionId;
    }

}
