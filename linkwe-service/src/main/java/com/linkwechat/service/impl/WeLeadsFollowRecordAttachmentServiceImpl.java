package com.linkwechat.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordAttachment;
import com.linkwechat.mapper.WeLeadsFollowRecordAttachmentMapper;
import com.linkwechat.service.IWeLeadsFollowRecordAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 线索跟进记录附件表服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 10:00
 */
@Slf4j
@Service
public class WeLeadsFollowRecordAttachmentServiceImpl extends ServiceImpl<WeLeadsFollowRecordAttachmentMapper, WeLeadsFollowRecordAttachment> implements IWeLeadsFollowRecordAttachmentService {

}

