package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.moments.entity.WeMomentsInteracte;

/**
 * 朋友圈互动
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:07
 */
public interface IWeMomentsInteracteService extends IService<WeMomentsInteracte> {

    /**
     * 同步朋友圈互动情况
     *
     * @param momentsTaskId
     * @param momentsId
     * @return
     * @author WangYX
     * @date 2023/06/12 17:23
     */
    void syncAddWeMomentsInteracte(Long momentsTaskId, String momentsId);

}
