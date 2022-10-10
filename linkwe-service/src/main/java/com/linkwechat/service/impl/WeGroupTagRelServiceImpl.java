package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeGroupTagRel;
import com.linkwechat.domain.groupchat.query.WeMakeGroupTagQuery;
import com.linkwechat.mapper.WeGroupTagRelMapper;
import com.linkwechat.service.IWeGroupTagRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群标签关系(WeGroupTagRel)
 *
 * @author danmo
 * @since 2022-04-06 11:09:57
 */
@Service
public class WeGroupTagRelServiceImpl extends ServiceImpl<WeGroupTagRelMapper, WeGroupTagRel> implements IWeGroupTagRelService {


    @Transactional
    @Override
    public void makeGroupTag(WeMakeGroupTagQuery query) {

        this.remove(new LambdaQueryWrapper<WeGroupTagRel>()
                .eq(WeGroupTagRel::getChatId,query.getChatId()));

         if(CollectionUtil.isNotEmpty(query.getTagIds())){
             List<WeGroupTagRel> tagRels=new ArrayList<>();
             query.getTagIds().stream().forEach(k->{
                 tagRels.add(
                         WeGroupTagRel.builder()
                                 .chatId(query.getChatId())
                                 .tagId(k)
                                 .build()
                 );
             });
             if(CollectionUtil.isNotEmpty(tagRels)){
                 this.saveBatch(tagRels);
             }
         }
    }
}
