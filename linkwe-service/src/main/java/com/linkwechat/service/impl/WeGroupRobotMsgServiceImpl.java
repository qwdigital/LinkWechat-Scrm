package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeGroupRobotMsg;
import com.linkwechat.mapper.WeGroupRobotMsgMapper;
import com.linkwechat.service.IWeGroupRobotMsgService;
import org.springframework.stereotype.Service;

/**
 * 群机器人消息表(WeGroupRobotMsg)
 *
 * @author danmo
 * @since 2022-11-08 16:06:01
 */
@Service
public class WeGroupRobotMsgServiceImpl extends ServiceImpl<WeGroupRobotMsgMapper, WeGroupRobotMsg> implements IWeGroupRobotMsgService {

}
