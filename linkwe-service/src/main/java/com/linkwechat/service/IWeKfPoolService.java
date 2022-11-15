package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.kf.query.WeKfRecordQuery;
import com.linkwechat.domain.kf.vo.WeKfRecordListVo;

import java.util.List;
import java.util.Map;

/**
 * 客服接待池表(WeKfPool)
 *
 * @author danmo
 * @since 2022-04-15 15:53:37
 */
public interface IWeKfPoolService extends IService<WeKfPool> {
    /**
     * 保存接待信息
     * @param weKfPool
     */
    void saveKfPoolInfo(WeKfPool weKfPool);

    /**
     * 获取接待信息
     *
     * @param corpId
     * @param openKfId 客服id
     * @param externalUserId 客户UserID
     * @return
     */
    WeKfPool getKfPoolInfo(String corpId, String openKfId, String externalUserId);

    /**
     * 更新接待信息
     * @param weKfPool
     */
    void updateKfPoolInfo(WeKfPool weKfPool);

    /**
     * 变更接待人
     * @param corpId
     * @param openKfId 客服id
     * @param externalUserId 客户UserID
     * @param userId 员工id
     */
    void updateKfServicer(String corpId, String openKfId, String externalUserId, String userId);

    /**
     * 获取上一个接待信息
     *
     * @param corpId
     * @param openKfId
     * @param externalUserId
     * @return
     */
    WeKfPool getLastServicer(String corpId, String openKfId, String externalUserId);

    /**
     * 分配接待人员
     * @param corpId
     * @param openKfId 客服账号id
     * @param externalUserId 客户id
     * @param userId 接待人员id
     * @param status 状态
     */
    void allocationServicer(String corpId, String openKfId, String externalUserId, String userId, Integer status);


    /**
     * 获取员工接待数量
     *
     * @param corpId
     * @param openKfId 客服账号id
     * @param userId 接待人员id
     * @return
     */
    int getReceptionCnt(String corpId, String openKfId, String userId);

    /**
     * 获取每个员工接待数量
     * @param openKfId 客服账号id
     * @param externalUserId 客户id
     * @param userIds 接待人员id列表
     * @return
     */
    List<Map<String, Object>> getReceptionGroupByCnt(String corpId, String openKfId, String externalUserId, List<String> userIds);

    /**
     * 接待池客户转接
     * @param corpId
     * @param openKfId 客服账号id
     * @param externalUserId 客户id
     */
    void transferReceptionPoolCustomer(String corpId, String openKfId, String externalUserId);


    /**
     * 转人工处理
     *
     * @param corpId
     * @param openKfId 客服ID
     * @param externalUserId  客户id
     */
    void transServiceHandler(String corpId, String openKfId, String externalUserId, Boolean isAI);

    void transServiceHandler(String corpId, String openKfId, String externalUserId);

    /**
     * 咨询记录列表
     * @param query
     */
    List<WeKfRecordListVo> getRecordList(WeKfRecordQuery query);

} 
