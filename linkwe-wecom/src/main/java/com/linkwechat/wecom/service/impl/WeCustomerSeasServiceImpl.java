package com.linkwechat.wecom.service.impl;


import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.enums.CommunityTaskType;
import com.linkwechat.wecom.domain.WeCustomerSeas;
import com.linkwechat.wecom.domain.vo.CustomerSeasCountVo;
import com.linkwechat.wecom.domain.vo.CustomerSeasRecordVo;
import com.linkwechat.wecom.mapper.WeCustomerSeasMapper;
import com.linkwechat.wecom.service.IWeCustomerSeasService;
import com.linkwechat.wecom.service.IWeMessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class WeCustomerSeasServiceImpl extends ServiceImpl<WeCustomerSeasMapper, WeCustomerSeas> implements IWeCustomerSeasService {


    @Autowired
    private IWeMessagePushService iWeMessagePushService;

    @Override
    public CustomerSeasCountVo countCustomerSeas() {
        return this.baseMapper.countCustomerSeas();
    }

    @Override
    public List<CustomerSeasRecordVo> findSeasRecord(Integer groupByType) {
        return this.baseMapper.findSeasRecord(groupByType);
    }

    @Override
    @Async
    public void remidUser(List<String> addUserIds, Integer customerNum) {
        iWeMessagePushService.pushMessageSelfH5(addUserIds,
                "管理员给你分配了"+customerNum+"个客户还未添加,快去复制电话号码添加客户吧,", CommunityTaskType.SEAS.getType());
    }


}
