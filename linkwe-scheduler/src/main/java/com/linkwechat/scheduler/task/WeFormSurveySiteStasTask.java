package com.linkwechat.scheduler.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.SiteStasConstants;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeFormSurveyAnswer;
import com.linkwechat.domain.WeFormSurveyCatalogue;
import com.linkwechat.domain.WeFormSurveySiteStas;
import com.linkwechat.domain.WeFormSurveyStatistics;
import com.linkwechat.service.IWeFormSurveyAnswerService;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import com.linkwechat.service.IWeFormSurveySiteStasService;
import com.linkwechat.service.IWeFormSurveyStatisticsService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * 智能表单站点统计定时任务
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/14 15:03
 */
@Slf4j
@Component
public class WeFormSurveySiteStasTask {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;
    @Resource
    private IWeFormSurveySiteStasService weFormSurveySiteStasService;
    @Resource
    private IWeFormSurveyAnswerService weFormSurveyAnswerService;
    @Resource
    private IWeFormSurveyStatisticsService weFormSurveyStatisticsService;


    @XxlJob("weFormSurveySiteStasTask")
    public void process(String params) {
        log.info("智能表单站点统计定时任务>>>>>>>>>>>>>>>>>>启动 params:{}", params);
        List<WeFormSurveyCatalogue> weFormSurveyCatalogueList = weFormSurveyCatalogueService.getListIgnoreTenantId();
        if (weFormSurveyCatalogueList != null && weFormSurveyCatalogueList.size() > 0) {
            for (WeFormSurveyCatalogue weFormSurveyCatalogue : weFormSurveyCatalogueList) {
                //总的站点数据统计
                siteStas(weFormSurveyCatalogue);

                //每天的站点数据统计
                dayByDaySiteStas(weFormSurveyCatalogue);
            }
        }
    }

    /**
     * 总的数据统计
     *
     * @param weFormSurveyCatalogue
     * @return
     * @author WangYX
     * @date 2022/10/14 15:46
     */
    private void siteStas(WeFormSurveyCatalogue weFormSurveyCatalogue) {
        String[] channels = weFormSurveyCatalogue.getChannelsName().split(",");
        Integer pv = 0;
        Long uv = 0L;
        for (String channel : channels) {
            //PV
            String pvKey = StringUtils.format(SiteStasConstants.PREFIX_KEY_PV, weFormSurveyCatalogue.getId(), channel);
            pv += (Integer) redisTemplate.opsForValue().get(pvKey);
            //IP
            String ipKey = StringUtils.format(SiteStasConstants.PREFIX_KEY_IP, weFormSurveyCatalogue.getId(), channel);
            uv += redisTemplate.opsForSet().size(ipKey);
        }
        uv = uv - channels.length;

        //有效收集量
        QueryWrapper<WeFormSurveyAnswer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.lambda().eq(WeFormSurveyAnswer::getBelongId, weFormSurveyCatalogue.getId());
        answerQueryWrapper.lambda().eq(WeFormSurveyAnswer::getAnEffective, 0);
        answerQueryWrapper.lambda().eq(WeFormSurveyAnswer::getDelFlag, Constants.NORMAL_CODE);
        List<WeFormSurveyAnswer> list = weFormSurveyAnswerService.list(answerQueryWrapper);

        //上次站点统计的数据
        QueryWrapper<WeFormSurveySiteStas> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WeFormSurveySiteStas::getBelongId, weFormSurveyCatalogue.getId());
        WeFormSurveySiteStas one = weFormSurveySiteStasService.getOne(queryWrapper);

