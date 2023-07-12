package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordAttachment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 线索跟进记录内容附件表数据库访问层
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:47
 */
@Mapper
public interface WeLeadsFollowRecordAttachmentMapper extends BaseMapper<WeLeadsFollowRecordAttachment> {

}

