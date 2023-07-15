package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.domain.moments.dto.MomentsListDetailResultDto;
import com.linkwechat.domain.moments.entity.WeMomentsUser;
import com.linkwechat.domain.moments.query.WeMomentsTaskMobileRequest;
import com.linkwechat.domain.moments.vo.WeMomentsTaskMobileVO;

import java.util.List;


/**
 * 朋友圈员工 服务类
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 10:05
 */
public interface IWeMomentsUserService extends IService<WeMomentsUser> {


    /**
     * 添加朋友圈员工
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param users         员工
     * @return
     * @author WangYX
     * @date 2023/06/08 16:12
     */
    void addMomentsUser(Long momentsTaskId, List<SysUser> users);


    /**
     * 获取朋友圈任务执行员工
     *
     * @param scopeType 发送范围: 0全部客户 1按条件筛选
     * @param deptIds   部门Id集合
     * @param postIds   岗位名称集合
     * @param weUserIds 企微员工Id集合
     * @return {@link List< SysUser>}
     * @author WangYX
     * @date 2023/06/08 16:59
     */
    List<SysUser> getMomentsTaskExecuteUser(Integer scopeType, List<Long> deptIds, List<String> postIds, List<String> weUserIds);


    /**
     * 同步数据时，添加朋友圈执行员工 (仅支持企业群发)
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     朋友圈Id 仅支持企业发表的朋友圈id
     * @author WangYX
     * @date 2023/06/12 14:50
     */
    void syncAddMomentsUser(Long momentsTaskId, String momentsId);

    /**
     * 同步数据时，添加朋友圈执行员工
     * <p>
     * 兼容企业群发和个人发送
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param moment        企微朋友圈
     * @param sysUser       发送员工
     * @return
     * @author WangYX
     * @date 2023/06/30 11:05
     */
    void syncAddMomentsUser(Long momentsTaskId, MomentsListDetailResultDto.Moment moment, SysUser sysUser);


    /**
     * 同步数据时，更新朋友圈员工的执行情况
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     朋友圈Id
     * @return
     * @author WangYX
     * @date 2023/06/12 16:58
     */
    void syncUpdateMomentsUser(Long momentsTaskId, String momentsId);


    /**
     * 移动端列表
     *
     * @param request 移动端列表查询参数
     * @return {@link String}
     * @author WangYX
     * @date 2023/06/20 18:44
     */
    List<WeMomentsTaskMobileVO> mobileList(WeMomentsTaskMobileRequest request);


    /**
     * 移动端详情
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @return {@link WeMomentsTaskMobileVO}
     * @author WangYX
     * @date 2023/06/21 9:59
     */
    WeMomentsTaskMobileVO mobileGet(Long weMomentsTaskId);

    /**
     * 移动端-数量
     *
     * @param request 移动端列表查询参数
     * @return {@link int}
     * @author WangYX
     * @date 2023/06/26 18:53
     */
    int count(WeMomentsTaskMobileRequest request);


}
