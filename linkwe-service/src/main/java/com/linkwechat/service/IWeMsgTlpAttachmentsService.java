package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeMsgTlpAttachments;
import com.linkwechat.domain.media.WeMessageTemplate;

import java.util.List;

/**
 * 欢迎语模板素材表(WeMsgTlpAttachments)
 *
 * @author danmo
 * @since 2022-03-28 10:22:28
 */
public interface IWeMsgTlpAttachmentsService extends IService<WeMsgTlpAttachments> {

    /**
     * 新增欢迎语模板素材
     *
     * @param templateId
     * @param attachments
     * @return
     */
    void addMsgTlpAttachments(Long templateId, List<WeMessageTemplate> attachments);

    /**
     * 修改欢迎语模板素材
     *
     * @param templateId
     * @param attachments
     * @return
     */
    void updateMsgTlpAttachments(Long templateId, List<WeMessageTemplate> attachments);

    /**
     * 删除模板素材
     * @param templateIds
     */
    void delMsgTlpAttachments(List<Long> templateIds);
}
