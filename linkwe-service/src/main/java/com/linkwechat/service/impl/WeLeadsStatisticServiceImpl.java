package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.vo.WeLeadsConversionRateVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsDataTrendVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsUserFollowTop5VO;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.mapper.WeLeadsFollowRecordMapper;
import com.linkwechat.mapper.WeLeadsFollowerMapper;
import com.linkwechat.mapper.WeLeadsMapper;
import com.linkwechat.service.IWeLeadsStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 线索统计 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 10:22
 */
@Slf4j
@Service
public class WeLeadsStatisticServiceImpl implements IWeLeadsStatisticService {

    @Resource
    private WeLeadsMapper weLeadsMapper;
    @Resource
    private WeLeadsFollowRecordMapper weLeadsFollowRecordMapper;
    @Resource
    private WeLeadsFollowerMapper weLeadsFollowerMapper;


    @Override
    public Map<String, Object> statistic() {
        LambdaQueryWrapper<WeLeads> queryWrapper = Wrappers.lambdaQuery(WeLeads.class);
        queryWrapper.select(WeLeads::getId, WeLeads::getCustomerId, WeLeads::getLeadsStatus);
        queryWrapper.eq(WeLeads::getDelFlag, Constants.COMMON_STATE);
        List<WeLeads> weLeads = weLeadsMapper.selectList(queryWrapper);
        //线索导入总数
        int totalNum = weLeads.size();
        //已添加客户数
        long customerNum = weLeads.stream().filter(i -> i.getCustomerId() != null).count();

        //日跟进人数
        QueryWrapper<WeLeadsFollowRecord> query = Wrappers.query();
        query.eq("record_status", 1);
        query.eq("DATE_FORMAT(create_time, '%Y-%m-%d')", DateUtil.today());
        Integer followNum = weLeadsFollowRecordMapper.selectCount(query);

        NumberFormat instance = NumberFormat.getPercentInstance();
        instance.setMaximumFractionDigits(2);

        //线索转化率
        String conversionRate = instance.format(customerNum / (double) totalNum);

        //线索退回数量
        long returnNum = weLeads.stream().filter(i -> i.getLeadsStatus().equals(LeadsStatusEnum.RETURNED.getCode())).count();
        //线索退回率
        String returnRate = instance.format(returnNum / (double) totalNum);

        Map<String, Object> result = new HashMap<>(5);
        result.put("totalNum", totalNum);
        result.put("customerNum", (int) customerNum);
        result.put("followNum", followNum);
        result.put("conversionRate", conversionRate);
        result.put("returnRate", returnRate);
        return result;
    }

    @Override
    public List<WeLeadsDataTrendVO> dataTrend(String beginTime, String endTime) {

        //日期格式排序
        List<DateTime> dateTimes = DateUtil.rangeToList(DateUtil.parseDate(beginTime), DateUtil.parseDate(endTime), DateField.DAY_OF_YEAR);

        //时间范围内导入的线索数据
        LambdaQueryWrapper<WeLeads> queryWrapper = Wrappers.lambdaQuery(WeLeads.class);
        queryWrapper.select(WeLeads::getId, WeLeads::getCreateTime);
        queryWrapper.between(WeLeads::getCreateTime, DateUtil.parseDate(beginTime), DateUtil.offsetDay(DateUtil.parseDate(endTime), 1));
        List<WeLeads> importLeads = weLeadsMapper.selectList(queryWrapper);
        Map<String, List<WeLeads>> importMap = importLeads.stream().collect(Collectors.groupingBy(i -> DateUtil.formatDate(i.getCreateTime())));

        //时间范围内转化的客户数
        queryWrapper.clear();
        queryWrapper.select(WeLeads::getId, WeLeads::getBindCustomerTime);
        queryWrapper.between(WeLeads::getBindCustomerTime, DateUtil.parseDate(beginTime), DateUtil.offsetDay(DateUtil.parseDate(endTime), 1));
        List<WeLeads> conversionLeads = weLeadsMapper.selectList(queryWrapper);
        Map<String, List<WeLeads>> conversionMap = conversionLeads.stream().collect(Collectors.groupingBy(i -> DateUtil.formatDate(i.getBindCustomerTime())));

        //时间范围内的日跟进人数
        LambdaQueryWrapper<WeLeadsFollowRecord> wrapper = Wrappers.lambdaQuery(WeLeadsFollowRecord.class);
        wrapper.select(WeLeadsFollowRecord::getId, WeLeadsFollowRecord::getCreateTime);
        wrapper.eq(WeLeadsFollowRecord::getRecordStatus, 1);
        wrapper.between(WeLeadsFollowRecord::getCreateTime, DateUtil.parseDate(beginTime), DateUtil.offsetDay(DateUtil.parseDate(endTime), 1));
        List<WeLeadsFollowRecord> records = weLeadsFollowRecordMapper.selectList(wrapper);
        Map<String, List<WeLeadsFollowRecord>> recordsMap = records.stream().collect(Collectors.groupingBy(i -> DateUtil.formatDate(i.getCreateTime())));

        List<WeLeadsDataTrendVO> result = new ArrayList<>();
        for (DateTime dateTime : dateTimes) {
            WeLeadsDataTrendVO item = new WeLeadsDataTrendVO();
            String dateStr = dateTime.toDateStr();
            item.setDateStr(dateStr);
            //导入线索数据
            List<WeLeads> weLeads = importMap.get(dateStr);
            item.setLeadsNum(CollectionUtil.isNotEmpty(weLeads) ? weLeads.size() : 0);
            //转化客户数
            List<WeLeads> weLeads1 = conversionMap.get(dateStr);
            item.setCustomerNum(CollectionUtil.isNotEmpty(weLeads1) ? weLeads1.size() : 0);
            //线索日跟进人数
            List<WeLeadsFollowRecord> records1 = recordsMap.get(dateStr);
            item.setFollowNum(CollectionUtil.isNotEmpty(records1) ? records1.size() : 0);
            result.add(item);
        }
        return result;
    }

