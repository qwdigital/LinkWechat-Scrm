package com.linkwechat.typeHandler;

import com.alibaba.fastjson.TypeReference;
import com.linkwechat.common.typeHandler.ListTypeHandler;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;

import java.util.List;

public class WeStrategicCrowdSwipeListTypeHandler extends ListTypeHandler<WeStrategicCrowdSwipe> {

    @Override
    protected TypeReference<List<WeStrategicCrowdSwipe>> specificType() {
        return new TypeReference<List<WeStrategicCrowdSwipe>>() {
        };
    }


}
