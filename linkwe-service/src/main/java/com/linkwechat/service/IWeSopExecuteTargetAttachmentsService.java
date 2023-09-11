package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.sop.WeSopExecuteTargetAttachments;
import com.linkwechat.domain.sop.dto.WeSopPushTaskDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


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

    /**
     * 根据企微用户Id获取当天待推送数据
     *
     * @param weUserId   员工企微Id
     * @param targetType 目标类型1:客户 2:群
     * @param sendType   1:企业微信发送;2:手动发送
     * @return {@link List <  WeSopPushTaskDto >}
     * @author WangYX
     * @date 2023/08/09 15:23
     */
    List<WeSopPushTaskDto> findWeSopPushTaskDtoByWeUserId(@Param("weUserId") String weUserId, @Param("targetType") Integer targetType, @Param("sendType") Integer sendType);


}
