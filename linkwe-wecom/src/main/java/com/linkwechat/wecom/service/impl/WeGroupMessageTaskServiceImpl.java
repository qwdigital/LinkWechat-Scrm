package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerMessagePushClient;
import com.linkwechat.wecom.domain.WeGroupMessageTask;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgListDto;
import com.linkwechat.wecom.domain.dto.message.WeGroupMsgTaskDto;
import com.linkwechat.wecom.domain.query.WeGetGroupMsgListQuery;
import com.linkwechat.wecom.mapper.WeGroupMessageTaskMapper;
import com.linkwechat.wecom.service.IWeGroupMessageTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 群发消息成员执行结果Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-19
 */
@Service
public class WeGroupMessageTaskServiceImpl extends ServiceImpl<WeGroupMessageTaskMapper, WeGroupMessageTask> implements IWeGroupMessageTaskService {
    @Autowired
    private WeCustomerMessagePushClient messagePushClient;

    @Override
    public WeGroupMsgListDto getGroupMsgTask(String msgId, String cursor) {
        WeGetGroupMsgListQuery query = new WeGetGroupMsgListQuery();
        query.setMsgid(msgId);
        query.setCursor(cursor);
        WeGroupMsgListDto groupMsgTask = messagePushClient.getGroupMsgTask(query);
        if (groupMsgTask != null && ObjectUtil.equal(0, groupMsgTask.getErrcode())) {
            if (StringUtils.isNotEmpty(groupMsgTask.getNextCursor())) {
                WeGroupMsgListDto clildMsgList = getGroupMsgTask(msgId, groupMsgTask.getNextCursor());
                List<WeGroupMsgTaskDto> taskList = clildMsgList.getTaskList();
                taskList.addAll(clildMsgList.getTaskList());
                groupMsgTask.setTaskList(taskList);
                groupMsgTask.setNextCursor(clildMsgList.getNextCursor());
            }
        }
        return groupMsgTask;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrUpdateBatchByCondition(List<WeGroupMessageTask> taskList) {
        if (CollectionUtil.isNotEmpty(taskList)) {
            taskList.forEach(item -> {
                LambdaQueryWrapper<WeGroupMessageTask> wrapper = new LambdaQueryWrapper<>();
                if(item.getMsgTemplateId() != null){
                    wrapper.eq(WeGroupMessageTask::getMsgTemplateId,item.getMsgTemplateId());
                }
                wrapper.eq(WeGroupMessageTask::getUserId, item.getUserId());
                if (!this.update(item,wrapper)){
                    this.save(item);
                }
            });
        }
    }

    @Override
    public List<WeGroupMessageTask> groupMsgTaskList(WeGroupMessageTask task) {
        return this.baseMapper.groupMsgTaskList(task);
    }
}
