package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordContent;

/**
 * 线索跟进记录内容表服务接口
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:55
 */
public interface IWeLeadsFollowRecordContentService extends IService<WeLeadsFollowRecordContent> {

    /**
     * 添加成员主动领取内容
     *
     * @param recordId 跟进记录Id
     * @param seaId    公海Id
     * @return
     * @author WangYX
     * @date 2023/07/12 10:13
     */
    void memberToReceive(Long recordId, Long seaId);

    /**
     * 添加自动回收内容
     *
     * @param recordId 跟进记录Id
     * @author WangYX
     * @date 2023/07/12 14:22
     */
    void autoRecovery(Long recordId);

    /**
     * 管理员主动分配
     *
     * @param recordId 跟进记录Id
     * @param name     分配人姓名
     * @author WangYX
     * @date 2023/07/13 11:08
     */
    void adminAllocation(Long recordId, String name);

    /**
     * 员工退回线索
     *
     * @param recordId 跟进记录Id
     * @param remark   退回备注
     * @param seaId    退回公海
     * @author WangYX
     * @date 2023/07/18 13:51
     */
    void userReturn(Long recordId, String remark, Long seaId);

    /**
     * 转化客户
     *
     * @param recordId     跟进记录Id
     * @param customerName 客户名称
     * @param customerType 客户类型
     * @param avatar       客户头像
     * @return
     * @author WangYX
     * @date 2023/07/18 18:42
     */
    void convertedCustomer(Long recordId, String customerName, String customerType, String avatar);
}

