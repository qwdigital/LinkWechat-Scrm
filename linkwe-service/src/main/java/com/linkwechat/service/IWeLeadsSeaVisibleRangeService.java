package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSeaVisibleRange;

import java.util.List;

/**
 * <p>
 * 公海可见范围 服务类
 * </p>
 *
 * @author WangYX
 * @since 2023-04-03
 */
public interface IWeLeadsSeaVisibleRangeService extends IService<WeLeadsSeaVisibleRange> {

    /**
     * 获取员工公海的可见范围
     *
     * @param userId   员工Id
     * @param weUserId 员工企微Id
     * @return
     */
    List<WeLeadsSeaVisibleRange> getVisibleRange(Long userId, String weUserId);

}
