package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeGroupRobotInfo;
import com.linkwechat.mapper.WeGroupRobotInfoMapper;
import com.linkwechat.service.IWeGroupRobotInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 群机器人信息表(WeGroupRobotInfo)
 *
 * @author danmo
 * @since 2022-11-08 16:06:01
 */
@Service
public class WeGroupRobotInfoServiceImpl extends ServiceImpl<WeGroupRobotInfoMapper, WeGroupRobotInfo> implements IWeGroupRobotInfoService {

}
