package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeGroupTagRel;
import com.linkwechat.wecom.domain.vo.WeMakeGroupTagVo;
import com.linkwechat.wecom.mapper.WeGroupTagRelMapper;
import com.linkwechat.wecom.service.IWeGroupTagRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WeGroupTagRelServiceImpl extends ServiceImpl<WeGroupTagRelMapper,WeGroupTagRel> implements IWeGroupTagRelService {


    @Override
    @Transactional
    public void makeGroupTag(WeMakeGroupTagVo weMakeGroupTagVo) {

        List<WeGroupTagRel> addWeGroupTagRel = weMakeGroupTagVo.getWeeGroupTagRel();
        if(CollectionUtil.isNotEmpty(addWeGroupTagRel)){
            addWeGroupTagRel.stream().forEach(k->k.setChatId(weMakeGroupTagVo.getChatId()));
            this.remove(new LambdaQueryWrapper<WeGroupTagRel>()
                    .eq(WeGroupTagRel::getChatId,weMakeGroupTagVo.getChatId()));

            this.saveBatch(addWeGroupTagRel);
        }else{//移除所有标签
            this.remove(new LambdaQueryWrapper<WeGroupTagRel>()
                    .eq(WeGroupTagRel::getChatId,weMakeGroupTagVo.getChatId()));
        }

    }

}
