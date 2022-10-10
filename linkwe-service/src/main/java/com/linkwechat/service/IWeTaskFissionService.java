package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeTaskFission;
import com.linkwechat.domain.taskfission.query.WeAddTaskFissionQuery;
import com.linkwechat.domain.taskfission.query.WeTaskFissionQuery;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionProgressVo;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionStatisticVo;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionVo;

import java.util.Date;
import java.util.List;

/**
 * 任务宝表(WeTaskFission)
 *
 * @author danmo
 * @since 2022-06-28 13:48:53
 */
public interface IWeTaskFissionService extends IService<WeTaskFission> {

    /**
     * 查询任务宝列表
     *
     * @param weTaskFission 任务宝
     * @return 任务宝集合
     */
    public List<WeTaskFission> selectWeTaskFissionList(WeTaskFission weTaskFission);

    /**
     * 查询任务宝
     *
     * @param id 任务宝ID
     * @return 任务宝
     */
    public WeTaskFissionVo selectWeTaskFissionById(Long id);

    /**
     * 新增任务宝
     * @param query
     */
    void insertWeTaskFission(WeAddTaskFissionQuery query);

    /**
     * 更新数据
     * @param query
     */
    void updateWeTaskFission(WeAddTaskFissionQuery query);

    /**
     * 删除数据
     * @param ids 业务ID
     */
    void deleteWeTaskFissionByIds(Long[] ids);
    /**
     * 发送任务
     * @param query
     */
    void sendWeTaskFission(WeAddTaskFissionQuery query);


    /**
     * 生成海报
     * @param query
     * @return
     */
    String fissionPosterGenerate(WeTaskFissionQuery query);

    /**
     * 客户任务进度详情
     * @param query
     * @return
     */
    WeTaskFissionProgressVo getTaskProgress(WeTaskFissionQuery query);

    /**
     * 统计信息
     * @param taskFissionId
     * @param startTime
     * @param endTime
     * @return
     */
    WeTaskFissionStatisticVo taskFissionStatistic(Long taskFissionId, Date startTime, Date endTime);

    /**
     * 任务宝裂变客户处理
     *
     * @param externalUserId
     * @param userId
     * @param fissionRecordId
     */
    void addCustomerHandler(String externalUserId, String userId, String fissionRecordId);

    /**
     * 群裂变客户入群校验
     *
     * @param chatId
     * @param joinScene
     * @param createTime
     * @param memChangeCnt
     */
    void groupFissionEnterCheck(String chatId, Integer joinScene, Long createTime, Integer memChangeCnt);

    /**
     * 更新过期任务
     * @return
     */
    public void updateExpiredWeTaskFission();
}
