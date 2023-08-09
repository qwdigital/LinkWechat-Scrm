package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.sop.WeSopExecuteTargetAttachments;
import com.linkwechat.domain.sop.dto.WeSopPushTaskDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author robin
 * @description 针对表【we_sop_execute_target_attachments(目标执行内容)】的数据库操作Mapper
 * @createDate 2022-09-13 16:26:00
 * @Entity generator.domain.WeSopExecuteTargetAttachments
 */
public interface WeSopExecuteTargetAttachmentsMapper extends BaseMapper<WeSopExecuteTargetAttachments> {

    List<WeSopPushTaskDto> findWeSopPushTaskDto(@Param("targetType") Integer targetType, @Param("sendType") Integer sendType, @Param("isExpiringSoon") boolean isExpiringSoon);

    List<WeSopPushTaskDto> findWeSopPushTaskDtoBySopId(@Param("sopBaseId") String sopBaseId);

    /**
     * 根据企微用户Id获取当天待推送数据
     *
     * @param weUserId   员工企微Id
     * @param targetType 目标类型1:客户 2:群
     * @param sendType   1:企业微信发送;2:手动发送
     * @return {@link List< WeSopPushTaskDto>}
     * @author WangYX
     * @date 2023/08/09 15:23
     */
    List<WeSopPushTaskDto> findWeSopPushTaskDtoByWeUserId(@Param("weUserId") String weUserId, @Param("targetType") Integer targetType, @Param("sendType") Integer sendType);

}
