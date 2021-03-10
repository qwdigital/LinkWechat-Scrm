package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeCustomerTrajectory;
import com.linkwechat.wecom.mapper.WeCustomerTrajectoryMapper;
import com.linkwechat.wecom.service.IWeCustomerTrajectoryService;
import org.springframework.stereotype.Service;

@Service
public class WeCustomerTrajectoryServiceImpl extends ServiceImpl<WeCustomerTrajectoryMapper, WeCustomerTrajectory> implements IWeCustomerTrajectoryService {
}
