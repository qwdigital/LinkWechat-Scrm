package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.qr.WeQrAttachments;

import java.util.List;

/**
 * 活码素材表(WeQrAttachments)表服务接口
 *
 * @author makejava
 * @since 2021-11-07 01:29:12
 */
public interface IWeQrAttachmentsService extends IService<WeQrAttachments> {

    /**
     * 通过活码id批量保存活码欢迎语素材
     *
     * @param qrId        活码id
     * @param businessType  业务类型 1-员工活码 2-门店导购 3-拉新活码
     * @param attachments 素材列表
     */
    void saveBatchByQrId(Long qrId, Integer businessType, List<WeMessageTemplate> attachments);


    /**
     * 通过活码id批量修改活码欢迎语素材
     *
     * @param qrId        活码id
     * @param businessType  业务类型 1-员工活码 2-门店导购 3-拉新活码
     * @param attachments 素材列表
     */
    void updateBatchByQrId(Long qrId, Integer businessType, List<WeMessageTemplate> attachments);

    List<WeQrAttachments> getAttachmentsList(Long qrId, Integer businessType);
}
