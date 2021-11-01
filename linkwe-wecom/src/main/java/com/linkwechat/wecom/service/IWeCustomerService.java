package com.linkwechat.wecom.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeCustomerList;
import com.linkwechat.wecom.domain.WeCustomerPortrait;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.vo.WeCustomerDetailVo;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;

import java.util.List;

/**
 * 企业微信客户Service接口
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
public interface IWeCustomerService extends IService<WeCustomer>
{
    /**
     * 查询企业微信客户
     * 
     * @param externalUserId 企业微信客户ID
     * @return 企业微信客户
     */
    public WeCustomer selectWeCustomerById(String externalUserId);

//    /**
//     * 新增/修改企业微信客户
//     *
//     * @param weCustomer 企业微信客户
//     * @return 修改结果
//     */
//    @Override
//    public boolean saveOrUpdate(WeCustomer weCustomer);

    /**
     * 查询企业微信客户列表
     * 
     * @param weCustomer 企业微信客户
     * @return 企业微信客户集合
     */
    public List<WeCustomer> selectWeCustomerList(WeCustomer weCustomer);


    /**
     * 同步客户接口
     * @return
     */
    public void synchWeCustomer();


    /**
     * 分配离职员工客户
     * @param weLeaveUserInfoAllocateVo
     */
    public void allocateWeCustomer(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo);


    /**
     * 客户打标签
     * @param weMakeCustomerTag
     */
    public void makeLabel(WeMakeCustomerTag weMakeCustomerTag);


//    /**
//     * 移除客户标签
//     * @param weMakeCustomerTag
//     */
//    public void removeLabel(WeMakeCustomerTag weMakeCustomerTag);


    /**
     * 根据员工ID获取客户
     * @param externalUserid
     * @return
     */
    public List<WeUser> getCustomersByUserId(String externalUserid);

    /**
     * 获取客户详情并同步客户数据
     * @param externalUserid
     * @param userId
     */
    public void getCustomersInfoAndSynchWeCustomer(String externalUserid,String userId);



    /**
     * 向客户发送欢迎语
     * @param weWelcomeMsg
     */
    public void sendWelcomeMsg(WeWelcomeMsg weWelcomeMsg);

    /**
     * 修改客户是否开启会话状态
     * @param externalUserId 客户id
     */
    public boolean updateCustomerChatStatus(String externalUserId);





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
     * 查询全部客户列表
     * @param weCustomer
     * @return
     */
    public List<WeCustomer> selectWeCustomerAllList(WeCustomer weCustomer);

    /**
     * 查询企业微信客户列表,不查询一对多关系相关数据
     *
     * @param weCustomer 企业微信客户
     * @return 企业微信客户集合
     */
    List<WeCustomer> selectWeCustomerListNoRel(WeCustomer weCustomer);


    /**
     * 重构版客户列表
     * @param weCustomerList
     * @return
     */
    List<WeCustomerList> findWeCustomerList(WeCustomerList weCustomerList);



    WeCustomerDetailVo findWeCustomerDetail(String externalUserid);

    /**
     * 客户条件检索
     * @param params
     */
    void getCustomerByCondition(JSONObject params);
}
