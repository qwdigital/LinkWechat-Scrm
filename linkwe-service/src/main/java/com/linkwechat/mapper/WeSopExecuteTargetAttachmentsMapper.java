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

    List<WeSopPushTaskDto> findWeSopPushTaskDto(@Param("targetType") Integer targetType,@Param("sendType") Integer sendType,@Param("isExpiringSoon") boolean isExpiringSoon);

    List<WeSopPushTaskDto>  findWeSopPushTaskDtoBySopId(@Param("sopBaseId") String sopBaseId);
}
