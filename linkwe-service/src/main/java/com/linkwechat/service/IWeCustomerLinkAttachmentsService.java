package com.linkwechat.service;

import com.linkwechat.domain.WeCustomerLinkAttachments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.media.WeMessageTemplate;

import java.util.List;

/**
* @author robin
* @description 针对表【we_customer_link_attachments(活码附件表)】的数据库操作Service
* @createDate 2023-07-04 17:41:13
*/
public interface IWeCustomerLinkAttachmentsService extends IService<WeCustomerLinkAttachments> {

    /**
     * 通过活码id批量保存活码欢迎语素材
     *
     * @param customerLinkId        获客助手id
     * @param attachments 素材列表
     */
    void saveBatchByCustomerLinkId(Long customerLinkId, List<WeMessageTemplate> attachments);


    /**
     * 通过活码id批量修改活码欢迎语素材
     *
     * @param customerLinkId        获客助手id
     * @param attachments 素材列表
     */
    void updateBatchByCustomerLinkId(Long customerLinkId, List<WeMessageTemplate> attachments);

}
