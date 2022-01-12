package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.vo.WeCustomerDetailVo;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;
import com.linkwechat.wecom.domain.vo.WeOnTheJobCustomerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 企业微信客户Service接口
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
public interface IWeCustomerService extends IService<WeCustomer>
{



    /**
     * 同步客户接口
     * @return
     */
     void synchWeCustomer();


    /**
     * 分配离职员工客户
     * @param weLeaveUserInfoAllocateVo
     */
     void allocateWeCustomer(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo);


    /**
     * 在职员工客户分配
     * @param weOnTheJobCustomerVo
     */
     void allocateOnTheJobCustomer(WeOnTheJobCustomerVo weOnTheJobCustomerVo);




    /**
     * 客户打标签
     * @param weMakeCustomerTag
     */
     void makeLabel(WeMakeCustomerTag weMakeCustomerTag);





    /**
     * 获取客户详情并同步客户数据
     * @param externalUserid
     * @param userId
     */
     void getCustomersInfoAndSynchWeCustomer(String externalUserid,String userId);



    /**
     * 向客户发送欢迎语
     * @param weWelcomeMsg
     */
     void sendWelcomeMsg(WeWelcomeMsg weWelcomeMsg);

    /**
     * 修改客户是否开启会话状态
     * @param externalUserId 客户id
     */
    boolean updateCustomerChatStatus(String externalUserId);





    /**
     * 根据外部联系人ID和企业员工ID获取当前客户信息
     * @param externalUserid
     * @param userid
     * @return
     */
    WeCustomerPortrait findCustomerByOperUseridAndCustomerId(String externalUserid,String userid) throws Exception;


    /**
     * 跟新客户画像
     * @param weCustomerPortrait
     */
    void updateWeCustomerPortrait(WeCustomerPortrait weCustomerPortrait);




    /**
     * 重构版客户列表
     * @param weCustomerList
     * @return
     */
    List<WeCustomerList> findWeCustomerList(WeCustomerList weCustomerList, PageDomain pageDomain);

    /**
     * 客户总数统计
     * @param weCustomerList
     * @return
     */
    long countWeCustomerList(@Param("weCustomerList") WeCustomerList weCustomerList);


    /**
     * 客户详情基础数据
     * @param externalUserid
     * @param userId
     * @return
     */
    WeCustomerDetail findWeCustomerDetail(String externalUserid,String userId,Integer delFlag);


    /**
     * 客户画像汇总
     * @return
     */
    WeCustomerDetail findWeCustomerInfoSummary(String externalUserid,String userId,Integer delFlag);



    /**
     * 单个跟进人客户
     * @return
     */
    WeCustomerDetail findWeCustomerInfoByUserId(String externalUserid,String userId,Integer delFlag);


    /**
     * 去重统计
     * @return
     */
    long noRepeatCountCustomer(WeCustomerList weCustomerList);


    /**
     * 客户跟进记录
     * @param trajectory
     */
    void addOrEditWaitHandle(WeCustomerTrajectory trajectory);


}
