package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.moments.entity.WeMomentsTaskRelation;

/**
 * 朋友圈任务和企微朋友圈关联表 服务类
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/09 10:57
 */
public interface IWeMomentsTaskRelationService extends IService<WeMomentsTaskRelation> {

    /**
     * 添加朋友圈任务和企微朋友圈关联
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @param momentsId       朋友圈Id
     * @author WangYX
     * @date 2023/06/12 15:10
     */
    void syncAddRelation(Long weMomentsTaskId, String momentsId);

}
