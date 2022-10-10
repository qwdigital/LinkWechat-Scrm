package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeCustomerTrackRecord;
import com.linkwechat.domain.customer.WeMakeCustomerTag;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.query.WeOnTheJobCustomerQuery;
import com.linkwechat.domain.customer.vo.WeCustomerAddUserVo;
import com.linkwechat.domain.customer.vo.WeCustomerDetailInfoVo;
import com.linkwechat.domain.customer.vo.WeCustomerPortraitVo;
import com.linkwechat.domain.customer.vo.WeCustomersVo;

import java.util.List;
import java.util.Map;

public interface IWeCustomerService extends IService<WeCustomer> {

    /**
     * 客户列表
     * @param weCustomersQuery
     * @return
     */
    List<WeCustomersVo> findWeCustomerList(WeCustomersQuery weCustomersQuery, PageDomain pageDomain);



    /**
     * 客户总数统计
     * @param weCustomersQuery
     * @return
     */
    long countWeCustomerList(WeCustomersQuery weCustomersQuery);


    /**
     * 应用列表客户数统计
     * @param weCustomersQuery
     * @return
     */
    long countWeCustomerListByApp(WeCustomersQuery weCustomersQuery);


    /**
     * 去重统计
     * @return
     */
    long noRepeatCountCustomer(WeCustomersQuery weCustomersQuery);


    /**
     * 同步企业微信客户
     * @return
     */
    void synchWeCustomer();


    /**
     * 同步企业客户
     * @param msg
     */
    void synchWeCustomerHandler(String msg);


    /**
     * 客户打标签
     * @param weMakeCustomerTag
     */
    void makeLabel(WeMakeCustomerTag weMakeCustomerTag);


    /**
     * 在职员工客户分配
     * @param weOnTheJobCustomerQuery
     */
    void allocateOnTheJobCustomer(WeOnTheJobCustomerQuery weOnTheJobCustomerQuery);


    /**
     * 客户详情基础数据
     * @param externalUserid
     * @param userId
     * @return
     */
    WeCustomerDetailInfoVo findWeCustomerDetail(String externalUserid, String userId, Integer delFlag);



    /**
     * 客户画像汇总
     * @return
     */
    WeCustomerDetailInfoVo findWeCustomerInfoSummary(String externalUserid,String userId,Integer delFlag);


    /**
     * 单个跟进人客户
     * @return
     */
    WeCustomerDetailInfoVo findWeCustomerInfoByUserId(String externalUserid,String userId,Integer delFlag);



    /**
     * 根据员工id获取员工名称
     * @return
     */
    String findUserNameByUserId(String userId);



    /**
     * 根据外部联系人ID和企业员工ID获取当前客户信息
     * @param externalUserid
     * @param userid
     * @return
     */
    WeCustomerPortraitVo findCustomerByOperUseridAndCustomerId(String externalUserid, String userid) throws Exception;



    /**
     * 跟新客户画像
     * @param weCustomerPortrait
     */
    void updateWeCustomerPortrait(WeCustomerPortraitVo weCustomerPortrait);



    /**
     * 根据客户id获取客户添加人
     * @param externalUserid
     * @return
     */
    List<WeCustomerAddUserVo> findWeUserByCustomerId(String externalUserid);



    /**
     * 客户跟进记录
     * @param trajectory
     */
    void addOrEditWaitHandle( WeCustomerTrackRecord trajectory);


    /**
     * 新增客户时间
     * @param externalUserId 客户ID
     * @param userId 成员ID
     * @param state 添加此用户的渠道
     */
    void addCustomer(String externalUserId, String userId, String state);

    /**
     * 编辑客户信息
     * @param externalUserId 客户ID
     * @param userId 成员ID
     */
    void updateCustomer(String externalUserId, String userId);


    /**
     * 获取当前租户下的员工
     * @return
     */
    Map<String, SysUser>  findCurrentTenantSysUser();


    /**
     * 根据企业微信员工id获取当前企业员工相关信息
     * @param weUserId
     * @return
     */
    SysUser findSysUserInfoByWeUserId(String weUserId);


    /**
     * 根据系统用户主建，获取当前用户信息
     * @param userId
     * @return
     */
    SysUser findCurrentSysUserInfo(Long userId);


    /**
     * 补充客户unionId
     *
     * @param unionId
     */
    void updateCustomerUnionId(String unionId);

    /**
     * 通过条件查询客户信息
     * @param query
     * @return
     */
    List<WeCustomer> getCustomerListByCondition(WeCustomersQuery query);


//    /**
//     * 根据员工id获取当前员工下的客户id
//     * @param weUserId
//     * @return
//     */
//    List<String>  findWeCustomerListExternalUserIdsByAddUserId(String weUserId);
//
//
//    /**
//     * 根据系统内统一的角色权限获取客户id
//     * @return
//     */
//    List<String> findWeCustomerListExternalUserIds();


    /**
     * 获取所有员工
     * @return
     */
    List<SysUser> findAllSysUser();

    /**
     * 获取权限可见范围的员工id
     * @return
     */
    List<String> findWeUserIds();

    List<WeCustomersVo> findWeCustomerList(List<String> customerIds);
}
