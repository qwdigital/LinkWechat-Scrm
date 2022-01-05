package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.enums.TrajectoryType;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeCustomerList;
import com.linkwechat.wecom.domain.WeCustomerTrajectory;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.mapper.WeCustomerTrajectoryMapper;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeCustomerTrajectoryService;
import com.linkwechat.wecom.service.IWeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeCustomerTrajectoryServiceImpl extends ServiceImpl<WeCustomerTrajectoryMapper, WeCustomerTrajectory> implements IWeCustomerTrajectoryService {





    @Autowired
    private IWeUserService iWeUserService;

    @Autowired
    @Lazy
    private IWeCustomerService iWeCustomerService;



    @Override
    public void deleteSynchTrajectory() {
        this.baseMapper.deleteSynchTrajectory();
    }

    @Override
    public List<WeCustomerTrajectory> followUpRecord(String externalUserid, String userId, Integer trajectoryType) {
        return this.baseMapper.followUpRecord(externalUserid,userId,trajectoryType);
    }


    /***
     * 构建轨迹
     * @param trajectRels 员工客户构建关系列表
     * @param trajectoryType 轨迹类型
     * @param trajectorySence 轨迹场景，落实到标签
     * @param others 其他附加条件，比如多个标签，逗号隔开等
     */
    @Override
    @Async
    public void createTrajectory(List<WeCustomerTrajectory.TrajectRel> trajectRels, Integer trajectoryType, Integer trajectorySence,String others,Integer trackState) {


        List<WeCustomerTrajectory> trajectories=new ArrayList<>();

            trajectRels.stream().forEach(k->{
                WeCustomerTrajectory trajectory = WeCustomerTrajectory.builder()
                        .externalUserid(k.getCustomerId())
                        .trajectoryType(trajectoryType)
                        .title(TrajectorySceneType.of(trajectorySence).getName())
                        .trackTime(new Date())
                        .userId(k.getUserId())
                        .trackState(trackState)
                        .build();

                if(TrajectorySceneType.TRAJECTORY_TITLE_DZPYQ.getType().equals(trajectorySence)
                        ||TrajectorySceneType.TRAJECTORY_TITLE_PLPYQ.getType().equals(trajectorySence)){
                    trajectory.setContent(
                            String.format(TrajectorySceneType.of(trajectorySence).getMsgTpl(),
                                    iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                                            .eq(WeCustomer::getFirstUserId, k.getUserId())
                                            .eq(WeCustomer::getExternalUserid, k.getCustomerId())).getCustomerName(),
                                    iWeUserService.getById(k.getUserId()).getName())
                    );
                }else if(TrajectorySceneType.TRAJECTORY_TITLE_BJBQ.getType().equals(trajectorySence)){
                    trajectory.setContent(
                            String.format(TrajectorySceneType.of(trajectorySence).getMsgTpl(),
                                    iWeUserService.getById(k.getUserId()).getName(),
                                    iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                                            .eq(WeCustomer::getFirstUserId, k.getUserId())
                                            .eq(WeCustomer::getExternalUserid, k.getCustomerId())).getCustomerName())
                    );
                } else if(TrajectorySceneType.TRAJECTORY_TITLE_GXQYBQ.getType().equals(trajectorySence)
                        ||TrajectorySceneType.TRAJECTORY_TITLE_GXGRBQ.getType().equals(trajectorySence)
                        ||TrajectorySceneType.TRAJECTORY_TITLE_TJGJ.getType().equals(trajectorySence)){

                    trajectory.setContent(
                            String.format(TrajectorySceneType.of(trajectorySence).getMsgTpl(),
                                    iWeUserService.getById(k.getUserId()).getName(),
                                    others)
                    );

                }else if(TrajectorySceneType.TRAJECTORY_TITLE_SCYG.getType().equals(trajectorySence)){
                    trajectory.setContent(
                            String.format(TrajectorySceneType.of(trajectorySence).getMsgTpl(),
                                    iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                                            .eq(WeCustomer::getFirstUserId, k.getUserId())
                                            .eq(WeCustomer::getExternalUserid, k.getCustomerId())).getCustomerName()
                                    , iWeUserService.getById(k.getUserId()).getName())
                    );
                }else if(TrajectorySceneType.TRAJECTORY_TITLE_TJYG.getType().equals(trajectorySence)){
                    trajectory.setContent(
                            String.format(TrajectorySceneType.of(trajectorySence).getMsgTpl(),
                                    iWeCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                                            .eq(WeCustomer::getFirstUserId, k.getUserId())
                                            .eq(WeCustomer::getExternalUserid, k.getCustomerId())).getCustomerName(),
                                    others,
                                    iWeUserService.getById(k.getUserId()).getName())
                    );

                }


                trajectories.add(
                        trajectory
                    );

            });

        if(CollectionUtil.isNotEmpty(trajectories)){
            this.deleteSynchTrajectory();
            this.saveBatch(trajectories);
        }







    }


}
