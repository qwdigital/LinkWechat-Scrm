package com.linkwechat.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeCustomerTrajectory;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupMember;
import io.swagger.models.auth.In;

import java.util.List;

public interface IWeCustomerTrajectoryService extends IService<WeCustomerTrajectory> {


    /**
     *  创建互动轨迹
     * @param externalUserid
     * @param weUserId
     * @param trajectorySceneType
     * @param otherInfo
     */
    void createInteractionTrajectory(String externalUserid, String weUserId, Integer trajectorySceneType,String otherInfo);

    /**
     * 创建员工相关编辑轨迹，编辑企业标签，标记个人标签，编辑详细资料 【员工动态】
     * @param externalUserid
     * @param weUserId
     * @param trajectorySceneType
     * @param editInfo  编辑的信息，目前为标签id
     */
    void createEditTrajectory(String externalUserid, String weUserId, Integer trajectorySceneType,String editInfo);



    /**
     * 创建跟进轨迹,同时入库跟进记录表 【跟进动态】
     * @param externalUserid
     * @param weUserId
     * @param trackState
     */
    void createTrackTrajectory(String externalUserid,String weUserId,Integer trackState,String trackContent);

    /**
     * 生成添加或被客户删除轨迹 【客户动态】
     * @param externalUserid
     * @param weUserId
     * @param addOrRemove
     * @param removeTarget true 客户删除员工;false 员工删除客户
     */
    void createAddOrRemoveTrajectory(String externalUserid,String weUserId,boolean addOrRemove,boolean removeTarget);

    /**
     * 创建入群或退群轨迹 【客群动态】
     * joinOrQuit:true 入群;false:退群
     */
    void createJoinOrExitGroupTrajectory(List<WeGroupMember> members, String groupChatName,boolean joinOrQuit);


    /**
     * 创建建群或解散群轨迹 【客群动态】
     * true 创建群;false:解散群
     */
    void createBuildOrDissGroupTrajectory(List<WeGroup> weGroups, boolean buildOrDiss);

    /**
     * 获取当前员工个人轨迹数据
     * @param wrapper
     * @return
     */
    List<WeCustomerTrajectory> findPersonTrajectory(WeCustomerTrajectory trajectory);


    /**
     * 获取员工数据权限范围内的轨迹数据
     * @param wrapper
     * @return
     */
    List<WeCustomerTrajectory> findAllTrajectory(WeCustomerTrajectory trajectory);


}
