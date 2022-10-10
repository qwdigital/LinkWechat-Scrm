package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.groupcode.entity.WeGroupCodeTagRel;
import com.linkwechat.domain.groupcode.query.WeMakeGroupCodeTagQuery;
import com.linkwechat.mapper.WeGroupCodeTagRelMapper;
import com.linkwechat.service.IWeGroupCodeTagRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeGroupCodeTagRelServiceImpl extends ServiceImpl<WeGroupCodeTagRelMapper, WeGroupCodeTagRel> implements IWeGroupCodeTagRelService {


    @Override
    @Transactional
    public void makeGroupCodeTag(WeMakeGroupCodeTagQuery query) {

        this.remove(new LambdaQueryWrapper<WeGroupCodeTagRel>()
                .eq(WeGroupCodeTagRel::getGroupCodeId,query.getGroupCodeId()));

        List<String> tagIds = query.getTagIds();
        if(CollectionUtil.isNotEmpty(tagIds)){
            List<WeGroupCodeTagRel> groupCodeTagRels=new ArrayList<>();
            tagIds.stream().forEach(tagId->{
                groupCodeTagRels.add(
                        WeGroupCodeTagRel.builder()
                                .groupCodeId(query.getGroupCodeId())
                                .tagId(tagId)
                                .build()
                );
            });
            if(CollectionUtil.isNotEmpty(groupCodeTagRels)){
                this.saveBatch(groupCodeTagRels);
            }
        }


//
//        List<String> addTagIds = new LinkedList<>();
//        List<String> delTagIds = new LinkedList<>();
//        LambdaQueryWrapper<WeGroupCodeTagRel> wrapper = new LambdaQueryWrapper<WeGroupCodeTagRel>()
//                .eq(WeGroupCodeTagRel::getGroupCodeId, query.getGroupCodeId())
//                .eq(WeGroupCodeTagRel::getDelFlag, 0);
//        List<String> tagIds = query.getTagIds();
//        if (CollectionUtil.isNotEmpty(tagIds)) {
//            wrapper.eq(WeGroupCodeTagRel::getTagId, query.getTagIds());
//            List<WeGroupCodeTagRel> list = list(wrapper);
//            if (CollectionUtil.isNotEmpty(list)) {
//                List<String> tempAddTagIds = list.stream().filter(tag -> tagIds.stream().noneMatch(tagId -> ObjectUtil.equal(tagId, tag.getTagId())))
//                        .map(WeGroupCodeTagRel::getTagId).collect(Collectors.toList());
//                addTagIds.addAll(tempAddTagIds);
//                List<String> tempDelTagIds = tagIds.stream().filter(tagId -> list.stream().noneMatch(tag -> ObjectUtil.equal(tagId, tag.getTagId())))
//                        .collect(Collectors.toList());
//                delTagIds.addAll(tempDelTagIds);
//            } else {
//                addTagIds.addAll(tagIds);
//            }
//        } else {
//            WeGroupCodeTagRel weGroupTagRel = new WeGroupCodeTagRel();
//            weGroupTagRel.setDelFlag(1);
//            update(weGroupTagRel, wrapper);
//        }
//
//        if(CollectionUtil.isNotEmpty(addTagIds)){
//            List<WeGroupCodeTagRel> addTagRels = addTagIds.stream().map(tagId -> {
//                WeGroupCodeTagRel weGroupTagRel = new WeGroupCodeTagRel();
//                weGroupTagRel.setGroupCodeId(query.getGroupCodeId());
//                weGroupTagRel.setTagId(tagId);
//                return weGroupTagRel;
//            }).collect(Collectors.toList());
//            saveBatch(addTagRels);
//        }
//
//        if(CollectionUtil.isNotEmpty(delTagIds)){
//            WeGroupCodeTagRel weGroupTagRel = new WeGroupCodeTagRel();
//            weGroupTagRel.setDelFlag(1);
//            weGroupTagRel.setGroupCodeId(query.getGroupCodeId());
//            wrapper.in(WeGroupCodeTagRel::getTagId,delTagIds);
//            update(weGroupTagRel, wrapper);
//        }

    }
}
