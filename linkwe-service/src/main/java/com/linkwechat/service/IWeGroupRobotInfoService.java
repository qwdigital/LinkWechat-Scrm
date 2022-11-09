package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeGroupRobotInfo;
import com.linkwechat.domain.robot.query.WeRobotAddQuery;

/**
 * 群机器人信息表(WeGroupRobotInfo)
 *
 * @author danmo
 * @since 2022-11-08 16:06:01
 */
public interface IWeGroupRobotInfoService extends IService<WeGroupRobotInfo> {

    /**
     * 新增群机器人
     * @param query
     * @return
     */
    Long addGroupRobot(WeRobotAddQuery query);

    /**
     * 编辑群机器人
     * @param query
     */
    void updateRobot(WeRobotAddQuery query);
}
