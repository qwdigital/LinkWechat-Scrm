package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeGroupMessageSendResult;

/**
 * 群发消息成员执行结果表(WeGroupMessageSendResult)
 *
 * @author danmo
 * @since 2022-04-06 22:29:04
 */
@Repository()
@Mapper
public interface WeGroupMessageSendResultMapper extends BaseMapper<WeGroupMessageSendResult> {

    List<WeGroupMessageSendResult> groupMsgSendResultList(WeGroupMessageSendResult sendResult);
}

