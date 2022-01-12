package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCustomerTrajectory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeCustomerTrajectoryMapper extends BaseMapper<WeCustomerTrajectory> {

    void deleteSynchTrajectory();

    List<WeCustomerTrajectory> followUpRecord(@Param("externalUserid") String externalUserid,
                                              @Param("userId") String userId,@Param("trajectoryType") Integer trajectoryType);


}
