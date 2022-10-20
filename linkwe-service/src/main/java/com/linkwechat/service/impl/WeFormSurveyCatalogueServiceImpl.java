package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.SiteStatsConstants;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeFormSurveyCatalogue;
import com.linkwechat.domain.WeFormSurveyStatistics;
import com.linkwechat.domain.form.query.WeAddFormSurveyCatalogueQuery;
import com.linkwechat.domain.form.query.WeFormSurveyCatalogueQuery;
import com.linkwechat.mapper.WeFormSurveyCatalogueMapper;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import com.linkwechat.service.IWeFormSurveyStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 问卷-目录列表(WeFormSurveyCatalogue)
 *
 * @author danmo
 * @since 2022-09-20 18:02:56
 */
@Slf4j
@Service
public class WeFormSurveyCatalogueServiceImpl extends ServiceImpl<WeFormSurveyCatalogueMapper, WeFormSurveyCatalogue> implements IWeFormSurveyCatalogueService {

    @Autowired
    private IWeFormSurveyStatisticsService weFormSurveyStatisticsService;
    @Resource
    private WeFormSurveyCatalogueMapper weFormSurveyCatalogueMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Long add(WeAddFormSurveyCatalogueQuery query) {
        List<WeFormSurveyCatalogue> surveyCatalogues = list(new LambdaQueryWrapper<WeFormSurveyCatalogue>()
                .eq(WeFormSurveyCatalogue::getSurveyName, query.getSurveyName())
                .eq(WeFormSurveyCatalogue::getDelFlag, 0));
        if (CollectionUtil.isNotEmpty(surveyCatalogues)) {
            throw new WeComException("表单名称已重复!");
        }
        WeFormSurveyCatalogue catalogue = new WeFormSurveyCatalogue();
        BeanUtil.copyProperties(query, catalogue);
        save(catalogue);

        //保存站点数据到Redis
        String channelsName = catalogue.getChannelsName();
        if (StringUtils.isNotBlank(channelsName)) {
            String[] split = channelsName.split(",");
            for (String channelName : split) {
                //PV
                String pvKey = StringUtils.format(SiteStatsConstants.PREFIX_KEY_PV, catalogue.getId(), channelName);
                redisTemplate.opsForValue().set(pvKey, 0);
                //IP
                String ipKey = StringUtils.format(SiteStatsConstants.PREFIX_KEY_IP, catalogue.getId(), channelName);
                redisTemplate.opsForSet().add(ipKey, "");
            }
        }
        return catalogue.getId();
    }

    @Override
    public void deleteSurvey(List<Long> ids) {
        update(new LambdaUpdateWrapper<WeFormSurveyCatalogue>()
                .set(WeFormSurveyCatalogue::getDelFlag, 1)
                .in(WeFormSurveyCatalogue::getId, ids));
    }

    @Override
    public void updateSurvey(WeAddFormSurveyCatalogueQuery query) {
        WeFormSurveyCatalogue catalogue = new WeFormSurveyCatalogue();
        BeanUtil.copyProperties(query, catalogue);
        update(catalogue, new LambdaQueryWrapper<WeFormSurveyCatalogue>().eq(WeFormSurveyCatalogue::getId, query.getId()));
    }

    @Override
    public WeFormSurveyCatalogue getInfo(Long id) {
        WeFormSurveyCatalogue weFormSurveyCatalogue = weFormSurveyCatalogueMapper.getWeFormSurveyCatalogueById(id);
        return weFormSurveyCatalogue;
    }

    @Override
    public List<WeFormSurveyCatalogue> getList(WeFormSurveyCatalogueQuery query) {
        LambdaQueryWrapper<WeFormSurveyCatalogue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(query.getId()), WeFormSurveyCatalogue::getId, query.getId());
        queryWrapper.eq(StringUtils.isNotBlank(query.getSurveyName()), WeFormSurveyCatalogue::getSurveyName, query.getSurveyName());
        queryWrapper.eq(Objects.nonNull(query.getSurveyState()), WeFormSurveyCatalogue::getSurveyState, query.getSurveyState());
        queryWrapper.eq(Objects.nonNull(query.getGroupId()), WeFormSurveyCatalogue::getGroupId, query.getGroupId());
        queryWrapper.apply(Objects.nonNull(query.getCreateTime()), "date_format(create_time,'%Y-%m-%d') = " + "'" + DateUtil.formatDate(query.getCreateTime()) + "'");
        queryWrapper.apply(Objects.nonNull(query.getStartDate()), "date_format(create_time,'%Y-%m-%d') >= " + "'" + DateUtil.formatDate(query.getStartDate()) + "'");
        queryWrapper.apply(Objects.nonNull(query.getEndDate()), "date_format(create_time,'%Y-%m-%d') <= " + "'" + DateUtil.formatDate(query.getEndDate()) + "'");
        queryWrapper.eq(WeFormSurveyCatalogue::getDelFlag, 0);
        queryWrapper.orderByDesc(WeFormSurveyCatalogue::getCreateTime);
        List<WeFormSurveyCatalogue> catalogueList = list(queryWrapper);
        if (CollectionUtil.isNotEmpty(catalogueList)) {
            List<Long> ids = catalogueList.parallelStream().map(WeFormSurveyCatalogue::getId).collect(Collectors.toList());
            List<WeFormSurveyStatistics> statistics = weFormSurveyStatisticsService.list(new LambdaQueryWrapper<WeFormSurveyStatistics>()
                    .in(WeFormSurveyStatistics::getBelongId, ids)
                    .eq(WeFormSurveyStatistics::getDelFlag, 0));
            for (WeFormSurveyCatalogue catalogue : catalogueList) {
                List<WeFormSurveyStatistics> tempStatistic = statistics.parallelStream()
                        .filter(statistic -> Objects.equals(catalogue.getId(), statistic.getBelongId())).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(tempStatistic)) {
                    catalogue.setTotalVisits(tempStatistic.get(0).getTotalVisits());
                    catalogue.setCollectionVolume(tempStatistic.get(0).getCollectionVolume());
                }
            }
        }
        return catalogueList;
    }

    @Override
    public void updateStatus(WeFormSurveyCatalogueQuery query) {
        if (Objects.isNull(query.getSurveyState())) {
            throw new WeComException("状态不能为空");
        }
        if (Objects.isNull(query.getId())) {
            throw new WeComException("ID不能为空");
        }
        update(new LambdaUpdateWrapper<WeFormSurveyCatalogue>()
                .set(WeFormSurveyCatalogue::getSurveyState, query.getSurveyState())
                .eq(WeFormSurveyCatalogue::getId, query.getId()));
    }

    @Override
    public void deleteFormSurveyGroup(Long groupId) {
        update(new LambdaUpdateWrapper<WeFormSurveyCatalogue>()
                .set(WeFormSurveyCatalogue::getGroupId, 1L)
                .eq(WeFormSurveyCatalogue::getGroupId, groupId)
                .eq(WeFormSurveyCatalogue::getDelFlag, 0));
    }

    @Override
    public WeFormSurveyCatalogue getWeFormSurveyCatalogueById(Long id) {
        WeFormSurveyCatalogue weFormSurveyCatalogue = weFormSurveyCatalogueMapper.getWeFormSurveyCatalogueById(id);
        return weFormSurveyCatalogue;
    }


    @Override
    public List<WeFormSurveyCatalogue> getListIgnoreTenantId() {
        return weFormSurveyCatalogueMapper.getListIgnoreTenantId();
    }


}
