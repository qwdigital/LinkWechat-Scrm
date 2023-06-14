package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.domain.moments.entity.WeMomentsUser;

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
     * 同步数据时，添加朋友圈执行员工
     *
     * @param momentsTaskId 朋友圈任务Id
     * @param momentsId     朋友圈Id
     * @author WangYX
     * @date 2023/06/12 14:50
     */
    void syncAddMomentsUser(Long momentsTaskId, String momentsId);


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


}
