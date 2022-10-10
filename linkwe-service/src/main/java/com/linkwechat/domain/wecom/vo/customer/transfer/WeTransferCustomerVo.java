package com.linkwechat.domain.wecom.vo.customer.transfer;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 分配在职成员的客户
 * @date 2021/12/2 16:11
 **/
@Data
public class WeTransferCustomerVo extends WeResultVo {
    /**
     * 客户
     */
    private List<TransferCustomerVo> customer;


    @Data
    public static class TransferCustomerVo{
        /**
         * 客户的external_userid
         */
        private String externalUserId;
        /**
         * 0表示成功发起接替,待24小时后自动接替,并不代表最终接替成功
         * 对此客户进行分配的结果, 具体可参考全局错误码,
         */
        private Integer errCode;

        /**
         * 接替状态， 1-接替完毕 2-等待接替 3-客户拒绝 4-接替成员客户达到上限 5-无接替记录
         */
        private Integer status;

        /**
         * 接替客户的时间，如果是等待接替状态，则为未来的自动接替时间
         */
        private Long takeoverTime;
    }
}