        if (ObjectUtil.isNull(one)) {
            //总的数据
            one = new WeFormSurveySiteStas();
            one.setBelongId(weFormSurveyCatalogue.getId());
            one.setTotalVisits(pv);
            one.setTotalUser(uv.intValue());
            one.setCollectionVolume(list != null ? list.size() : 0);
            boolean save = weFormSurveySiteStasService.save(one);
        } else {
            one.setTotalVisits(pv);
            one.setTotalUser(uv.intValue());
            one.setCollectionVolume(list != null ? list.size() : one.getCollectionVolume());
            weFormSurveySiteStasService.updateById(one);
        }
    }


    /**
     * 每天站点统计的数据
     *
     * @param
     * @return
     * @author WangYX
     * @date 2022/10/14 15:44
     */
    private void dayByDaySiteStas(WeFormSurveyCatalogue weFormSurveyCatalogue) {
        String[] channels = weFormSurveyCatalogue.getChannelsName().split(",");
        for (String channel : channels) {
            //PV
            String pvKey = StringUtils.format(SiteStasConstants.PREFIX_KEY_PV, weFormSurveyCatalogue.getId(), channel);
            Integer pv = (Integer) redisTemplate.opsForValue().get(pvKey);
            //IP
            String ipKey = StringUtils.format(SiteStasConstants.PREFIX_KEY_IP, weFormSurveyCatalogue.getId(), channel);
            Long uv = redisTemplate.opsForSet().size(ipKey) - 1;

            //之前总的统计数据
            QueryWrapper<WeFormSurveySiteStas> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WeFormSurveySiteStas::getBelongId, weFormSurveyCatalogue.getId());
            WeFormSurveySiteStas one = weFormSurveySiteStasService.getOne(queryWrapper);

            //今天有效收集量
            QueryWrapper<WeFormSurveyAnswer> answerQueryWrapper = new QueryWrapper<>();
            answerQueryWrapper.lambda().eq(WeFormSurveyAnswer::getBelongId, weFormSurveyCatalogue.getId());
            answerQueryWrapper.lambda().eq(WeFormSurveyAnswer::getAnEffective, 0);
            answerQueryWrapper.lambda().eq(WeFormSurveyAnswer::getDataSource, channel);
            answerQueryWrapper.lambda().eq(WeFormSurveyAnswer::getDelFlag, Constants.COMMON_STATE);
            answerQueryWrapper.lambda().apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) = '" + DateUtil.formatDate(new Date()) + "'");
            List<WeFormSurveyAnswer> list = weFormSurveyAnswerService.list(answerQueryWrapper);

            //新增每天的数据统计
            WeFormSurveyStatistics weFormSurveyStatistics = new WeFormSurveyStatistics();
            weFormSurveyStatistics.setBelongId(weFormSurveyCatalogue.getId());
            weFormSurveyStatistics.setDataSource(channel);

            //每天的总访问量
            int i = pv - (one.getTotalVisits() != null ? one.getTotalVisits() : 0);
            weFormSurveyStatistics.setTotalVisits(i < 0 ? 0 : i);
            //每天的总访问用户
            int i1 = uv.intValue() - (one.getTotalUser() != null ? one.getTotalUser() : 0);
            weFormSurveyStatistics.setTotalUser(i1 < 0 ? 0 : i1);
            weFormSurveyStatistics.setCollectionVolume(list != null ? list.size() : 0);

            //收集率
            NumberFormat percentInstance = NumberFormat.getPercentInstance();
            percentInstance.setMaximumFractionDigits(2);
            if (list != null && list.size() > 0 && uv > 0) {
                double v = Double.valueOf(list.size()) / Double.valueOf(uv);
                weFormSurveyStatistics.setCollectionRate(percentInstance.format(v));
            } else {
                weFormSurveyStatistics.setCollectionRate(percentInstance.format(0));
            }
            //平均完成时间
            if (list != null && list.size() > 0) {
                double sum = list.stream().flatMapToDouble(item -> DoubleStream.of(item.getTotalTime())).sum();
                Double averageTime = sum / 1000 / list.size();
                weFormSurveyStatistics.setAverageTime(averageTime.intValue());
            } else {
                weFormSurveyStatistics.setAverageTime(0);
            }
            weFormSurveyStatistics.setCreateTime(DateUtil.offsetDay(new Date(), -1));
            weFormSurveyStatistics.setDelFlag(Constants.COMMON_STATE);
            weFormSurveyStatisticsService.save(weFormSurveyStatistics);
        }
    }


}
