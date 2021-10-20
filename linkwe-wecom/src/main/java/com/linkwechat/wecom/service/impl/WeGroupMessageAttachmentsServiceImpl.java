package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeGroupMessageAttachments;
import com.linkwechat.wecom.mapper.WeGroupMessageAttachmentsMapper;
import com.linkwechat.wecom.service.IWeGroupMessageAttachmentsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 群发消息附件Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-19
 */
@Service
public class WeGroupMessageAttachmentsServiceImpl extends ServiceImpl<WeGroupMessageAttachmentsMapper, WeGroupMessageAttachments> implements IWeGroupMessageAttachmentsService {

    @Override
    public List<WeGroupMessageAttachments> queryList(WeGroupMessageAttachments weGroupMessageAttachments) {
        LambdaQueryWrapper<WeGroupMessageAttachments> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(weGroupMessageAttachments.getMsgId())){
            lqw.eq(WeGroupMessageAttachments::getMsgId ,weGroupMessageAttachments.getMsgId());
        }
        if (StringUtils.isNotBlank(weGroupMessageAttachments.getMsgType())){
            lqw.eq(WeGroupMessageAttachments::getMsgType ,weGroupMessageAttachments.getMsgType());
        }
        if (StringUtils.isNotBlank(weGroupMessageAttachments.getContent())){
            lqw.eq(WeGroupMessageAttachments::getContent ,weGroupMessageAttachments.getContent());
        }
        if (StringUtils.isNotBlank(weGroupMessageAttachments.getMediaId())){
            lqw.eq(WeGroupMessageAttachments::getMediaId ,weGroupMessageAttachments.getMediaId());
        }
        if (StringUtils.isNotBlank(weGroupMessageAttachments.getTitle())){
            lqw.eq(WeGroupMessageAttachments::getTitle ,weGroupMessageAttachments.getTitle());
        }
        if (StringUtils.isNotBlank(weGroupMessageAttachments.getDescription())){
            lqw.eq(WeGroupMessageAttachments::getDescription ,weGroupMessageAttachments.getDescription());
        }
        if (StringUtils.isNotBlank(weGroupMessageAttachments.getLinkUrl())){
            lqw.eq(WeGroupMessageAttachments::getLinkUrl ,weGroupMessageAttachments.getLinkUrl());
        }
        if (StringUtils.isNotBlank(weGroupMessageAttachments.getPicUrl())){
            lqw.eq(WeGroupMessageAttachments::getPicUrl ,weGroupMessageAttachments.getPicUrl());
        }
        if (StringUtils.isNotBlank(weGroupMessageAttachments.getAppId())){
            lqw.eq(WeGroupMessageAttachments::getAppId ,weGroupMessageAttachments.getAppId());
        }
        return this.list(lqw);
    }
}
