package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.vo.CusertomerBelongUserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 企业微信客户Mapper接口
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
public interface WeCustomerMapper  extends BaseMapper<WeCustomer>
{


    /**
     * 重构版客户列表接口
     * @param weCustomerList
     * @return
     */
    List<WeCustomerList> findWeCustomerList(@Param("weCustomerList") WeCustomerList weCustomerList,@Param("pageDomain") PageDomain pageDomain);


    /**
     * 客户总数统计
     * @param weCustomerList
     * @return
     */
    long countWeCustomerList(@Param("weCustomerList") WeCustomerList weCustomerList);

    /**
     * 获取客户所在群
     * @param userId
     * @return
     */
    List<WeCustomerDetail.Groups> findWecustomerGroups(@Param("userId") String userId);




    /**
     * 根据外部联系人ID和企业员工ID获取当前客户信息(客户画像)
     * @param externalUserid
     * @param userid
     * @return
     */
    WeCustomerPortrait findCustomerByOperUseridAndCustomerId(@Param("externalUserid") String externalUserid,@Param("userid") String userid);


    /**
     * 统计客户社交关系
     * @param externalUserid 客户id
     * @param userid 员工id
     * @return
     */
    WeCustomerSocialConn countSocialConn(@Param("externalUserid")String externalUserid,@Param("userid")String userid);





    /**
     * 客户所属员工相关
     * @return
     */
    List<CusertomerBelongUserInfo> findCusertomerBelongUserInfo(@Param("externalUserId") String externalUserId);


    /**
     * 批量更新或新增
     * @param weCustomer
     */
    void batchAddOrUpdate(@Param("weCustomers") List<WeCustomer> weCustomer);


    /**
     * 去重复统计
     * @return
     */
    long noRepeatCountCustomer(@Param("weCustomerList") WeCustomerList weCustomerList);





}
