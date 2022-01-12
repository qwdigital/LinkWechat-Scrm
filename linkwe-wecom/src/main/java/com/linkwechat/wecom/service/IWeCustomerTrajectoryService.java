package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomerTrajectory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IWeCustomerTrajectoryService extends IService<WeCustomerTrajectory> {







    void deleteSynchTrajectory();

    List<WeCustomerTrajectory> followUpRecord(String externalUserid,
                                              String userId,Integer trajectoryType);


    void createTrajectory(List<WeCustomerTrajectory.TrajectRel> trajectRels,Integer trajectoryType,Integer trajectorySence,String others,Integer trackState);

}
