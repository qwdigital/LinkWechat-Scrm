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
import com.linkwechat.domain.leads.leads.vo.WeLeadsDataTrendVO;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.mapper.WeLeadsFollowRecordMapper;
import com.linkwechat.mapper.WeLeadsMapper;
import com.linkwechat.service.IWeLeadsStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        //日跟进日数
        QueryWrapper<WeLeadsFollowRecord> query = Wrappers.query();
        query.eq("record_status", 1);
        query.eq("DATE_FORMAT(create_time, '%Y-%m-%d')", DateUtil.today());
        Integer followNum = weLeadsFollowRecordMapper.selectCount(query);

        NumberFormat instance = NumberFormat.getCurrencyInstance();
        instance.setMaximumFractionDigits(2);

        //线索转化率
        String conversionRate = instance.format(customerNum / (double) totalNum);

        //线索退回数量
        long returnNum = weLeads.stream().filter(i -> i.getLeadsStatus().equals(LeadsStatusEnum.RETURNED.getCode())).count();
        //线索退回率
        String returnRate = instance.format(returnNum / (double) totalNum);

        Map<String, Object> result = new HashMap<>(5);
        result.put("totalNum", totalNum);
        result.put("customerNum", customerNum);
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
        }
        return result;
    }
}
