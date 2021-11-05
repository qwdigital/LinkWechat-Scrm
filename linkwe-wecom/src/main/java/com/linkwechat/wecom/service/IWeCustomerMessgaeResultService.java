package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeCustomerList;
import com.linkwechat.wecom.domain.WeCustomerMessgaeResult;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import com.linkwechat.wecom.domain.vo.WeCustomerMessageResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发消息  微信消息发送结果表 we_customer_messgaeResult
 *
 * @author kewen
 * @date 2020-12-12
 */
public interface IWeCustomerMessgaeResultService extends IService<WeCustomerMessgaeResult> {

    /**
     * 查询微信消息发送情况
     *
     * @param messageId  微信消息表id
     * @param status 发送状态 0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     * @return {@link WeCustomerMessageResultVo}s
     */
    List<WeCustomerMessageResultVo> customerMessagePushs(Long messageId, String status);

    /**
     * 保存映射关系
     *
     * @param customerMessagePushDto
     * @param messageId
     * @return
     */
    int workerMappingCustomer(CustomerMessagePushDto customerMessagePushDto, long messageId, List<WeCustomerList> customers, List<WeGroup> groups);

}
