package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroupMessageTask;

import java.util.List;

/**
 * 群发消息成员发送任务表Mapper接口
 *
 * @author ruoyi
 * @date 2021-10-19
 */
public interface WeGroupMessageTaskMapper extends BaseMapper<WeGroupMessageTask> {

    List<WeGroupMessageTask> groupMsgTaskList(WeGroupMessageTask task);
}
