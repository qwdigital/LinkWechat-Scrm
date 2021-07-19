package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerMessage;
import com.linkwechat.wecom.domain.dto.message.DetailMessageStatusResultDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发消息  微信消息表Mapper接口
 *
 * @author kewen
 * @date 2020-12-08
 */
public interface WeCustomerMessageMapper extends BaseMapper<WeCustomerMessage> {

    int updateWeCustomerMessageMsgIdById(WeCustomerMessage customerMessage);

    /**
     *
     * @param messageId 微信消息表主键id
     * @param actualSend 实际发送消息数（客户对应多少人 客户群对应多个群）
     * @return int
     */
    int updateWeCustomerMessageActualSend(@Param("messageId") Long messageId,@Param("actualSend") Integer actualSend);

    /**
     * 更新消息发送状态
     * @param messageId id
     * @param status 消息发送状态 0 未发送  1 已发送
     * @return int
     */
    int updateWeCustomerMessageCheckStatusById(@Param("messageId") Long messageId,@Param("status") String status);



}
