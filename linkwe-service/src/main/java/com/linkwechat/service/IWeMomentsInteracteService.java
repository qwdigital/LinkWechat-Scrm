package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.moments.entity.WeMomentsInteracte;

import java.util.List;

/**
 * 朋友圈互动
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:07
 */
public interface IWeMomentsInteracteService extends IService<WeMomentsInteracte> {

    /**
     * 新增同步朋友圈互动情况
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     企微朋友圈Id
     * @return
     * @author WangYX
     * @date 2023/06/12 17:23
     */
    void syncAddWeMomentsInteracte(Long momentsTaskId, String momentsId);

    /**
     * 更新朋友圈互动情况
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     企微朋友圈Id
     * @return
     * @author WangYX
     * @date 2023/06/26 18:13
     */
    void syncUpdateWeMomentsInteract(Long momentsTaskId, String momentsId);


    /**
     * 客户互动标签
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @param list            互动数据
     * @return
     * @author WangYX
     * @date 2023/06/26 17:10
     */
    void interactTag(Long weMomentsTaskId, List<WeMomentsInteracte> list);

}
