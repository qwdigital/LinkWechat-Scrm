package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeCustomerLinkAttachments;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.service.IWeCustomerLinkAttachmentsService;
import com.linkwechat.mapper.WeCustomerLinkAttachmentsMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author robin
* @description 针对表【we_customer_link_attachments(活码附件表)】的数据库操作Service实现
* @createDate 2023-07-04 17:41:13
*/
@Service
public class WeCustomerLinkAttachmentsServiceImpl extends ServiceImpl<WeCustomerLinkAttachmentsMapper, WeCustomerLinkAttachments>
    implements IWeCustomerLinkAttachmentsService {

    @Override
    public void saveBatchByCustomerLinkId(Long customerLinkId, List<WeMessageTemplate> attachments) {
        List<WeCustomerLinkAttachments> attachmentsList = Optional.ofNullable(attachments)
                .orElseGet(ArrayList::new).stream().map(weQrAttachmentsQuery -> {
                    WeCustomerLinkAttachments weCustomerLinkAttachments = new WeCustomerLinkAttachments();
                    BeanUtil.copyProperties(weQrAttachmentsQuery, weCustomerLinkAttachments);
                    weCustomerLinkAttachments.setCustomerLinkId(customerLinkId);
                    return weCustomerLinkAttachments;
                }).collect(Collectors.toList());
        saveBatch(attachmentsList);

    }

    @Override
    public void updateBatchByCustomerLinkId(Long customerLinkId, List<WeMessageTemplate> attachments) {
        this.remove(new LambdaQueryWrapper<WeCustomerLinkAttachments>()
                .in(WeCustomerLinkAttachments::getCustomerLinkId, ListUtil.toList(customerLinkId)));
        saveBatchByCustomerLinkId(customerLinkId,attachments);
    }
}




