package com.linkwechat.domain.wecom.vo.customer.moment;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 获取企业全部的发表列表
 * @date 2021/12/2 16:11
 **/
@ApiModel
@Data
public class WeMomentCustomerListVo extends WeResultVo {
    /**
     * 成员可见客户列表
     */
    private List<WeMomentCustomer> customerList;

    @Data
    public static class WeMomentCustomer{
        /**
         * 发表成员用户userid
         */
        private String userId;

        /**
         * 发送成功的外部联系人userid
         */
        private String externalUserId;
    }
}
