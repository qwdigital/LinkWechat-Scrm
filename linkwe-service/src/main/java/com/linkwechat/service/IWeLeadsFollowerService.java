package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerNumVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsUserFollowTop5VO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsUserStatisticVO;
import com.linkwechat.domain.leads.record.query.WeLeadsAddFollowRequest;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordRequest;

import java.util.List;

/**
 * 线索跟进人 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:43
 */
public interface IWeLeadsFollowerService extends IService<WeLeadsFollower> {

    /**
     * 获取跟进人的跟进线索数量数据
     *
     * @param seaId 公海Id
     * @return {@link WeLeadsFollowerNumVO}
     * @author WangYX
     * @date 2023/07/11 17:05
     */
    WeLeadsFollowerNumVO getLeadsFollowerNum(Long seaId);

    /**
     * 获取跟进人名单
     *
     * @param leadsId 线索Id
     * @return {@link List<WeLeadsFollowerVO>}
     * @author WangYX
     * @date 2023/07/12 15:03
     */
    List<WeLeadsFollowerVO> getFollowerList(Long leadsId);

    /**
     * 添加跟进
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/12 15:34
     */
    void addFollow(WeLeadsAddFollowRequest request);

    /**
     * 获取跟进记录
     *
     * @param request 请求参数
     * @return
     * @author WangYX
     * @date 2023/07/18 14:44
     */
    void list(WeLeadsFollowRecordRequest request);

    /**
     * 员工统计
     *
     * @param userIds 员工Id集合
     * @return {@link List<WeLeadsUserFollowTop5VO>}
     * @author WangYX
     * @date 2023/07/19 17:58
     */
    List<WeLeadsFollowerVO> userStatistic(List<Long> userIds);


}
