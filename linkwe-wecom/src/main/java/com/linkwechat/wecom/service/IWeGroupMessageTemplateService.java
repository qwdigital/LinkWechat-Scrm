package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.wecom.domain.WeGroupMessageSendResult;
import com.linkwechat.wecom.domain.WeGroupMessageTask;
import com.linkwechat.wecom.domain.WeGroupMessageTemplate;
import com.linkwechat.wecom.domain.query.WeAddGroupMessageQuery;
import com.linkwechat.wecom.domain.vo.WeGroupMessageDetailVo;

import java.util.List;

/**
 * 群发消息模板Service接口
 *
 * @author ruoyi
 * @date 2021-10-27
 */
public interface IWeGroupMessageTemplateService extends IService<WeGroupMessageTemplate> {

    /**
     * 查询列表
     */
    List<WeGroupMessageTemplate> queryList(WeGroupMessageTemplate weGroupMessageTemplate);

    /**
     * 获取群发消息详情
     * @param id 模板id
     * @return
     */
    WeGroupMessageDetailVo getGroupMsgTemplateDetail(Long id);


    /**
     * 创建群发消息
     * @param query
     */
    void addGroupMsgTemplate(WeAddGroupMessageQuery query) throws Exception;

    /**
     * 取消定时发送
     * @param asList
     */
    void cancelByIds(List<Long> asList);

    /**
     * 同步消息发送结果
     * @param asList
     */
    void syncGroupMsgSendResultByIds(List<Long> asList);

    /**
     * 群发成员发送任务列表
     * @param task
     */
    List<WeGroupMessageTask> groupMsgTaskList(WeGroupMessageTask task);

    /**
     * 企业群发成员执行结果
     * @param sendResult
     */
    List<WeGroupMessageSendResult> groupMsgSendResultList(WeGroupMessageSendResult sendResult);
}
