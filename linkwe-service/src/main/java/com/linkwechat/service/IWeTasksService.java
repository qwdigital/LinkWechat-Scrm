package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.task.entity.WeTasks;
import com.linkwechat.domain.task.vo.WeTasksVO;

import java.util.List;

/**
 * <p>
 * 待办任务 服务类
 * </p>
 *
 * @author WangYX
 * @since 2023-07-20
 */
public interface IWeTasksService extends IService<WeTasks> {

    /**
     * 我的任务
     *
     * @return {@link List< WeTasksVO>}
     * @author WangYX
     * @date 2023/07/20 18:01
     */
    List<WeTasksVO> myList();

    /**
     * 历史人物
     *
     * @return {@link List< WeTasksVO>}
     * @author WangYX
     * @date 2023/07/20 18:02
     */
    List<WeTasksVO> history();

    void save();


}
