package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeGroupMessageAttachments;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 群发消息附件Service接口
 *
 * @author ruoyi
 * @date 2021-10-19
 */
public interface IWeGroupMessageAttachmentsService extends IService<WeGroupMessageAttachments> {

    /**
     * 查询列表
     */
    List<WeGroupMessageAttachments> queryList(WeGroupMessageAttachments weGroupMessageAttachments);
}
