package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.moments.dto.MomentsListDetailParamDto;
import com.linkwechat.domain.moments.dto.MomentsListDetailResultDto;
import com.linkwechat.domain.moments.entity.WeMomentsTask;
import com.linkwechat.domain.moments.query.WeMomentsJobIdToMomentsIdRequest;
import com.linkwechat.domain.moments.query.WeMomentsSyncGroupSendRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskAddRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskListRequest;
import com.linkwechat.domain.moments.vo.WeMomentsTaskVO;

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
    void sendWeMoments(Long weMomentsTaskId);

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
     * @param filterType 朋友圈类型。0：企业发表 1：个人发表 2：所有，包括个人创建以及企业创建，默认情况下为所有类型
     * @return
     * @author WangYX
     * @date 2023/06/12 11:04
     */
    void syncMoments(Integer filterType);

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
     * @return
     * @author WangYX
     * @date 2023/06/13 9:41
     */
    void groupSendFinish(WeMomentsSyncGroupSendRequest request);

    /**
     * jobId换取MomentsId
     *
     * @param request jobId换取MomentsId参数
     * @author WangYX
     * @date 2023/06/13 10:33
     */
    void jobIdToMomentsId(WeMomentsJobIdToMomentsIdRequest request);


    void getByMoment(String nextCursor, List<MomentsListDetailResultDto.Moment> list, MomentsListDetailParamDto query);


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


    //----------------------------------------------------------------------------------------------------------------

    List<WeMomentsTask> findMoments(WeMomentsTask weMoments);

    void addOrUpdateMoments(WeMomentsTask weMoments);


    WeMomentsTask findMomentsDetail(Long id);


    void synchMomentsInteracteHandler(String msg);

    void synchMomentsInteracte(List<String> userIds);
}
