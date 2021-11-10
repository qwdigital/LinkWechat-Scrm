package com.linkwechat.wecom.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeMessageTemplate;
import com.linkwechat.wecom.domain.WeQrAttachments;
import com.linkwechat.wecom.mapper.WeQrAttachmentsMapper;
import com.linkwechat.wecom.service.IWeQrAttachmentsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 活码附件表(WeQrAttachments)表服务实现类
 *
 * @author makejava
 * @since 2021-11-07 01:29:13
 */
//@DS("db1")
@Service
public class IWeQrAttachmentsServiceImpl extends ServiceImpl<WeQrAttachmentsMapper, WeQrAttachments> implements IWeQrAttachmentsService {

    @Override
    public void saveBatchByQrId(Long qrId, List<WeMessageTemplate> attachments) {
        List<WeQrAttachments> attachmentsList = Optional.ofNullable(attachments)
                .orElseGet(ArrayList::new).stream().map(weQrAttachmentsQuery -> {
                    WeQrAttachments weQrAttachments = new WeQrAttachments();
                    BeanUtil.copyProperties(weQrAttachmentsQuery, weQrAttachments);
                    weQrAttachments.setQrId(qrId);
                    return weQrAttachments;
                }).collect(Collectors.toList());
        saveBatch(attachmentsList);
    }

    @Override
    public Boolean delBatchByQrIds(List<Long> qrIds) {
        WeQrAttachments weQrAttachments = new WeQrAttachments();
        weQrAttachments.setDelFlag(1);
        return this.update(weQrAttachments,new LambdaQueryWrapper<WeQrAttachments>().in(WeQrAttachments::getQrId,qrIds));
    }

    @Override
    public void updateBatchByQrId(Long qrId, List<WeMessageTemplate> attachments) {
        delBatchByQrIds(ListUtil.toList(qrId));
        saveBatchByQrId(qrId,attachments);
    }
}
