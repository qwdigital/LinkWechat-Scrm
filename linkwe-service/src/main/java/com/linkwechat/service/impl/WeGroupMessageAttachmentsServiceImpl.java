package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeGroupMessageAttachments;
import com.linkwechat.mapper.WeGroupMessageAttachmentsMapper;
import com.linkwechat.service.IWeGroupMessageAttachmentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 群发消息附件表(WeGroupMessageAttachments)
 *
 * @author danmo
 * @since 2022-04-06 22:29:03
 */
@Slf4j
@Service
public class WeGroupMessageAttachmentsServiceImpl extends ServiceImpl<WeGroupMessageAttachmentsMapper, WeGroupMessageAttachments> implements IWeGroupMessageAttachmentsService {

}
