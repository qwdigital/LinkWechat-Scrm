package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.enums.TrackState;
import com.linkwechat.common.enums.fieldtempl.CustomerPortraitFieldTempl;
import com.linkwechat.domain.WeStrackStage;
import com.linkwechat.mapper.WeStrackStageMapper;
import com.linkwechat.service.IWeStrackStageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class WeStrackStageServiceImpl extends ServiceImpl<WeStrackStageMapper, WeStrackStage> implements IWeStrackStageService {


    @Override
    public void initStrackStage() {

       if(count(new LambdaQueryWrapper<WeStrackStage>()
               .eq(WeStrackStage::getIsDefault,1))<=0){
           TrackState[] values = TrackState.values();
           List<WeStrackStage> weStrackStages=new ArrayList<>();
           for(TrackState value:values){
               weStrackStages.add(
                       WeStrackStage.builder()
                               .stageKey(value.getName())
                               .stageVal(value.getType())
                               .stageState(value.getStrackStage())
                               .stageDesc(value.getDesc())
                               .sort(value.getSort())
                               .isDefault(1)
                               .build()
               );
           }
           saveBatch(weStrackStages);
       }


    }
}
