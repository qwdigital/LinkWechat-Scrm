package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeShortLinkStat;
import com.linkwechat.domain.shortlink.query.WeShortLinkStatisticQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkStatisticsVo;
import com.linkwechat.mapper.WeShortLinkStatMapper;
import com.linkwechat.service.IWeShortLinkStatService;
import org.springframework.stereotype.Service;

/**
 * 短链统计表(WeShortLinkStat)
 *
 * @author danmo
 * @since 2023-01-10 23:04:10
 */
@Service
public class WeShortLinkStatServiceImpl extends ServiceImpl<WeShortLinkStatMapper, WeShortLinkStat> implements IWeShortLinkStatService {

    @Override
    public WeShortLinkStatisticsVo getDataStatistics(WeShortLinkStatisticQuery query) {
        return null;
    }

    @Override
    public void getLineStatistics(WeShortLinkStatisticQuery query) {

    }
}
