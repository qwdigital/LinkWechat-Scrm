package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerMessage;

/**
 * 群发消息  微信消息表Mapper接口
 *
 * @author kewen
 * @date 2020-12-08
 */
public interface WeCustomerMessageMapper extends BaseMapper<WeCustomerMessage> {
    int updateWeCustomerMessageMsgIdById(WeCustomerMessage customerMessage);
}
