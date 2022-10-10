package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeUserBehaviorData;
import com.linkwechat.domain.operation.query.WePageStateQuery;
import com.linkwechat.domain.operation.vo.WePageCountVo;
import com.linkwechat.mapper.WeUserBehaviorDataMapper;
import com.linkwechat.service.IWeUserBehaviorDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 联系客户统计数据 (WeUserBehaviorData)
 *
 * @author danmo
 * @since 2022-04-30 23:28:06
 */
@Service
public class WeUserBehaviorDataServiceImpl extends ServiceImpl<WeUserBehaviorDataMapper, WeUserBehaviorData> implements IWeUserBehaviorDataService {


    @Override
    public List<WePageCountVo> getDayCountDataByTime(String beginTime, String endTime) {
        WePageStateQuery query = new WePageStateQuery();
        query.setStartTime(beginTime);
        query.setEndTime(endTime);
        return this.baseMapper.getDayCountDataByTime(query);
    }
}
