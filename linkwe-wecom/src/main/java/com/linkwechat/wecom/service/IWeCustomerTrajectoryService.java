package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomerTrajectory;

public interface IWeCustomerTrajectoryService extends IService<WeCustomerTrajectory> {

    void waitHandleMsg(String url);


    void inforMationNews(String userId,String externalUserid,Integer trajectoryType);


    void deleteSynchTrajectory();

}
