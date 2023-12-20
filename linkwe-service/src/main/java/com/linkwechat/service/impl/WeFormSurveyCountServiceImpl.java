package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.enums.LockEnums;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.ip.IpUtils;
import com.linkwechat.domain.WeFormSurveyCount;
import com.linkwechat.domain.WeFormSurveyStatistics;
import com.linkwechat.domain.form.vo.WeFormSurveyStatisticsVO;
import com.linkwechat.service.IWeFormSurveyCountService;
import com.linkwechat.mapper.WeFormSurveyCountMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
* @author robin
* @description 针对表【we_form_survey_count(智能表单统计(按照每天的维度统计相关客户数据；ip+当天定位每一条记录))】的数据库操作Service实现
* @createDate 2023-12-11 18:21:45
*/
@Service
public class WeFormSurveyCountServiceImpl extends ServiceImpl<WeFormSurveyCountMapper, WeFormSurveyCount>
    implements IWeFormSurveyCountService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void weFormCount(Long belongId,String dataSource, String visitorIp) {

        RLock lock = redissonClient.getLock(LockEnums.WE_FORM_SURVEY_COUNT_LOCK.getCode());
        if(lock.tryLock()){
            try {
                List<WeFormSurveyCount> weFormSurveyCounts = this.list(new LambdaQueryWrapper<WeFormSurveyCount>()
                        .eq(WeFormSurveyCount::getBelongId, belongId)
                        .eq(WeFormSurveyCount::getVisitorIp, visitorIp)
                        .eq(WeFormSurveyCount::getDataSource,dataSource)
                        .apply("date_format (create_time,'%Y-%m-%d') = date_format ({0},'%Y-%m-%d')", new Date()));
                if(CollectionUtil.isNotEmpty(weFormSurveyCounts)){
                    WeFormSurveyCount weFormSurveyCount = weFormSurveyCounts.stream().findFirst().get();
                    weFormSurveyCount.setTotalVisits(
                            weFormSurveyCount.getTotalVisits()+1
                    );

                    this.updateById(weFormSurveyCount);

                }else{

                    this.save(
                            WeFormSurveyCount.builder()
                                    .totalVisits(new Long(1))
                                    .belongId(belongId)
                                    .visitorIp(visitorIp)
                                    .dataSource(dataSource)
                                    .build()
                    );
                }

             }finally {
                lock.unlock();
             }
        }

    }


    @Override
    public void setVisitTime(Long belongId,String visitorIp,String dataSource,Long visitTime){
        List<WeFormSurveyCount> weFormSurveyCounts = this.list(new LambdaQueryWrapper<WeFormSurveyCount>()
                .eq(WeFormSurveyCount::getBelongId, belongId)
                .eq(WeFormSurveyCount::getVisitorIp, visitorIp)
                .eq(WeFormSurveyCount::getDataSource,dataSource)
                .apply("date_format (create_time,'%Y-%m-%d') = date_format ({0},'%Y-%m-%d')", new Date()));

        if(CollectionUtil.isNotEmpty(weFormSurveyCounts)){
            WeFormSurveyCount weFormSurveyCount = weFormSurveyCounts.stream().findFirst().get();
            weFormSurveyCount.setTotalTime(visitTime);
            this.updateById(weFormSurveyCount);
        }


    }
    @Override
    public WeFormSurveyStatistics getStatistics(WeFormSurveyCount weFormSurveyCount) {
        return this.baseMapper.getStatistics(weFormSurveyCount);
    }

    @Override
    public List<WeFormSurveyStatistics> findDataList(WeFormSurveyCount weFormSurveyCount) {

        return this.baseMapper.findDataList(weFormSurveyCount);
    }

    @Override
    public List<WeFormSurveyStatistics> lineChart(WeFormSurveyCount weFormSurveyCount) {
        return this.baseMapper.lineChart(weFormSurveyCount);
    }

    @Override
    public Integer sumTotalVisits(Long belongId) {
        Integer sumTotalVisits = this.baseMapper.sumTotalVisits(belongId);
        if(null != sumTotalVisits){
            return sumTotalVisits;
        }
        return new Integer(0);
    }


}




