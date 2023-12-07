package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WeCustomerMapper extends BaseMapper<WeCustomer> {


    /**
     * 分页获取用户id列表
     *
     * @param weCustomersQuery
     * @param pageDomain
     * @return
     */
    @DataScope(type = "2", value = @DataColumn(alias = "wcr", name = "create_by_id", userid = "user_id"))
    List<String> findWeCustomerListIds(@Param("weCustomerList") WeCustomersQuery weCustomersQuery, @Param("pageDomain") PageDomain pageDomain);

    /**
     * 获取应用相关客户id列表
     *
     * @param weCustomersQuery
     * @param pageDomain
     * @return
     */
    List<String> findWeCustomerListIdsByApp(@Param("weCustomerList") WeCustomersQuery weCustomersQuery, @Param("pageDomain") PageDomain pageDomain);

    /**
     * 重构版客户列表接口
     *
     * @param ids
     * @return
     */
    List<WeCustomersVo> findWeCustomerList(@Param("ids") List<String> ids);


    /**
     * 客户总数统计
     *
     * @param weCustomersQuery
     * @return
     */
    @DataScope(type = "2", value = @DataColumn(alias = "wcr", name = "create_by_id", userid = "user_id"))
    long countWeCustomerList(@Param("weCustomerList") WeCustomersQuery weCustomersQuery);


    /**
     * 应用的相关客户统计
     *
     * @param weCustomersQuery
     * @return
     */
    long countWeCustomerListByApp(@Param("weCustomerList") WeCustomersQuery weCustomersQuery);


    /**
     * 去重复统计
     *
     * @return
     */
    @DataScope(type = "2", value = @DataColumn(alias = "wcr", name = "create_by_id", userid = "user_id"))
    long noRepeatCountCustomer(@Param("weCustomerList") WeCustomersQuery weCustomersQuery);


    /**
     * 获取客户所在群
     *
     * @param userId
     * @return
     */
    List<WeCustomerDetailInfoVo.Groups> findWecustomerGroups(@Param("userId") String userId);


    /**
     * 根据员工id获取员工名称
     *
     * @return
     */
    String findUserNameByUserId(@Param("weUserId") String weUserId);


    /**
     * 根据外部联系人ID和企业员工ID获取当前客户信息(客户画像)
     *
     * @param externalUserid
     * @param userid
     * @return
     */
    WeCustomerPortraitVo findCustomerByOperUseridAndCustomerId(@Param("externalUserid") String externalUserid, @Param("userid") String userid);


    /**
     * 统计客户社交关系
     *
     * @param externalUserid 客户id
     * @param userid         员工id
     * @return
     */
    WeCustomerSocialConnVo countSocialConn(@Param("externalUserid") String externalUserid, @Param("userid") String userid);


    /**
     * 根据客户id获取客户添加人
     *
     * @param externalUserid
     * @return
     */
    List<WeCustomerAddUserVo> findWeUserByCutomerId(@Param("externalUserid") String externalUserid);


    /**
     * 批量更新或新增
     *
     * @param weCustomer
     */

    void batchAddOrUpdate(@Param("weCustomers") List<WeCustomer> weCustomer);


    /**
     * 获取当前租户下所有员工
     *
     * @return
     */
    List<SysUser> findCurrentTenantSysUser();


    /**
     * 跟进数据权限获取员工
     *
     * @return
     */
    @DataScope(value = @DataColumn(name = "create_by_id", userid = "user_id"))
    List<String> findWeUserIds();


    /**
     * 跟进条件获取客户
     *
     * @param query
     * @return
     */
    List<WeCustomer> getCustomerListByCondition(WeCustomersQuery query);

    /**
     * 获取当前员工信息
     *
     * @param userId
     * @return
     */
    SysUser findCurrentSysUserInfo(@Param("userId") Long userId);


    /**
     * 根据员工id获取员工信息
     *
     * @param weUserId
     * @return
     */
    SysUser findSysUserInfoByWeUserId(@Param("weUserId") String weUserId);


    /**
     * 获取当前系统下所有可用用户，不做数据权限过滤
     *
     * @return
     */

    List<SysUser> findAllSysUser();


    /**
     * 根据条件物理删除指定客户数据
     *
     * @param externalUserId
     * @param userId
     */

    void deleteWeCustomer(@Param("externalUserId") String externalUserId, @Param("userId") String userId);


    /**
     * 获取应用相关客户id(对应企业微信客户id)列表
     *
     * @param weCustomersQuery
     * @return
     */
    List<String> findWeCustomerListEuIds(@Param("weCustomerList") WeCustomersQuery weCustomersQuery);

    /**
     * 通过eid查询客户简单信息
     * @param externalUserIds
     */
    List<WeCustomerSimpleInfoVo> getCustomerSimpleInfo(@Param("externalUserIds") List<String> externalUserIds);




    /**
     * 根据渠道时间统计客户(目前用于活码统计相关)
     * @param state
     * @param startTime
     * @param endTime
     * @return
     */
    List<WeCustomerChannelCountVo> countCustomerChannel(@Param("state") String state,
                                                        @Param("startTime") String startTime,@Param("endTime") String endTime,
                                                        @Param("delFlag") Integer delFlag);

    /**
     * 统计指定渠道下累计次数
     * @param state
     * @return
     */
    Integer totalScanCodeNumber(@Param("state") String state);


    List<WeCustomerChannelCountVo> getCustomerNumByState(@Param("state") String state, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
