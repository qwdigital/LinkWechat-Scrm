package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerMessgaeResult;
import com.linkwechat.wecom.domain.vo.WeCustomerMessageResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发消息  微信消息发送结果表 Mapper接口
 *
 * @author kewen
 * @date 2020-12-08
 */
public interface WeCustomerMessgaeResultMapper extends BaseMapper<WeCustomerMessgaeResult> {


    /**
     * 更新群发消息结果
     *
     * @param messageId 微信消息表id
     * @param chatId 外部客户群id
     * @param externalUserid 外部联系人userid
     * @param status 发送状态 0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     * @param sendTime 发送时间，发送状态为1时返回(为时间戳的形式)
     * @return int
     */
    int updateWeCustomerMessgaeResult(@Param("messageId") Long messageId,@Param("chatId") String chatId,@Param("externalUserid") String externalUserid
            ,@Param("status") String status,@Param("sendTime") String sendTime);

    /**
     * 查询微信消息发送情况
     *
     * @param messageId  微信消息表id
     * @param status 发送状态 0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     * @return {@link WeCustomerMessageResultVo}s
     */
    List<WeCustomerMessageResultVo> customerMessagePushs(@Param("messageId") Long messageId,@Param("status") String status);

    /**
     * 检查是否已经同步群发结果
     *
     * @param messageId 微信消息表id
     * @return 已发送消息数
     */
    int  checkSendStatus(@Param("messageId") Long messageId);

    /**
     * 批量保存映射关系
     *
     * @param weCustomerMessgaeResults 映射关系列表信息
     * @return int 结果
     */
    int batchInsert(@Param("customers") List<WeCustomerMessgaeResult> weCustomerMessgaeResults);

}
