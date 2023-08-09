package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.task.entity.WeTasks;
import com.linkwechat.domain.task.query.WeTasksRequest;
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

    /**
     * 新增
     *
     * @param weTasks 代办任务
     * @author WangYX
     * @date 2023/07/21 14:31
     */
    void add(WeTasks weTasks);

    /**
     * 完成待办任务
     *
     * @param id 任务Id
     * @author WangYX
     * @date 2023/07/21 14:46
     */
    void finish(Long id);


    /**
     * 添加线索长时间未跟进
     * <p>
     * 线索领取后，线索分配后 调用
     *
     * @param leadsId  线索Id
     * @param name     线索名称
     * @param userId   用户Id
     * @param weUserId 用户企微Id
     * @param seaId    公海Id
     * @author WangYX
     * @date 2023/07/21 15:36
     */
    void addLeadsLongTimeNotFollowUp(Long leadsId, String name, Long userId, String weUserId, Long seaId);

    /**
     * 取消线索长时间未跟进
     * <p>
     * 线索转化后，线索回收后，线索退回后 调用
     *
     * @param leadsId 线索Id
     * @param userId  用户Id
     * @author WangYX
     * @date 2023/07/21 16:13
     */
    void cancelLeadsLongTimeNotFollowUp(Long leadsId, Long userId);

    /**
     * 取消旧的并添加新的线索长时间未跟进
     * <p>
     * 线索跟进后 调用
     *
     * @param leadsId  线索Id
     * @param name     线索名称
     * @param userId   用户Id
     * @param weUserId 用户企微Id
     * @param seaId    公海Id
     * @author WangYX
     * @date 2023/07/21 15:36
     */
    void cancelAndAddLeadsLongTimeNotFollowUp(Long leadsId, String name, Long userId, String weUserId, Long seaId);

    /**
     * 处理线索长时间未跟进
     * <p>
     * 1.显示在待办任务中
     * <p>
     * 2.发送应用消息
     *
     * @author WangYX
     * @date 2023/07/21 16:35
     */
    void handlerLeadsLongTimeNotFollowUp();

    /**
     * 线索约定事项待跟进
     * <p>
     * 1.发送mq消息到RabbitMq，异步解耦处理
     * </p>
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/24 9:57
     */
    void appointItemWaitFollowUp(WeTasksRequest request);

    /**
     * 成员的线索约定事项待跟进
     * <p>
     * 1.发送mq消息到RabbitMq，异步解耦处理
     * </p>
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/24 15:51
     */
    void userAppointItemWaitFollowUp(WeTasksRequest request);

    /**
     * 成员的线索跟进@了你
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/24 17:10
     */
    void userFollowUp2You(WeTasksRequest request);

    /**
     * 有1个标签建群任务待完成
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/25 15:33
     */
    void groupAddByLabel(WeTasksRequest request);

    /**
     * 处理待办任务
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/24 10:00
     */
    void handlerWeTasks(WeTasksRequest request);

    /**
     * 今日客户sop待推送
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/08/09 11:22
     */
    void addCustomerSop(WeTasksRequest request);

    /**
     * 今日客群sop待推动
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/08/09 11:23
     */
    void addGroupSop(WeTasksRequest request);

}
