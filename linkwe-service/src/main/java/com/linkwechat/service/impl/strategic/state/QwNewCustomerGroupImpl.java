package com.linkwechat.service.impl.strategic.state;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.WeEmpleCode;
import com.linkwechat.domain.strategic.crowd.query.WeCorpStateTagSourceQuery;
import com.linkwechat.service.IWeCommunityNewGroupService;
import com.linkwechat.service.IWeEmpleCodeService;
import com.linkwechat.service.IWeStrategicCrowdStateTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QwNewCustomerGroupImpl extends IWeStrategicCrowdStateTagService {

    @Autowired
    private IWeCommunityNewGroupService iWeCommunityNewGroupService;

    @Autowired
    private IWeEmpleCodeService iWeEmpleCodeService;

    @Override
    public List<Map<String, Object>> getStateTagSourceList(WeCorpStateTagSourceQuery query) {
//        LambdaQueryWrapper<WeCommunityNewGroup> wrapper = new LambdaQueryWrapper<>();
//        wrapper.like(StringUtils.isNotEmpty(query.getName()),WeCommunityNewGroup::getEmplCodeName,query.getName());
//        wrapper.eq(WeCommunityNewGroup::getDelFlag,0);
//        List<WeCommunityNewGroup> communityNewGroups = iWeCommunityNewGroupService.list(wrapper);
//        if(CollectionUtil.isNotEmpty(communityNewGroups)){
//            List<Map<String, Object>> qrTagList = communityNewGroups.parallelStream().map(item -> {
//                Map<String, Object> map = new HashMap<>();
//                map.put("code", item.getId());
//                map.put("value", item.getEmplCodeName());
//                return map;
//            }).collect(Collectors.toList());
//            return qrTagList;
//        }
        return new ArrayList<>();
    }

    @Override
    public List<WeCustomer> getStateTagCustomerList(String code) {
//        WeCommunityNewGroup weCommunityNewGroup = iWeCommunityNewGroupService.getById(code);
//        if(weCommunityNewGroup != null){
//            WeEmpleCode weEmpleCode = iWeEmpleCodeService.getById(weCommunityNewGroup.getEmplCodeId());
//            if(weEmpleCode != null && StringUtils.isNotEmpty(weEmpleCode.getState())){
//                return weCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
//                        .eq(WeCustomer::getState, weEmpleCode.getState()).eq(WeCustomer::getDelFlag,0));
//            }
//        }
        return null;
    }
}
