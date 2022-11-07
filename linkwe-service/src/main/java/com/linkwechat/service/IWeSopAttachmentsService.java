package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.sop.WeSopAttachments;
import com.linkwechat.domain.sop.vo.WeSopAttachmentVo;

import java.util.List;


/**
* @author robin
* @description 针对表【we_sop_attachments(sop素材附件)】的数据库操作Service
* @createDate 2022-09-07 15:23:33
*/
public interface IWeSopAttachmentsService extends IService<WeSopAttachments> {

    /**
     * 通过活码id批量保存活码欢迎语素材
     * @param sopPushTimeId 发布时间id
     * @param sopBaseId sop主键
     * @param attachments 素材列表
     */
    void saveBatchBySopBaseId(Long sopPushTimeId,Long sopBaseId, List<WeMessageTemplate> attachments);


    /**
     * 通过活码id批量修改活码欢迎语素材
     * @param sopPushTimeId 发布时间id
     * @param sopBaseId sop主键
     * @param attachments 素材列表
     */
    void updateBatchBySopBaseId(Long sopPushTimeId,Long sopBaseId, List<WeMessageTemplate> attachments);


    /**
     * 根据sop主键获取附件
     * @param sopBaseId
     * @return
     */
    List<WeSopAttachmentVo> findWeSopAttachmentVos(Long sopBaseId);


    /**
     * 存储的对象转化为发送对象
     * @param weSopAttachments
     * @return
     */
    List<WeMessageTemplate> weSopAttachmentsToTemplate(List<WeSopAttachments> weSopAttachments);

}
