package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeShortLinkStat;
import com.linkwechat.domain.shortlink.query.WeShortLinkStatisticQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkStatisticsVo;

/**
 * 短链统计表(WeShortLinkStat)
 *
 * @author danmo
 * @since 2023-01-10 23:04:09
 */
public interface IWeShortLinkStatService extends IService<WeShortLinkStat> {

    WeShortLinkStatisticsVo getDataStatistics(WeShortLinkStatisticQuery query);

    WeShortLinkStatisticsVo getLineStatistics(WeShortLinkStatisticQuery query);
}
