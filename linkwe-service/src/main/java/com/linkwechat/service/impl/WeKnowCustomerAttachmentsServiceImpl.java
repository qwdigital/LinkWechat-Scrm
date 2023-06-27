package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.know.WeKnowCustomerAttachments;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.sop.WeSopAttachments;
import com.linkwechat.mapper.WeKnowCustomerAttachmentsMapper;
import com.linkwechat.service.IWeKnowCustomerAttachmentsService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author robin
* @description 针对表【we_know_customer_attachments(活码附件表)】的数据库操作Service实现
* @createDate 2023-01-09 18:13:57
*/
@Service
public class WeKnowCustomerAttachmentsServiceImpl extends ServiceImpl<WeKnowCustomerAttachmentsMapper, WeKnowCustomerAttachments>
implements IWeKnowCustomerAttachmentsService {

    @Override
    public void saveBatchByKnowCustomerId(Long knowCustomerId, List<WeMessageTemplate> attachments) {
        List<WeKnowCustomerAttachments> attachmentsList = Optional.ofNullable(attachments)
                .orElseGet(ArrayList::new).stream().map(weQrAttachmentsQuery -> {
                    WeKnowCustomerAttachments weKnowCustomerAttachments = new WeKnowCustomerAttachments();
                    BeanUtil.copyProperties(weQrAttachmentsQuery, weKnowCustomerAttachments);
                    weKnowCustomerAttachments.setKnowCustomerId(knowCustomerId);
                    return weKnowCustomerAttachments;
                }).collect(Collectors.toList());
        saveOrUpdateBatch(attachmentsList);
    }

    @Override
    public void updateBatchByKnowCustomerId(Long knowCustomerId, List<WeMessageTemplate> attachments) {

        if(CollectionUtil.isNotEmpty(attachments)){
            List<WeMessageTemplate> weMessageTemplates
                    = attachments.stream().filter(attachment -> attachment.getId() != null).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(weMessageTemplates)){
                this.remove(new LambdaQueryWrapper<WeKnowCustomerAttachments>()
                        .eq(WeKnowCustomerAttachments::getKnowCustomerId,knowCustomerId)
                        .notIn(WeKnowCustomerAttachments::getId,weMessageTemplates.stream().map(WeMessageTemplate::getId).collect(Collectors.toList())));

            }

        }else{
            this.remove(new LambdaQueryWrapper<WeKnowCustomerAttachments>()
                    .eq(WeKnowCustomerAttachments::getKnowCustomerId,knowCustomerId));
        }


        saveBatchByKnowCustomerId(knowCustomerId,attachments);


    }


}
