package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.live.WeLiveAttachments;
import com.linkwechat.domain.media.WeMessageTemplate;
import java.util.List;

/**
* @author robin
* @description 针对表【we_live_attachments(活码附件表)】的数据库操作Service
* @createDate 2022-10-26 22:40:45
*/
public interface IWeLiveAttachmentsService extends IService<WeLiveAttachments> {

    void saveOrUpdate(Long liveId,List<WeMessageTemplate> attachments);


    /**
     * 存储的对象转化为发送对象
     * @param weLiveAttachments
     * @return
     */
    List<WeMessageTemplate> weLiveAttachmentsToTemplate(List<WeLiveAttachments> weLiveAttachments);

}
