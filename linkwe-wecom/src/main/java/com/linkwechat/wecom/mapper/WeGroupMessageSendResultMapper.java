package com.linkwechat.wecom.mapper;

import com.linkwechat.wecom.domain.WeGroupMessageSendResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 群发消息成员执行结果Mapper接口
 *
 * @author ruoyi
 * @date 2021-10-19
 */
public interface WeGroupMessageSendResultMapper extends BaseMapper<WeGroupMessageSendResult> {

    List<WeGroupMessageSendResult> groupMsgSendResultList(WeGroupMessageSendResult sendResult);
}
