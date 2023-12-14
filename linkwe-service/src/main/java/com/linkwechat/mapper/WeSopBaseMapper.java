package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.sop.WeSopBase;
import com.linkwechat.domain.sop.vo.WeSopDetailCustomerVo;
import com.linkwechat.domain.sop.vo.WeSopDetailGroupVo;
import com.linkwechat.domain.sop.vo.WeSopDetailTabVo;
import com.linkwechat.domain.sop.vo.content.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author robin
 * @description 针对表【we_sop_base(Sop base表)】的数据库操作Mapper
 * @createDate 2022-09-06 10:33:34
 * @Entity generator.domain.WeSopBase
 */
public interface WeSopBaseMapper extends BaseMapper<WeSopBase> {

    /**
     * sop统计详情tab
     * @param sopBaseId
     * @return
     */
    WeSopDetailTabVo findWeSopDetailTabVo(@Param("sopBaseId") String sopBaseId);


    /**
     *  sop统计详情客户列表
     * @return
     */
    List<WeSopDetailCustomerVo> findWeSopDetailCustomer(@Param("sopBaseId") String sopBaseId,
                                                        @Param("customerName")String customerName,@Param("executeState")Integer executeState,
                                                        @Param("weUserId")String weUserId);


    /**
     * sop统计详情客群列表
     * @param sopBaseId
     * @param groupName
     * @param executeState
     * @param weUserId
     * @return
     */
    List<WeSopDetailGroupVo> findWeSopDetailGroup(@Param("sopBaseId") String sopBaseId,
                                                  @Param("groupName")String groupName, @Param("executeState")Integer executeState,
                                                  @Param("weUserId")String weUserId);


    /**
     * 获取客户内容执行列表
     * @param executeWeUserId
     * @param targetId
     * @param executeSubState
     * @param sopBaseId
     * @param checkAll true:所有 false:当天
     * @return
     */
    List<WeCustomerSopBaseContentVo> findCustomerExecuteContent(@Param("executeWeUserId") String executeWeUserId, @Param("targetId") String targetId,
                                                                @Param("executeSubState") Integer executeSubState, @Param("sopBaseId") String sopBaseId,@Param("executeTargetId") String executeTargetId
            ,@Param("checkAll") boolean checkAll);


    /**
     * 获取客群内容执行列表
     * @param chatId
     * @param executeSubState
     * @param sopBaseId
     * @param executeTargetId 执行目标id
     * @param  executeWeUserId 执行成员id
     * @return
     */
    List<WeGroupSopBaseContentVo> findGroupExecuteContent(@Param("chatId") String chatId,
                                                          @Param("executeSubState") Integer executeSubState,@Param("sopBaseId") String sopBaseId,@Param("executeTargetId") String executeTargetId
            ,@Param("checkAll") boolean checkAll,@Param("executeWeUserId") String executeWeUserId);


    /**
     * 根据员工id获取今日需要发送sop的客户
     * @param weUserId
     * @return
     */
    List<WeCustomerSopToBeSentVo> findTdSendSopCustomers(@Param("weUserId") String weUserId,@Param("isExpiringSoon") boolean isExpiringSoon);

    /**
     * 根据员工id获取今日需要发送sop的群
     * @param weUserId
     * @return
     */
    List<WeGroupSopToBeSentVo> findTdSendSopGroups(@Param("weUserId") String weUserId,@Param("isExpiringSoon") boolean isExpiringSoon);


    /**
     * 获取今日客户或群未发送sop相关信息
     * @param weUserId
     * @param targetId
     * @return
     */
    List<WeSopToBeSentContentInfoVo> findSopToBeSentContentInfo(@Param("weUserId") String weUserId,@Param("targetId") String targetId);


    /**
     * 更新sop状态
     * @param sopId
     * @param sopState
     */
    void  updateSopState(@Param("sopId") String sopId,@Param("sopState") Integer sopState);


}

