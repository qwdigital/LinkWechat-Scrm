package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.moments.dto.MomentsListDetailParamDto;
import com.linkwechat.domain.moments.dto.MomentsListDetailResultDto;
import com.linkwechat.domain.moments.entity.WeMomentsTask;
import com.linkwechat.domain.moments.entity.WeMomentsTaskRelation;
import com.linkwechat.domain.moments.query.*;
import com.linkwechat.domain.moments.vo.WeMomentsTaskVO;

import java.io.IOException;
import java.util.List;

/**
 * 朋友圈
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/06 17:18
 */
public interface IWeMomentsTaskService extends IService<WeMomentsTask> {

    /**
     * 朋友圈列表
     *
     * @param request 列表查询参数
     * @return {@link List< WeMomentsTaskVO>}
     * @author WangYX
     * @date 2023/06/06 18:10
     */
    List<WeMomentsTaskVO> selectList(WeMomentsTaskListRequest request);


    /**
     * 新增朋友圈
     *
     * @param request 新增请求参数
     * @return
     * @author WangYX
     * @date 2023/06/07 13:58
     */
    Long add(WeMomentsTaskAddRequest request);


    /**
     * 异步执行-立即执行发送朋友圈
     * @param task
     */
    void immediatelySendMoments(WeMomentsTask task);

    /**
     * 获取朋友圈详情
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @return {@link WeMomentsTaskVO}
     * @author WangYX
     * @date 2023/06/09 11:29
     */
    WeMomentsTaskVO get(Long weMomentsTaskId);

    /**
     * 执行发送朋友圈
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @author WangYX
     * @date 2023/06/12 10:17
     */
    void sendWeMoments(Long weMomentsTaskId) throws IOException;

    /**
     * 取消发送朋友圈
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @author WangYX
     * @date 2023/06/12 10:01
     */
    void cancelSendMoments(Long weMomentsTaskId);

    /**
     * 同步朋友圈
     * 发送消息到mq队列
     *
     * @param taskIds 任务id集合
     * @return
     * @author WangYX
     * @date 2023/06/12 11:04
     */
    void syncMoments(List<String> taskIds);

    /**
     * mq队列,朋友圈同步处理
     *
     * @param msg mq消息体
     * @return
     * @author WangYX
     * @date 2023/06/12 11:07
     */
    void syncWeMomentsHandler(String msg);

    /**
     * 成员群发类型任务，员工完成任务
     *
     * @param request 员工完成任务
     * @author WangYX
     * @date 2023/06/13 9:41
     */
    void groupSendFinish(WeMomentsSyncGroupSendRequest request);

    /**
     * 获取成员群发执行结果
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/04 15:00
     */
    void getGroupSendExecuteResult(WeMomentsSyncGroupSendMqRequest request);

    /**
     * jobId换取MomentsId
     *
     * @param relations jobId换取MomentsId参数
     * @author WangYX
     * @date 2023/06/13 10:33
     */
    void jobIdToMomentsId(List<WeMomentsTaskRelation> relations);

    /**
     * 获取企微朋友圈信息
     *
     * @param nextCursor 游标
     * @param list       从企微获取的朋友圈数据，放到该list中
     * @param query      查询数据
     */
    void getMoment(String nextCursor, List<MomentsListDetailResultDto.Moment> list, MomentsListDetailParamDto query);

    /**
     * 朋友圈定时拉取数据
     *
     * @param moments 朋友圈集合
     */
    void syncMomentsDataHandle(List<MomentsListDetailResultDto.Moment> moments);

    /**
     * 提醒执行
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @return
     * @author WangYX
     * @date 2023/06/13 17:36
     */
    void reminderExecution(Long weMomentsTaskId);


    /**
     * 同步朋友圈互动数据
     *
     * @param userIds 用户Id集合
     * @return
     * @author WangYX
     * @date 2023/06/21 16:51
     */
    void syncMomentsInteract(List<String> userIds);

    /**
     * 同步朋友圈互动数据
     *
     * @param msg
     * @return
     * @author WangYX
     * @date 2023/06/21 16:52
     */
    void syncMomentsInteractHandler(String msg);
}
