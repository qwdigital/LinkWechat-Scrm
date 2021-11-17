package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WePageStatistics;
import com.linkwechat.wecom.domain.dto.WePageCountDto;
import com.linkwechat.wecom.domain.query.WePageStateQuery;
import com.linkwechat.wecom.mapper.WePageStatisticsMapper;
import com.linkwechat.wecom.service.IWePageStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页数据统计表(WePageStatistics)
 *
 * @author danmo
 * @since 2021-11-16 16:20:32
 */
@Service
public class WePageStatisticsServiceImpl extends ServiceImpl<WePageStatisticsMapper, WePageStatistics> implements IWePageStatisticsService {

    @Override
    public List<WePageCountDto> getDayCountData(WePageStateQuery wePageStateQuery) {
        return this.baseMapper.getDayCountData(wePageStateQuery);
    }

    @Override
    public List<WePageCountDto> getWeekCountData(WePageStateQuery wePageStateQuery) {
        return this.baseMapper.getWeekCountData(wePageStateQuery);
    }

    @Override
    public List<WePageCountDto> getMonthCountData(WePageStateQuery wePageStateQuery) {
        return this.baseMapper.getMonthCountData(wePageStateQuery);
    }
}
