package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.sop.WeSopAttachments;
import com.linkwechat.domain.sop.vo.WeSopAttachmentVo;
import com.linkwechat.mapper.WeSopAttachmentsMapper;
import com.linkwechat.service.IWeSopAttachmentsService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author robin
* @description 针对表【we_sop_attachments(sop素材附件)】的数据库操作Service实现
* @createDate 2022-09-07 15:23:33
*/
@Service
public class WeSopAttachmentsServiceImpl extends ServiceImpl<WeSopAttachmentsMapper, WeSopAttachments>
implements IWeSopAttachmentsService {

    @Override
    public void saveBatchBySopBaseId(Long sopPushTimeId,Long sopBaseId, List<WeMessageTemplate> attachments) {
        List<WeSopAttachments> attachmentsList = Optional.ofNullable(attachments)
                .orElseGet(ArrayList::new).stream().map(weQrAttachmentsQuery -> {
                    WeSopAttachments weQrAttachments = new WeSopAttachments();
                    BeanUtil.copyProperties(weQrAttachmentsQuery, weQrAttachments);
                    weQrAttachments.setSopBaseId(sopBaseId);
                    weQrAttachments.setSopPushTimeId(sopPushTimeId);
                    return weQrAttachments;
                }).collect(Collectors.toList());
        saveOrUpdateBatch(attachmentsList);
    }

    @Override
    public void updateBatchBySopBaseId(Long sopPushTimeId,Long sopBaseId, List<WeMessageTemplate> attachments) {

        if(CollectionUtil.isNotEmpty(attachments)){
            List<WeMessageTemplate> weMessageTemplates
                    = attachments.stream().filter(attachment -> attachment.getId() != null).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(weMessageTemplates)){
                this.remove(new LambdaQueryWrapper<WeSopAttachments>()
                                .eq(WeSopAttachments::getSopPushTimeId,sopPushTimeId)
                                .eq(WeSopAttachments::getSopBaseId,sopBaseId)
                        .notIn(WeSopAttachments::getId,weMessageTemplates.stream().map(WeMessageTemplate::getId).collect(Collectors.toList())));

            }

        }else{
            this.remove(new LambdaQueryWrapper<WeSopAttachments>()
                    .eq(WeSopAttachments::getSopBaseId,sopBaseId));
        }




        saveBatchBySopBaseId(sopPushTimeId,sopBaseId,attachments);
    }

    @Override
    public List<WeSopAttachmentVo> findWeSopAttachmentVos(Long sopBaseId) {
        return null;
    }

    @Override
    public List<WeMessageTemplate> weSopAttachmentsToTemplate(List<WeSopAttachments> weSopAttachments) {
        List<WeMessageTemplate> weMessageTemplates = Optional.ofNullable(weSopAttachments)
                .orElseGet(ArrayList::new).stream().map(weSopAttachment -> {
                    WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
                    BeanUtil.copyProperties(weSopAttachment, weMessageTemplate);
                    return weMessageTemplate;
                }).collect(Collectors.toList());
        return weMessageTemplates;
    }


}
