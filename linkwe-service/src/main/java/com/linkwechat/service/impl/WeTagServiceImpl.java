package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.WeTagGroup;
import com.linkwechat.domain.wecom.entity.customer.tag.WeCorpTagEntity;
import com.linkwechat.domain.wecom.entity.customer.tag.WeCorpTagGroupEntity;
import com.linkwechat.domain.wecom.query.customer.tag.WeAddCorpTagQuery;
import com.linkwechat.domain.wecom.query.customer.tag.WeCorpTagListQuery;
import com.linkwechat.domain.wecom.vo.customer.tag.WeCorpTagVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeTagMapper;
import com.linkwechat.service.IWeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeTagServiceImpl extends ServiceImpl<WeTagMapper, WeTag> implements IWeTagService {



    @Autowired
    private QwCustomerClient qwCustomerClient;



    @Override
    public void addWxTag(WeTagGroup weTagGroup,List<WeTag> weTags) throws WeComException{
        List<WeCorpTagEntity> weCorpTagEntities=new ArrayList<>();

        weTags.stream().forEach(k->{
            weCorpTagEntities.add(
                    WeCorpTagEntity.builder()
                            .name(k.getName())
                            .build()
            );
        });

        WeCorpTagVo weCorpTagVo  = qwCustomerClient.addCorpTag(
                WeAddCorpTagQuery.builder()
                        .group_id(weTagGroup.getGroupId())
                        .group_name(weTagGroup.getGroupName())
                        .tag(weCorpTagEntities)
                        .build()
        ).getData();

        if(weCorpTagVo.getTagGroup()==null){
            throw new WeComException("标签同步企业微信失败");
        }

        WeCorpTagGroupEntity tagGroup = weCorpTagVo.getTagGroup();
        if(null != tagGroup){
            if(StringUtils.isNotEmpty(tagGroup.getGroup_id())
                    && CollectionUtil.isNotEmpty(tagGroup.getTag())){//设置tag_id与group_id
                weTagGroup.setGroupId(tagGroup.getGroup_id());
                weTags.stream().forEach(k->{
                    k.setTagId(
                            tagGroup.getTag().stream().filter(a->a.getName().equals(k.getName())).collect(Collectors.toList()).stream().findFirst().get().getId()
                    );
                });

            }
        }

    }

    @Override
    public void removeWxTag(String groupId, List<WeTag> removeWeTags,boolean removeGroup) {
        //同步删除微信端的标签
        try {
            WeCorpTagListQuery tagListQuery = WeCorpTagListQuery.builder().build();
            if (removeGroup) {
                tagListQuery.setGroup_id(ListUtil.toList(groupId));
            } else {
                tagListQuery.setTag_id(
                        removeWeTags.stream().map(WeTag::getTagId).collect(Collectors.toList()
                        ));
            }


            qwCustomerClient.delCorpTag(
                    tagListQuery
                    );
        }catch (Exception e){
            log.error(e.getMessage());
        } finally {
            //移除本地
            if(CollectionUtil.isNotEmpty(removeWeTags)){
                this.remove(new LambdaQueryWrapper<WeTag>()
                        .in(WeTag::getTagId,removeWeTags.stream().map(WeTag::getTagId).collect(Collectors.toList())));
            }

        }
    }

    @Override
    public void batchAddOrUpdate(List<WeTag> weTags) {
        this.baseMapper.batchAddOrUpdate(weTags);
    }

    @Override
    public List<WeTag> getTagListByGroupIds(List<String> groupIds) {
        return list(new LambdaQueryWrapper<WeTag>().in(WeTag::getGroupId,groupIds).eq(WeTag::getDelFlag,0));
    }


}
