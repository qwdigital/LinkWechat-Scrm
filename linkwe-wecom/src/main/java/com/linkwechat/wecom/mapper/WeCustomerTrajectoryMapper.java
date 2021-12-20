package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerTrajectory;

public interface WeCustomerTrajectoryMapper extends BaseMapper<WeCustomerTrajectory> {

    void deleteSynchTrajectory();


}
