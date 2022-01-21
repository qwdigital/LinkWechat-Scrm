package com.linkwechat.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeGroupStatistic;
import com.linkwechat.wecom.domain.dto.GroupChatStatisticDto;
import com.linkwechat.wecom.domain.query.GroupChatStatisticQuery;
import com.linkwechat.wecom.service.IWeGroupService;
import com.linkwechat.wecom.service.IWeGroupStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 群聊数据统计
 * @date 2021/2/24 0:42
 **/
@Slf4j
@Component("GroupChatStatisticTask")
public class GroupChatStatisticTask {
    @Autowired
    private WeCustomerClient weCustomerClient;
    @Autowired
    private IWeGroupService weGroupService;
    @Autowired
    private IWeGroupStatisticService weGroupStatisticService;

    public void getGroupChatData() {
        log.info("群聊数据统计>>>>>>>>>>>>>>>>>>>启动");
        List<WeGroup> weGroupList = weGroupService.list(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getStatus, 0));
        log.info("群聊数据统计>>>>>>>>>>>>>>>>>>>weGroupList:{}",weGroupList.size());
        if (CollectionUtil.isNotEmpty(weGroupList)){
            List<WeGroupStatistic> weGroupStatisticList = new ArrayList<>();
            GroupChatStatisticQuery query = new GroupChatStatisticQuery();
            //前一天的数据
            Long startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(),-1)).getTime()/1000;
            Long endTime = DateUtil.endOfDay(DateUtil.offsetDay(new Date(),-1)).getTime()/1000;
            query.setDay_begin_time(startTime);
            query.setDay_end_time(endTime);
            weGroupList.forEach(weGroup -> {
                GroupChatStatisticQuery.OwnerFilter ownerFilter = new GroupChatStatisticQuery.OwnerFilter();
                List<String> idList = new ArrayList<>();
                idList.add(weGroup.getOwner());
                ownerFilter.setUserid_list(idList);
                query.setOwnerFilter(ownerFilter);
                try {
                    GroupChatStatisticDto groupChatStatistic = weCustomerClient.getGroupChatStatisticGroupByDay(query);
                    List<GroupChatStatisticDto.GroupchatStatisticData> items = groupChatStatistic.getItems();
                    if(CollectionUtil.isNotEmpty(items)){
                        items.forEach(groupchatStatisticData -> {
                            WeGroupStatistic weGroupStatistic = new WeGroupStatistic();
                            GroupChatStatisticDto.StatisticData data = groupchatStatisticData.getData();
                            BeanUtils.copyPropertiesignoreOther(data, weGroupStatistic);
                            weGroupStatistic.setChatId(weGroup.getChatId());
                            weGroupStatistic.setStatTime(groupchatStatisticData.getStatTime());
                            weGroupStatisticList.add(weGroupStatistic);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("群聊数据拉取失败: ownerFilter:【{}】,ex:【{}】", JSONObject.toJSONString(ownerFilter),e.getStackTrace());
                }
            });
            weGroupStatisticService.saveBatch(weGroupStatisticList);
        }
    }

    private Long strToDate(int days, Integer type) {
        Long time = null;
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.DATE, days);
        String tarday = new SimpleDateFormat("yyyy-MM-dd").format(cale.getTime());
        if (type.equals(0)) {
            tarday += " 00:00:00";
        } else {
            tarday += " 23:59:59";
        }
        // String转Date
        try {
            date = format2.parse(tarday);
            System.out.println(date.getTime());
            time = date.getTime() / 1000;
            System.out.println(time.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
