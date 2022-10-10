package com.linkwechat.domain.wecom.vo.customer.transfer;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 分配离职成员的客户群
 * @date 2021/12/2 16:11
 **/
@Data
public class WeTransferGroupChatVo extends WeResultVo {
    /**
     * 客户
     */
    private List<TransferGroupChat> failedChatList;


    @Data
    public static class TransferGroupChat extends WeResultVo{
        /**
         * 没能成功继承的群ID
         */
        private String chatId;

    }
}
