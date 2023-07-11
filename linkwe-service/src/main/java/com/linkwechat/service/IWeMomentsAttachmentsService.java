package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.moments.dto.MomentsListDetailResultDto;
import com.linkwechat.domain.moments.entity.WeMomentsAttachments;

import java.util.List;

/**
 * 朋友圈附件 服务类
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:06
 */
public interface IWeMomentsAttachmentsService extends IService<WeMomentsAttachments> {

    /**
     * 添加朋友圈任务素材
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param materialIds   素材Id集合
     * @return
     * @author WangYX
     * @date 2023/06/08 16:45
     */
    public void addMomentsAttachments(Long momentsTaskId, List<Long> materialIds);


    /**
     * 企微同步时添加附件
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param moment        企微朋友圈
     * @return
     * @author WangYX
     * @date 2023/06/12 13:59
     */
    public void syncAddMomentsAttachments(Long momentsTaskId, MomentsListDetailResultDto.Moment moment);

}
