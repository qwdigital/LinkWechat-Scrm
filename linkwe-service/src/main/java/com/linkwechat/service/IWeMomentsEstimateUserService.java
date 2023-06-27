package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.moments.entity.WeMomentsEstimateUser;

import java.util.List;

/**
 * 预估朋友圈执行员工 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/26 19:26
 */
public interface IWeMomentsEstimateUserService extends IService<WeMomentsEstimateUser> {

    /**
     * 批量新增预估朋友圈执行员工
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @param weUserIds   员工Id集合
     * @author WangYX
     * @date 2023/06/26 19:31
     */
    void batchInsert(Long weMomentsTaskId, List<String> weUserIds);


}
