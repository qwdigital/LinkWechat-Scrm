package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomerMessage;
import org.apache.ibatis.annotations.Param;

/**
 * 群发消息  微信消息表service接口
 *
 * @author kewen
 * @date 2020-12-12
 */
public interface IWeCustomerMessageService extends IService<WeCustomerMessage> {

    /**
     * @param customerMessage
     * @return
     */
    int updateWeCustomerMessageMsgId(WeCustomerMessage customerMessage);

    /**
     *
     * @param messageId 微信消息表主键id
     * @param actualSend 实际发送消息数（客户对应多少人 客户群对应多个群）
     * @return int
     */
    int updateWeCustomerMessageActualSend( Long messageId, Integer actualSend);


}
