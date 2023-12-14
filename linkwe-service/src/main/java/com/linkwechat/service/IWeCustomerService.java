package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeCustomerTrackRecord;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.customer.WeBacthMakeCustomerTag;
import com.linkwechat.domain.customer.WeMakeCustomerTag;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.query.WeOnTheJobCustomerQuery;
import com.linkwechat.domain.customer.vo.*;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.groupmsg.vo.WeGroupMessageExecuteUsertipVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IWeCustomerService extends IService<WeCustomer> {

    /**
     * 客户列表
     *
     * @param weCustomersQuery
     * @return
     */
    List<WeCustomersVo> findWeCustomerList(WeCustomersQuery weCustomersQuery, PageDomain pageDomain);


    /**
     * 获取客户相关信息，直接从企业微信获取
     * @param externalUserids
     * @return
     */
    List<WeCustomersVo>  findWeCustomerInfoFromWechat(List<String> externalUserids);


    /**
     * 应用客户列表
     *
     * @param weCustomersQuery
     * @param pageDomain
     * @return
     */
    TableDataInfo<List<WeCustomersVo>> findWeCustomerListByApp(WeCustomersQuery weCustomersQuery, PageDomain pageDomain);

    /**
     * 客户总数统计
     *
     * @param weCustomersQuery
     * @return
     */
    long countWeCustomerList(WeCustomersQuery weCustomersQuery);


    /**
     * 应用列表客户数统计
     *
     * @param weCustomersQuery
     * @return
     */
    long countWeCustomerListByApp(WeCustomersQuery weCustomersQuery);


    /**
     * 去重统计
     *
     * @return
     */
    long noRepeatCountCustomer(WeCustomersQuery weCustomersQuery);


    /**
     * 同步企业微信客户
     *
     * @return
     */
    void synchWeCustomer();


    /**
     * 同步企业客户
     *
     * @param msg
     */
    void synchWeCustomerHandler(String msg);


    /**
     * 通过跟进人id同步客户
     *
     * @param followUserIds
     */
    void synchWeCustomerByAddIds(List<String> followUserIds);



    /**
     * 构建离职待分配客户
     *
     * @param followUserIds
     */
    void buildAllocateWecustomer(List<String> followUserIds);


    /**
     * 客户打标签
     *
     * @param weMakeCustomerTag
     */
    void makeLabel(WeMakeCustomerTag weMakeCustomerTag);


    /**
     * 更新客户表的标签id,冗余字段
     * @param userId
     * @param externalUserid
     */
    void updateWeCustomerTagIds(String userId, String externalUserid);


    /**
     * 在职员工客户分配
     *
     * @param weOnTheJobCustomerQuery
     */
    void allocateOnTheJobCustomer(WeOnTheJobCustomerQuery weOnTheJobCustomerQuery);




    /**
     * 批量更新客户标签id，冗余字段
     * @param weCustomers
     */
    void batchUpdateWeCustomerTagIds(List<WeCustomer> weCustomers);


    /**
     * 客户详情基础数据
     *
     * @param externalUserid
     * @param userId
     * @return
     */
    WeCustomerDetailInfoVo findWeCustomerDetail(String externalUserid, String userId, Integer delFlag);


    /**
     * 客户画像汇总
     *
     * @return
     */
    WeCustomerDetailInfoVo findWeCustomerInfoSummary(String externalUserid, String userId, Integer delFlag);


    /**
     * 单个跟进人客户
     *
     * @return
     */
    WeCustomerDetailInfoVo findWeCustomerInfoByUserId(String externalUserid, String userId, Integer delFlag);


    /**
     * 根据员工id获取员工名称
     *
     * @return
     */
    String findUserNameByUserId(String userId);


    /**
     * 根据外部联系人ID和企业员工ID获取当前客户信息
     *
     * @param externalUserid
     * @param userid
     * @return
     */
    WeCustomerPortraitVo findCustomerByOperUseridAndCustomerId(String externalUserid, String userid) throws Exception;


    /**
     * 跟新客户画像
     *
     * @param weCustomerPortrait
     */
    void updateWeCustomerPortrait(WeCustomerPortraitVo weCustomerPortrait);


    /**
     * 根据客户id获取客户添加人
     *
     * @param externalUserid
     * @return
     */
    List<WeCustomerAddUserVo> findWeUserByCustomerId(String externalUserid);


    /**
     * 客户跟进记录
     *
     * @param trajectory
     */
    void addOrEditWaitHandle(WeCustomerTrackRecord trajectory);


    /**
     * 新增客户时间
     *
     * @param externalUserId 客户ID
     * @param userId         成员ID
     * @param state          添加此用户的渠道
     */
    void addCustomer(String externalUserId, String userId, String state);

    /**
     * 编辑客户信息
     *
     * @param externalUserId 客户ID
     * @param userId         成员ID
     */
    WeCustomer updateCustomer(String externalUserId, String userId);


    /**
     * 获取当前租户下的员工
     *
     * @return
     */
    Map<String, SysUser> findCurrentTenantSysUser();


    /**
     * 根据企业微信员工id获取当前企业员工相关信息
     *
     * @param weUserId
     * @return
     */
    SysUser findSysUserInfoByWeUserId(String weUserId);


    /**
     * 根据系统用户主建，获取当前用户信息
     *
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
     *
     * @param query
     * @return
     */
    List<WeCustomer> getCustomerListByCondition(WeCustomersQuery query);




    /**
     * 获取所有员工
     *
     * @return
     */
    List<SysUser> findAllSysUser();

    /**
     * 获取权限可见范围的员工id
     *
     * @return
     */
    List<String> findWeUserIds();

    List<WeCustomersVo> findWeCustomerList(List<String> customerIds);


    /**
     * 根据客户id获取客户，不存在则从企业微信端同步
     *
     * @param externalUserid
     * @return
     */
    WeCustomer findOrSynchWeCustomer(String externalUserid);


    /**
     * 批量编辑标签
     *
     * @param makeCustomerTags
     */
    void batchMakeLabel(WeBacthMakeCustomerTag makeCustomerTags);


    /**
     * 根据渠道统计相关客户数
     * @param state
     * @param startTime
     * @param endTime
     * @param delFlag
     * @return
     */
    List<WeCustomerChannelCountVo> countCustomerChannel(String state,
                                                        String startTime, String endTime,
                                                        Integer delFlag);

    /**
     * 统计指定渠道下累计次数
     * @param state
     * @return
     */
    Integer totalScanCodeNumber(String state);


    /**
     * 前端公共组件客户筛选封装
     * @param executeUserOrGroup
     * @return
     */
    List<WeCustomersVo> findWeCustomersForCommonAssembly( WeGroupMessageExecuteUsertipVo executeUserOrGroup );



    /**
     * 通过eid查询客户简单信息
     * @param externalUserIds
     */
    List<WeCustomerSimpleInfoVo> getCustomerSimpleInfo(List<String> externalUserIds);


    /**
     * 获取当前库中前一万条客户数，主要因为大数据客户场景下，企业微信群发只支持最多1万人
     * @return
     */
    List<WeCustomersVo> findLimitWeCustomerList();





    List<WeCustomersVo> findLimitWeCustomerList(WeCustomersQuery weCustomersQuery);
    /**
     * 通过客户id为当前客户打标签(如果是多个员工添加该客户都需打标签)
     * @param exId
     * @param weTags
     */
    void makeTagWeCustomer(String exId, List<WeTag> weTags);




    /**
     * 获取群发客户(针对全部)
     * @return
     */
    List<WeAddGroupMessageQuery.SenderInfo> findLimitSenderInfoWeCustomerList();




    List<WeCustomerChannelCountVo> getCustomerNumByState(String state, Date startTime, Date endTime);





}
