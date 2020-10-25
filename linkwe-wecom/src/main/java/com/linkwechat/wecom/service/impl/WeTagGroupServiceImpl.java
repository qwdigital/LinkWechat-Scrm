package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.client.WeCropTagClient;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.domain.WeTagGroup;
import com.linkwechat.wecom.domain.dto.tag.*;
import com.linkwechat.wecom.mapper.WeTagGroupMapper;
import com.linkwechat.wecom.mapper.WeTagMapper;
import com.linkwechat.wecom.service.IWeTagGroupService;
import com.linkwechat.wecom.service.IWeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 标签组Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
@Service
public class WeTagGroupServiceImpl  extends ServiceImpl<WeTagGroupMapper,WeTagGroup> implements IWeTagGroupService
{
    @Autowired
    private WeTagGroupMapper weTagGroupMapper;


    @Autowired
    private WeCropTagClient weCropTagClient;

    @Autowired
    private IWeTagService iWeTagService;





    /**
     * 查询标签组列表
     * 
     * @param weTagGroup 标签组
     * @return 标签组
     */
    @Override
    public List<WeTagGroup> selectWeTagGroupList(WeTagGroup weTagGroup)
    {
        return weTagGroupMapper.selectWeTagGroupList(weTagGroup);
    }

    /**
     * 新增标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertWeTagGroup(WeTagGroup weTagGroup)
    {

        List<WeTag> weTags = weTagGroup.getWeTags();

        if(CollectionUtil.isEmpty(weTags)){
            this.save(weTagGroup);

        }else{

            WeCropGropTagDtlDto weCropGropTagDtlDto = weCropTagClient.addCorpTag(WeCropGroupTagDto.transformAddTag(weTagGroup));

            if(weCropGropTagDtlDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                this.batchSaveOrUpdateTagGroupAndTag(ListUtil.toList(weCropGropTagDtlDto.getTag_group()));
            }

        }



    }

    /**
     * 修改标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWeTagGroup(WeTagGroup weTagGroup)
    {

            List<WeTag> weTags = weTagGroup.getWeTags();
            //获取新增的集合
            if(CollectionUtil.isNotEmpty(weTags)) {
                List<WeTag> filterWeTags = weTags.stream().filter(v -> StringUtils.isEmpty(v.getTagId())).collect(Collectors.toList());
                //同步新增标签到微信端
                if(CollectionUtil.isNotEmpty(WeCropGroupTagDto.transformAddTag(weTagGroup).getTag())){
                    WeCropGropTagDtlDto
                            weCropGropTagDtlDto = weCropTagClient.addCorpTag( WeCropGroupTagDto.transformAddTag(weTagGroup));
                     if(weCropGropTagDtlDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                         //微信端返回的标签主键,设置到weTags中
                         Map<String, String> weCropTagMap = weCropGropTagDtlDto.getTag_group().getTag().stream()
                                 .collect(Collectors.toMap(weCropTagDto -> weCropTagDto.getName(), weCropTagDto -> weCropTagDto.getId()));
                         filterWeTags.stream().forEach(tag->{
                             tag.setTagId(weCropTagMap.get(tag.getName()));
                             tag.setCreateTime(new Date());
                         });
                     }
                }



                //获取需要删除的数据
                List<WeTag> removeWeTags = iWeTagService.list( new LambdaQueryWrapper<WeTag>().notIn(WeTag::getTagId,
                        weTags.stream().map(WeTag::getTagId).collect(Collectors.toList())).eq(WeTag::getGroupId,weTagGroup.getGroupId())
                .eq(WeTag::getStatus,Constants.NORMAL_CODE));

                if (CollectionUtil.isNotEmpty(removeWeTags)) {
                    //同步删除微信端的标签
                    weCropTagClient.delCorpTag(
                            WeCropDelDto.builder()
                                    .group_id(ArrayUtil.toArray(ListUtil.toList(weTagGroup.getGroupId()), String.class))
                                    .tag_id(ArrayUtil.toArray(removeWeTags.stream().map(WeTag::getTagId).collect(Collectors.toList()), String.class))
                                    .build());

                    //移除本地
                    removeWeTags.stream().forEach(v -> v.setStatus(Constants.DELETE_CODE));
                    iWeTagService.updateBatchById(removeWeTags);
                }


                //保存或更新wetag
                filterWeTags.stream().forEach(v->v.setGroupId(weTagGroup.getGroupId()));
                iWeTagService.saveOrUpdateBatch(filterWeTags);

            }

    }


    /**
     * 批量删除标签组
     * 
     * @param ids 需要删除的标签组ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteWeTagGroupByIds(String[] ids)
    {



        int returnCode = weTagGroupMapper.deleteWeTagGroupByIds(ids);

        if(returnCode> Constants.SERVICE_RETURN_SUCCESS_CODE){

            List<WeTag> weTags
                    = iWeTagService.list(new LambdaQueryWrapper<WeTag>().in(WeTag::getGroupId, ids));
            if(CollectionUtil.isNotEmpty(weTags)){
                weCropTagClient.delCorpTag(
                        WeCropDelDto.builder()
                                .group_id(ids)
                                .tag_id(weTags.stream().map(WeTag::getTagId).toArray(String[]::new))
                                .build()
                );
            }

        }

        return returnCode;
    }




    /**
     * 同步标签
     */
    @Override
    @Transactional
    public void synchWeTags() {


        WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getAllCorpTagList();

        if(weCropGroupTagListDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
            this.batchSaveOrUpdateTagGroupAndTag(weCropGroupTagListDto.getTag_group());
        }

    }


    /**
     * 来自微信端批量保存或者更新标签组和标签
     * @param tagGroup
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveOrUpdateTagGroupAndTag(List<WeCropGroupTagDto> tagGroup) {

        List<WeTagGroup> weTagGroups=new ArrayList<>();


        if(CollectionUtil.isNotEmpty(tagGroup)){
            tagGroup.stream().forEach(k->{
                WeTagGroup weTagGroup=new WeTagGroup();
                weTagGroup.setCreateBy(SecurityUtils.getUsername());
                weTagGroup.setGourpName(k.getGroup_name());
                weTagGroup.setGroupId(k.getGroup_id());
                List<WeCropTagDto> tag = k.getTag();
                if(CollectionUtil.isNotEmpty(tag)){
                    List<WeTag> weTags=new ArrayList<>();
                    tag.stream().forEach(v->{
                        WeTag weTag=new WeTag();
                        weTag.setTagId(v.getId());
                        weTag.setGroupId(weTagGroup.getGroupId());
                        weTag.setName(v.getName());
                        weTags.add(weTag);
                    });
                    weTagGroup.setWeTags(weTags);
                }
                weTagGroups.add(weTagGroup);
            });


        }

        this.saveOrUpdateBatch(weTagGroups);

        List<WeTag> weTags =
                weTagGroups.stream().map(WeTagGroup::getWeTags).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        if(CollectionUtil.isNotEmpty(weTags)){

            iWeTagService.saveOrUpdateBatch(weTags);

        }


    }




}
