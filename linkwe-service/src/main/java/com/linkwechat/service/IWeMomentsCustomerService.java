package com.linkwechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.moments.entity.WeMomentsCustomer;
import com.linkwechat.domain.moments.query.WeMomentsTaskAddRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskEstimateCustomerNumRequest;

import java.util.List;

/**
 * 朋友圈可见客户 服务类
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:06
 */
public interface IWeMomentsCustomerService extends IService<WeMomentsCustomer> {


    /**
     * 预估客户数量
     *
     * @param request 客户范围查询参数
     * @return {@link java.lang.Integer}
     * @author WangYX
     * @date 2023/06/07 17:01
     */
    long estimateCustomerNum(WeMomentsTaskEstimateCustomerNumRequest request);

    /**
     * 预估客户
     *
     * @param request 客户范围查询参数
     * @return {@link List< WeCustomer>}
     * @author WangYX
     * @date 2023/06/30 18:14
     */
    List<WeCustomer> estimateCustomers(WeMomentsTaskEstimateCustomerNumRequest request);


    /**
     * 新增朋友圈可见客户
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @param request         朋友圈任务新增请求参数
     * @return
     * @author WangYX
     * @date 2023/06/08 17:21
     */
    void addMomentsCustomer(Long weMomentsTaskId, WeMomentsTaskAddRequest request);

    /**
     * 同步企微朋友圈时，新增可见客户
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @param momentsId       朋友圈Id
     * @return
     * @author WangYX
     * @date 2023/06/12 15:18
     */
    void syncAddMomentsCustomer(Long weMomentsTaskId, String momentsId);

    /**
     * 同步朋友圈发送成功的情况
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @param momentsId       朋友圈Id
     * @author WangYX
     * @date 2023/06/12 16:12
     */
    void syncMomentsCustomerSendSuccess(Long weMomentsTaskId, String momentsId);

    /**
     * 批量插入
     * @param weMomentsCustomers
     */
    void saveBatch(List<WeMomentsCustomer> weMomentsCustomers);
}
