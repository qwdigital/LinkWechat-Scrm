package com.linkwechat.quartz.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.wecom.domain.WeCorpAccount;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author danmo
 * @description 首页数据统计
 * @date 2021/2/23 23:51
 **/
@Slf4j
@Component("PageHomeDataTask")
public class PageHomeDataTask {
    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeUserService weUserService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Autowired
    private RedisCache redisCache;


    public void getCorpBasicData(){
        //查询当前使用企业
        //WeCorpAccount weCorpAccount = weCorpAccountService.findValidWeCorpAccount();
        //String corpId = weCorpAccount.getCorpId();
        Map<String,Object> totalMap = new HashMap<>(16);
        //企业成员总数
        int userCount = weUserService.count(new LambdaQueryWrapper<WeUser>().eq(WeUser::getIsActivate, WeConstans.WE_USER_IS_ACTIVATE));
        //客户总人数
        int customerCount = weCustomerService.count();
        //客户群总数
        int groupCount = weGroupService.count();
        //群成员总数
        int groupMemberCount = weGroupMemberService.count();

        totalMap.put("userCount",userCount);
        totalMap.put("customerCount",customerCount);
        totalMap.put("groupCount",groupCount);
        totalMap.put("groupMemberCount",groupMemberCount);
        redisCache.setCacheMap("getCorpBasicData",totalMap);
    }


    public void getCorpRealTimeData(){
        //今日
        //发起申请数
    }
}
