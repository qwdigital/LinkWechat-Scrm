package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.sop.WeSopExecuteTargetAttachments;


/**
* @author robin
* @description 针对表【we_sop_execute_target_attachments(目标执行内容)】的数据库操作Service
* @createDate 2022-09-13 16:26:00
*/
public interface IWeSopExecuteTargetAttachmentsService extends IService<WeSopExecuteTargetAttachments> {

    /**
     *  企业微信官方api推送提醒
     */
    void weChatPushTypeSopTaskTip(String sopBaseId);


    /**
     * 手动方式推送提醒
     * @param isExpiringSoon false:每日一次的提醒;true:每日即将到期任务提醒
     */
    void manualPushTypeSopTaskTip(boolean isExpiringSoon);






}
