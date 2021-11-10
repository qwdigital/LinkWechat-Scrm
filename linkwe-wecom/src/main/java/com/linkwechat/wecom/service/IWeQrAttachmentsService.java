package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeMessageTemplate;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.domain.WeQrAttachments;

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
     * @param qrId 活码id
     * @param attachments 素材列表
     */
    void saveBatchByQrId(Long qrId, List<WeMessageTemplate> attachments);

    /**
     * 通过活码id批量删除活码欢迎语素材
     * @param qrIds 活码id
     * @return
     */
    Boolean delBatchByQrIds(List<Long> qrIds);

    /**
     * 通过活码id批量修改活码欢迎语素材
     * @param qrId 活码id
     * @param attachments 素材列表
     */
    void updateBatchByQrId(Long qrId, List<WeMessageTemplate> attachments);
}
