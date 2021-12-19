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
import com.linkwechat.wecom.domain.WeCustomerTrajectory;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.mapper.WeCustomerTrajectoryMapper;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.service.IWeCustomerTrajectoryService;
import com.linkwechat.wecom.service.IWeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeCustomerTrajectoryServiceImpl extends ServiceImpl<WeCustomerTrajectoryMapper, WeCustomerTrajectory> implements IWeCustomerTrajectoryService {



    @Autowired
    private WeMessagePushClient weMessagePushClient;
//
//
////    @Autowired
////    private IWeCustomerService iWeCustomerService;
//
    @Autowired
    private WeCustomerMapper weCustomerMapper;
//
//
//
//    @Autowired
//    private IWeUserService iWeUserService;

    @Autowired
    private WeUserMapper weUserMapper;

    /**
     * 待办理处理通知
     */
    @Override
    public void waitHandleMsg(String url) {
        //获取所有满足时间的通知
        List<WeCustomerTrajectory> trajectories = this.list(new LambdaQueryWrapper<WeCustomerTrajectory>()
                .ne(WeCustomerTrajectory::getStatus, Constants.DELETE_CODE)
                .last(" AND concat_ws(' ',create_date,start_time)  <= DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i:%s')" +
                        " AND concat_ws(' ',create_date,end_time) >= DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i:%s')"));
        if(CollectionUtil.isNotEmpty(trajectories)){

            List<WeCustomer> weCustomers = weCustomerMapper.selectBatchIds(
                    trajectories.stream().map(WeCustomerTrajectory::getExternalUserid).collect(Collectors.toList())
            );
            Map<String, WeCustomer> weCustomerMap
                    = weCustomers.stream().collect(Collectors.toMap(WeCustomer::getExternalUserid, a -> a, (k1, k2) -> k1));
            //给员工发送通知
            trajectories.stream().forEach(k->{
                weMessagePushClient.sendMessageToUser(WeMessagePushDto.builder()
                                .touser(k.getUserId())
                                .msgtype(MessageType.TEXT.getMessageType())
                                .agentid(Integer.parseInt(k.getAgentId()))
                                .text(TextMessageDto.builder()
                                        .content("您有一项关于客户"+weCustomerMap.get(k.getExternalUserid()).getCustomerName()+"的待办任务提醒，请尽快处理，\n<a href="+url+">点击查看客户详情。</a>")
                                        .build())
                                .build(),
                        String.valueOf(1000012)
                );
            });

        }
    }


    /**
     * 信息动态编辑
     * @param userId
     * @param externalUserid
     * @param trajectoryType
     */
    @Override
    public void inforMationNews(String userId, String externalUserid, Integer trajectoryType) {
        WeUser weUser = weUserMapper.selectById(userId);
        if(null != weUser){
            this.save(
                    WeCustomerTrajectory.builder()
                            .externalUserid(externalUserid)
                            .trajectoryType(TrajectoryType.TRAJECTORY_TYPE_XXDT.getType())
                            .createTime(new DateTime())
                            .userId(userId)
                            .content(
                                    TrajectorySceneType.TRAJECTORY_TYPE_XXDT_BCZL.getKey().equals(trajectoryType)?
                                            "员工"+weUser.getName()+"编辑了当前客户信息":"员工"+weUser.getName()+"编辑了当前客户标签"
                            ).build()
            );
        }
    }


}