    @Override
    public List<WeLeadsConversionRateVO> conversionTop5(String beginTime, String endTime) {
        LambdaQueryWrapper<WeLeadsFollower> queryWrapper = Wrappers.lambdaQuery(WeLeadsFollower.class);
        queryWrapper.between(WeLeadsFollower::getFollowerStartTime, DateUtil.parseDate(beginTime), DateUtil.offsetDay(DateUtil.parseDate(endTime), 1));
        List<WeLeadsFollower> list = weLeadsFollowerMapper.selectList(queryWrapper);
        Map<String, List<WeLeadsFollower>> map = list.stream().collect(Collectors.groupingBy(WeLeadsFollower::getFollowerName));
        List<WeLeadsConversionRateVO> result = new ArrayList<>();
        map.entrySet().forEach(i -> {
            WeLeadsConversionRateVO vo = new WeLeadsConversionRateVO();
            vo.setUserName(i.getKey());
            List<WeLeadsFollower> value = i.getValue();

            List<WeLeadsFollower> conversionList = value.stream().filter(j -> j.getFollowerStatus().equals(2)).collect(Collectors.toList());
            NumberFormat instance = NumberFormat.getIntegerInstance();
            String format = instance.format(conversionList.size() / Double.valueOf(value.size()) * 100);
            vo.setRate(format);
            result.add(vo);
        });
        result.sort(Comparator.comparing(WeLeadsConversionRateVO::getRate));
        if (result.size() > 5) {
            return CollectionUtil.sub(result, 0, 5);
        }
        return result;
    }

    @Override
    public List<WeLeadsUserFollowTop5VO> userFollowTop5(String beginTime, String endTime) {
        LambdaQueryWrapper<WeLeadsFollower> queryWrapper = Wrappers.lambdaQuery(WeLeadsFollower.class);
        queryWrapper.eq(WeLeadsFollower::getFollowerStatus, LeadsStatusEnum.BE_FOLLOWING_UP.getCode());
        queryWrapper.between(WeLeadsFollower::getFollowerStartTime, DateUtil.parseDate(beginTime), DateUtil.offsetDay(DateUtil.parseDate(endTime), 1));
        List<WeLeadsFollower> list = weLeadsFollowerMapper.selectList(queryWrapper);
        Map<String, List<WeLeadsFollower>> map = list.stream().collect(Collectors.groupingBy(WeLeadsFollower::getFollowerName));
        List<WeLeadsUserFollowTop5VO> result = new ArrayList<>();
        map.entrySet().forEach(i -> {
            WeLeadsUserFollowTop5VO vo = new WeLeadsUserFollowTop5VO();
            vo.setUserName(i.getKey());
            vo.setFollowNum(i.getValue().size());
            result.add(vo);
        });
        result.sort(Comparator.comparing(WeLeadsUserFollowTop5VO::getFollowNum));
        if (result.size() > 5) {
            return CollectionUtil.sub(result, 0, 5);
        }
        return result;
    }
}
