package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.WeKfUserStat;
import com.linkwechat.domain.kf.query.WeKfQualityStatQuery;
import com.linkwechat.domain.kf.vo.WeKfQualityBrokenLineVo;
import com.linkwechat.domain.kf.vo.WeKfQualityChatVo;
import com.linkwechat.domain.kf.vo.WeKfQualityHistogramVo;

import java.util.List;

/**
 * 客服员工统计表(WeKfUserStat)
 *
 * @author danmo
 * @since 2022-11-28 16:48:24
 */
public interface IWeKfUserStatService extends IService<WeKfUserStat> {

    List<WeKfQualityBrokenLineVo> getQualityBrokenLine(WeKfQualityStatQuery query);

    List<WeKfQualityHistogramVo> getQualityHistogram(WeKfQualityStatQuery query);

    PageInfo<WeKfQualityChatVo> getQualityChart(WeKfQualityStatQuery query);
}
