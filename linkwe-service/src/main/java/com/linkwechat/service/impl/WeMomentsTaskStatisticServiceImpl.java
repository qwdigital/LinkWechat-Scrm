package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.domain.moments.entity.*;
import com.linkwechat.mapper.*;
import com.linkwechat.service.IWeMomentsTaskStatisticService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 朋友圈统计
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/20 9:54
 */
@Service
public class WeMomentsTaskStatisticServiceImpl implements IWeMomentsTaskStatisticService {

    @Resource
    private WeMomentsUserMapper weMomentsUserMapper;
    @Resource
    private WeMomentsCustomerMapper weMomentsCustomerMapper;
    @Resource
    private WeMomentsInteracteMapper weMomentsInteracteMapper;
    @Resource
    private WeMomentsTaskMapper weMomentsTaskMapper;
    @Resource
    private WeMomentsEstimateUserMapper weMomentsEstimateUserMapper;
    @Resource
    private WeMomentsEstimateCustomerMapper weMomentsEstimateCustomerMapper;


    @Override
    public Map<String, Long> userStatistic(Long weMomentsTaskId) {

        Map<String, Long> result = new HashMap<>(5);
        result.put("targetExecute", 0L);
        result.put("nonExecute", 0L);
        result.put("executed", 0L);
        result.put("todayExecute", 0L);
        result.put("remindCount", 0L);

        //获取朋友圈任务详情
        WeMomentsTask weMomentsTask = weMomentsTaskMapper.selectById(weMomentsTaskId);
        if (BeanUtil.isEmpty(weMomentsTask)) {
            return result;
        }

        //成员群发
        if (weMomentsTask.getSendType().equals(2)) {
            LambdaQueryWrapper<WeMomentsEstimateUser> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(WeMomentsEstimateUser::getMomentsTaskId, weMomentsTaskId);
            List<WeMomentsEstimateUser> weMomentsEstimateUsers = weMomentsEstimateUserMapper.selectList(wrapper);
            if(CollectionUtil.isNotEmpty(weMomentsEstimateUsers)){
                result.put("targetExecute", Long.valueOf(weMomentsEstimateUsers.size()));
                result.put("nonExecute", Long.valueOf(weMomentsEstimateUsers.size()));
                Integer executeCount = weMomentsEstimateUsers.stream().max(Comparator.comparing(WeMomentsEstimateUser::getExecuteCount)).get().getExecuteCount();
                result.put("remindCount", Long.valueOf(executeCount));
            }
        }

        //查询数据
        LambdaQueryWrapper<WeMomentsUser> queryWrapper = Wrappers.lambdaQuery(WeMomentsUser.class);
        queryWrapper.eq(WeMomentsUser::getMomentsTaskId, weMomentsTaskId);
        queryWrapper.eq(WeMomentsUser::getDelFlag, Constants.COMMON_STATE);
        List<WeMomentsUser> list = weMomentsUserMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            //当天日期的开始时间和结束时间
            DateTime date = DateUtil.date();
            DateTime beginTime = DateUtil.beginOfDay(date);
            DateTime endTime = DateUtil.endOfDay(date);

            long targetExecute = Long.valueOf(list.size());
            long nonExecute = list.stream().filter(i -> i.getExecuteStatus().equals(0)).count();
            long executed = list.stream().filter(i -> i.getExecuteStatus().equals(1)).count();
            long todayExecute = list.stream().filter(i -> i.getExecuteStatus().equals(1)).filter(i -> beginTime.getTime() <= i.getUpdateTime().getTime() && i.getUpdateTime().getTime() <= endTime.getTime()).count();
            long remindCount = Long.valueOf(list.stream().max(Comparator.comparing(WeMomentsUser::getExecuteCount)).get().getExecuteCount());

            //返回结果
            if (weMomentsTask.getSendType().equals(2)) {
                result.put("nonExecute", result.get("targetExecute") - executed);
            } else {
                result.put("targetExecute", targetExecute);
                result.put("nonExecute", nonExecute);
                result.put("remindCount", remindCount);
            }
            result.put("executed", executed);
            result.put("todayExecute", todayExecute);
        }
        return result;
    }

    @Override
    public Map<String, Long> customerStatistic(Long weMomentsTaskId) {

        Map<String, Long> result = new HashMap<>(5);
        result.put("predictSend", 0L);
        result.put("nonSend", 0L);
        result.put("sent", 0L);
        result.put("failSend", 0L);
        result.put("todaySend", 0L);

        //获取朋友圈任务详情
        WeMomentsTask weMomentsTask = weMomentsTaskMapper.selectById(weMomentsTaskId);
        if (BeanUtil.isEmpty(weMomentsTask)) {
            return result;
        }

        //成员群发
        if (weMomentsTask.getSendType().equals(2)) {
            LambdaQueryWrapper<WeMomentsEstimateCustomer> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(WeMomentsEstimateCustomer::getMomentsTaskId, weMomentsTaskId);
            Integer count = weMomentsEstimateCustomerMapper.selectCount(wrapper);
            result.put("predictSend", Long.valueOf(count));
            result.put("nonSend", Long.valueOf(count));
        }

        LambdaQueryWrapper<WeMomentsCustomer> queryWrapper = Wrappers.lambdaQuery(WeMomentsCustomer.class);
        queryWrapper.eq(WeMomentsCustomer::getMomentsTaskId, weMomentsTaskId);
        queryWrapper.eq(WeMomentsCustomer::getDelFlag, Constants.COMMON_STATE);
        List<WeMomentsCustomer> list = weMomentsCustomerMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {

            //当天日期的开始时间和结束时间
            DateTime date = DateUtil.date();
            DateTime beginTime = DateUtil.beginOfDay(date);
            DateTime endTime = DateUtil.endOfDay(date);

            long predictSend = Long.valueOf(list.size());
            long nonSend = list.stream().filter(i -> i.getDeliveryStatus().equals(1)).count();
            long sent = list.stream().filter(i -> i.getDeliveryStatus().equals(0)).count();
            long todaySend = list.stream().filter(i -> i.getDeliveryStatus().equals(0)).filter(i -> beginTime.getTime() <= i.getUpdateTime().getTime() && i.getUpdateTime().getTime() <= endTime.getTime()).count();

            if (weMomentsTask.getSendType().equals(2)) {
                result.put("nonSend", result.get("predictSend") - sent);
            } else {
                result.put("predictSend", predictSend);
                result.put("failSend", predictSend - nonSend - sent);
                result.put("nonSend", nonSend);
            }
            result.put("sent", sent);
            result.put("todaySend", todaySend);
        }
        return result;
    }

    @Override
    public Map<String, Long> interactStatistic(Long weMomentsTaskId) {
        Map<String, Long> result = new HashMap<>(5);
        result.put("comment", 0L);
        result.put("like", 0L);
        result.put("people", 0L);
        result.put("count", 0L);

        LambdaQueryWrapper<WeMomentsInteracte> queryWrapper = Wrappers.lambdaQuery(WeMomentsInteracte.class);
        queryWrapper.eq(WeMomentsInteracte::getMomentsTaskId, weMomentsTaskId);
        queryWrapper.eq(WeMomentsInteracte::getInteracteUserType, 1);
        queryWrapper.eq(WeMomentsInteracte::getDelFlag, Constants.COMMON_STATE);
        List<WeMomentsInteracte> list = weMomentsInteracteMapper.selectList(queryWrapper);

        if (CollectionUtil.isNotEmpty(list)) {
            long comment = list.stream().filter(i -> i.getInteracteType().equals(0)).count();
            long like = list.stream().filter(i -> i.getInteracteType().equals(1)).count();

            result.put("comment", comment);
            result.put("like", like);
            result.put("people", 0L);
            result.put("count", 0L);
        }
        return result;
    }
}
