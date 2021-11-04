package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import com.linkwechat.wecom.domain.WeFlowerCustomerTagRel;
import com.linkwechat.wecom.mapper.WeFlowerCustomerRelMapper;
import com.linkwechat.wecom.mapper.WeFlowerCustomerTagRelMapper;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeFlowerCustomerRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 具有外部联系人功能企业员工也客户的关系Service业务层处理
 *
 * @author ruoyi
 * @date 2020-09-19
 */
@Service
public class WeFlowerCustomerRelServiceImpl extends ServiceImpl<WeFlowerCustomerRelMapper, WeFlowerCustomerRel> implements IWeFlowerCustomerRelService {




    @Autowired
    @Lazy
    IWeCustomerService iWeCustomerService;



    @Autowired
    WeFlowerCustomerTagRelMapper weFlowerCustomerTagRelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFollowUser(String userId, String externalUserid) {

        //如何是首位添加人删除的客户，直接删除该客户，否则直接删除关系
        WeCustomer weCustomer
                =  iWeCustomerService.getById(externalUserid);
        if(weCustomer != null){
            if(weCustomer.getFirstUserId().equals(userId)){
                //删除客户
                iWeCustomerService.removeById(externalUserid);
            }

            //删除更进关系
            this.remove(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                    .eq(WeFlowerCustomerRel::getExternalUserid,externalUserid)
                    .eq(WeFlowerCustomerRel::getUserId,userId));

            //删除跟进人给客户打的标签
            weFlowerCustomerTagRelMapper.delete(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                    .eq(WeFlowerCustomerTagRel::getExternalUserid,externalUserid)
                    .eq(WeFlowerCustomerTagRel::getUserId,userId));
        }




    }

    @Override
    public Map<String, Object> getUserAddCustomerStat(WeFlowerCustomerRel weFlowerCustomerRel) {
        Map<String, Object> resultMap = new HashMap<>(16);
        List<String> dateList = new ArrayList<>();
        List<Long> statList = new ArrayList<>();
        Long resultTotal = 0L;
        List<Map<String, Object>> userAddCustomerStatList = this.baseMapper.getUserAddCustomerStat(weFlowerCustomerRel);
        String beginTime = weFlowerCustomerRel.getBeginTime();
        String endTime = weFlowerCustomerRel.getEndTime();
        if (beginTime != null && endTime != null) {
            Date beginDate = DateUtils.dateTime(DateUtils.YYYY_MM_DD, beginTime);
            Date endDate = DateUtils.dateTime(DateUtils.YYYY_MM_DD, endTime);
            List<Date> dates = DateUtils.findDates(beginDate, endDate);
            dateList = dates.stream().map(date -> DateUtils.dateTime(date)).collect(Collectors.toList());
            Optional.ofNullable(dateList).orElseGet(ArrayList::new).forEach(date -> {
                AtomicLong total = new AtomicLong(0);
                Optional.ofNullable(userAddCustomerStatList).orElseGet(ArrayList::new).forEach(statInfo -> {
                    if (date.equals(statInfo.get("createTime"))) {
                        total.set((Long) statInfo.get("total"));
                        return;
                    }
                });
                statList.add(total.get());
            });
            resultTotal = statList.stream().mapToLong(Long::longValue).sum();
        } else {
            dateList.addAll(Optional.ofNullable(userAddCustomerStatList).orElseGet(ArrayList::new)
                    .stream()
                    .filter(statInfo -> null != statInfo.get("createTime"))
                    .map(statInfo -> (String) statInfo.get("createTime")).collect(Collectors.toList()));

            statList.addAll(Optional.ofNullable(userAddCustomerStatList).orElseGet(ArrayList::new)
                    .stream()
                    .filter(statInfo -> null != statInfo.get("total"))
                    .map(statInfo -> (Long) statInfo.get("total")).collect(Collectors.toList()));

            resultTotal = Optional.ofNullable(userAddCustomerStatList).orElseGet(ArrayList::new)
                    .stream()
                    .filter(statInfo -> null != statInfo.get("total"))
                    .mapToLong(statInfo -> (Long) statInfo.get("total")).sum();
        }
        resultMap.put("dateList", dateList);
        resultMap.put("statList", statList);
        resultMap.put("total", resultTotal);
        return resultMap;
    }
}
