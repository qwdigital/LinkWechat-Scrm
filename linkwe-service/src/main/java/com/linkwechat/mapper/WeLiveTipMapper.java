package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.linkwechat.domain.live.WeLiveTaskDetailTab;
import com.linkwechat.domain.live.WeLiveTaskUserDetail;
import com.linkwechat.domain.live.WeLiveTip;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* @author robin
* @description 针对表【we_live_tip(直播员工群发通知消息表)】的数据库操作Mapper
* @createDate 2022-10-26 22:40:45
* @Entity generator.domain.WeLiveTip
*/
public interface WeLiveTipMapper extends BaseMapper<WeLiveTip> {


    List<WeLiveTip> findWeLiveTip(@Param(Constants.WRAPPER) Wrapper wrapper,@Param("sendTargetType") Integer sendTargetType);


    List<WeLiveTaskUserDetail> findWeLiveTaskUserDetail(@Param("sendTargetType") Integer sendTargetType, @Param("userName") String userName, @Param("liveId") String liveId);


    List<WeLiveTaskUserDetail> findWeLiveTaskCustomerDetail(@Param("sendTargetType") Integer sendTargetType,@Param("userName") String userName,@Param("liveId") String liveId,
    @Param("sendState") Integer sendState);


    WeLiveTaskDetailTab findWeLiveTaskUserDetailTab(@Param("liveId") String liveId, @Param("sendTargetType") Integer sendTargetType);

    WeLiveTaskDetailTab findWeLiveTaskExecuteUserDetailTab(@Param("liveId") String liveId,@Param("sendTargetType") Integer sendTargetType);
}
