package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeGroupRobotInfo;
import com.linkwechat.domain.robot.query.WeRobotAddQuery;
import com.linkwechat.mapper.WeGroupRobotInfoMapper;
import com.linkwechat.service.IWeGroupRobotInfoService;
import org.springframework.stereotype.Service;

/**
 * 群机器人信息表(WeGroupRobotInfo)
 *
 * @author danmo
 * @since 2022-11-08 16:06:01
 */
@Service
public class WeGroupRobotInfoServiceImpl extends ServiceImpl<WeGroupRobotInfoMapper, WeGroupRobotInfo> implements IWeGroupRobotInfoService {

    @Override
    public Long addGroupRobot(WeRobotAddQuery query) {
        WeGroupRobotInfo robotInfo = new WeGroupRobotInfo();
        robotInfo.setGroupName(query.getGroupName());
        robotInfo.setWebHookUrl(query.getWebHookUrl());
        save(robotInfo);
        return robotInfo.getId();
    }

    @Override
    public void updateRobot(WeRobotAddQuery query) {
        update(new LambdaUpdateWrapper<WeGroupRobotInfo>()
                .set(StringUtils.isNotEmpty(query.getGroupName()),WeGroupRobotInfo::getGroupName,query.getGroupName())
                .set(StringUtils.isNotEmpty(query.getWebHookUrl()),WeGroupRobotInfo::getWebHookUrl,query.getWebHookUrl())
                .eq(WeGroupRobotInfo::getId,query.getId()));
    }
}
