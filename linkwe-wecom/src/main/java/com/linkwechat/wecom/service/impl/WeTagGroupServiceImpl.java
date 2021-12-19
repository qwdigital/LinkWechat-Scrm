package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.wecom.annotation.SynchRecord;
import com.linkwechat.wecom.client.WeCropTagClient;
import com.linkwechat.wecom.constants.SynchRecordConstants;
import com.linkwechat.wecom.domain.WeTag;
import com.linkwechat.wecom.domain.WeTagGroup;
import com.linkwechat.wecom.domain.dto.tag.*;
import com.linkwechat.wecom.mapper.WeTagGroupMapper;
import com.linkwechat.wecom.service.IWeTagGroupService;
import com.linkwechat.wecom.service.IWeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标签组Service业务层处理
 *
 * @author ruoyi
 * @date 2020-09-07
 */
@Service
public class WeTagGroupServiceImpl extends ServiceImpl<WeTagGroupMapper, WeTagGroup> implements IWeTagGroupService {
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
    public List<WeTagGroup> selectWeTagGroupList(WeTagGroup weTagGroup) {
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
    public void insertWeTagGroup(WeTagGroup weTagGroup) {


            List<WeTag> weTags = weTagGroup.getWeTags();

            if(CollectionUtil.isNotEmpty(weTags)){


                //客户企业标签
                if(weTagGroup.getGroupTagType().equals(new Integer(1))){
                    weTagGroup.setAddWeTags(
                            weTags
                    );
                    WeCropGropTagDtlDto weCropGropTagDtlDto = weCropTagClient.addCorpTag(WeCropGroupTagDto.transformAddTag(weTagGroup));
                    if (weCropGropTagDtlDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
                        this.batchSaveOrUpdateTagGroupAndTag(ListUtil.toList(weCropGropTagDtlDto.getTag_group()), false);
                    }
                }else{//群标签或者个人标签
                    if(this.save(weTagGroup)) {
                        weTags.stream().forEach(k->{
                            k.setGroupId(weTagGroup.getGroupId());
                        });
                        iWeTagService.saveBatch(weTags);
                    }
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
    public void updateWeTagGroup(WeTagGroup weTagGroup) {

        List<WeTag> weTags = weTagGroup.getWeTags();

        List<WeTag> removeWeTags =new ArrayList<>();

        if(CollectionUtil.isNotEmpty(weTags)){
            //新增的标签
            List<WeTag> addWeTags = weTags.stream().filter(v -> StringUtils.isEmpty(v.getTagId())).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(addWeTags)){
                weTagGroup.setAddWeTags(addWeTags);

                  WeCropGropTagDtlDto
                        weCropGropTagDtlDto = weCropTagClient.addCorpTag(WeCropGroupTagDto.transformAddTag(weTagGroup));
                if (weCropGropTagDtlDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
                    //微信端返回的标签主键,设置到weTags中
                    Map<String, String> weCropTagMap = weCropGropTagDtlDto.getTag_group().getTag().stream()
                            .collect(Collectors.toMap(weCropTagDto -> weCropTagDto.getName(), weCropTagDto -> weCropTagDto.getId()));
                    addWeTags.stream().forEach(tag -> {
                        tag.setTagId(weCropTagMap.get(tag.getName()));
                    });

                    addWeTags.stream().forEach(v -> v.setGroupId(weTagGroup.getGroupId()));
                    iWeTagService.saveBatch(addWeTags);
                }

            }
            removeWeTags.addAll(
                    iWeTagService.list(new LambdaQueryWrapper<WeTag>().notIn(WeTag::getTagId,
                                    weTags.stream().map(WeTag::getTagId).collect(Collectors.toList()))
                            .eq(WeTag::getGroupId, weTagGroup.getGroupId()))
            );
        }else{//删除所有标签
            removeWeTags.addAll(
                    iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                            .eq(WeTag::getGroupId, weTagGroup.getGroupId()))
            );
        }

        if(CollectionUtil.isNotEmpty(removeWeTags)){
               //同步删除微信端的标签
            try {
                weCropTagClient.delCorpTag(
                        WeCropDelDto.builder()
                                .group_id(ArrayUtil.toArray(ListUtil.toList(weTagGroup.getGroupId()), String.class))
                                .tag_id(ArrayUtil.toArray(removeWeTags.stream().map(WeTag::getTagId).collect(Collectors.toList()), String.class))
                                .build());
            }catch (Exception e){
              log.error(e.getMessage());
            } finally {
                //移除本地
                iWeTagService.removeByIds(removeWeTags.stream().map(WeTag::getTagId).collect(Collectors.toList()));
            }
        }





        //获取新增的集合
//        if (CollectionUtil.isNotEmpty(weTags)) {
//            List<WeTag> filterWeTags = weTags.stream().filter(v -> StringUtils.isEmpty(v.getTagId())).collect(Collectors.toList());
//
//            //同步新增标签到微信端
//            if (CollectionUtil.isNotEmpty(WeCropGroupTagDto.transformAddTag(weTagGroup).getTag())) {
//                WeCropGropTagDtlDto
//                        weCropGropTagDtlDto = weCropTagClient.addCorpTag(WeCropGroupTagDto.transformAddTag(weTagGroup));
//                if (weCropGropTagDtlDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
//                    //微信端返回的标签主键,设置到weTags中
//                    Map<String, String> weCropTagMap = weCropGropTagDtlDto.getTag_group().getTag().stream()
//                            .collect(Collectors.toMap(weCropTagDto -> weCropTagDto.getName(), weCropTagDto -> weCropTagDto.getId()));
//                    filterWeTags.stream().forEach(tag -> {
//                        tag.setTagId(weCropTagMap.get(tag.getName()));
//                    });
//                }
//            }
//
//
//            //获取需要删除的数据
//            List<WeTag> removeWeTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>().notIn(WeTag::getTagId,
//                    weTags.stream().map(WeTag::getTagId).collect(Collectors.toList())).eq(WeTag::getGroupId, weTagGroup.getGroupId()));
//
//            if (CollectionUtil.isNotEmpty(removeWeTags)) {
//                //同步删除微信端的标签
//                weCropTagClient.delCorpTag(
//                        WeCropDelDto.builder()
//                                .group_id(ArrayUtil.toArray(ListUtil.toList(weTagGroup.getGroupId()), String.class))
//                                .tag_id(ArrayUtil.toArray(removeWeTags.stream().map(WeTag::getTagId).collect(Collectors.toList()), String.class))
//                                .build());
//
//                //移除本地
//                iWeTagService.removeByIds(removeWeTags.stream().map(WeTag::getTagId).collect(Collectors.toList()));
//            }
//
//
//            //保存或更新wetag
//            filterWeTags.stream().forEach(v -> v.setGroupId(weTagGroup.getGroupId()));
//            iWeTagService.saveOrUpdateBatch(filterWeTags);
//
//        }

    }


    /**
     * 批量删除标签组
     *
     * @param ids 需要删除的标签组ID
     * @return 结果
     */
    @Override
    @Transactional
    public void deleteWeTagGroupByIds(String[] ids) {

        if (this.removeByIds(CollectionUtil.toList(ids))) {
            List<WeTag> weTags
                    = iWeTagService.list(new LambdaQueryWrapper<WeTag>().in(WeTag::getGroupId, ids));
            if (CollectionUtil.isNotEmpty(weTags)) {
                try {
                    weCropTagClient.delCorpTag(
                            WeCropDelDto.builder()
                                    .group_id(ids)
                                    .tag_id(weTags.stream().map(WeTag::getTagId).toArray(String[]::new))
                                    .build()
                    );
                }catch (Exception e){//防止数据不同步产生的错误
                    log.error(e.getMessage());
                }

            }

        }
    }


    /**
     * 同步标签
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER_TAG)
    public void synchWeTags() {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        //异步同步一下标签库,解决标签不同步问题
        Threads.SINGLE_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                SecurityContextHolder.setContext(securityContext);
                WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getAllCorpTagList();

                if (weCropGroupTagListDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
                   batchSaveOrUpdateTagGroupAndTag(weCropGroupTagListDto.getTag_group(), true);
                }

            }
        });


    }




    /**
     * 来自微信端批量保存或者更新标签组和标签
     *
     * @param tagGroup
     * @param isSync   是否同步
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveOrUpdateTagGroupAndTag(List<WeCropGroupTagDto> tagGroup, Boolean isSync) {

        List<WeTagGroup> weTagGroups = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(tagGroup)) {
            tagGroup.stream().forEach(k -> {
                WeTagGroup weTagGroup = new WeTagGroup();
                weTagGroup.setGourpName(k.getGroup_name());
                weTagGroup.setGroupId(k.getGroup_id());
                List<WeCropTagDto> tag = k.getTag();
                if (CollectionUtil.isNotEmpty(tag)) {
                    List<WeTag> weTags = new ArrayList<>();
                    tag.stream().forEach(v -> {
                        WeTag weTag = new WeTag();
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


        //先逻辑删除不存在得组
        if (CollectionUtil.isNotEmpty(weTagGroups)) {

            if (isSync) {
                List<WeTagGroup> noExist
                        = this.list(new LambdaQueryWrapper<WeTagGroup>()
                                .eq(WeTagGroup::getGroupTagType,new Integer(1))
                        .notIn(WeTagGroup::getGroupId, weTagGroups.stream().map(WeTagGroup::getGroupId).collect(Collectors.toList())));
                //企业微信端删除得标签
                if (CollectionUtil.isNotEmpty(noExist)) {
                    this.removeByIds(noExist.stream().map(WeTagGroup::getGroupId).collect(Collectors.toList()));
                }
            }
            this.saveOrUpdateBatch(weTagGroups);



            List<WeTag> weTags =
                    weTagGroups.stream().map(WeTagGroup::getWeTags).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);

            if (CollectionUtil.isNotEmpty(weTags)) {

                if (isSync) {

                    List<WeTag> noExistWeTags
                            = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                                    .eq(WeTag::getTagType,new Integer(1))
                            .notIn(WeTag::getTagId, weTags.stream().map(WeTag::getTagId).collect(Collectors.toList())));
                    if (CollectionUtil.isNotEmpty(noExistWeTags)) {
                        iWeTagService.removeByIds(
                                noExistWeTags.stream().map(WeTag::getTagId).collect(Collectors.toList())
                        );
                    }

                }


                iWeTagService.saveOrUpdateBatch(weTags);

            }


        } else {//不存在删除所有标签组,以标签

            if (isSync) {
                List<WeTagGroup> weTagGroupList
                        = this.list();

                if (CollectionUtil.isNotEmpty(weTagGroupList)) {

                    this.removeByIds(weTagGroupList.stream().map(WeTagGroup::getGroupId).collect(Collectors.toList()));
                }
                List<WeTag> weTags
                        = iWeTagService.list();
                if (CollectionUtil.isNotEmpty(weTags)) {
                    iWeTagService.removeByIds(weTags.stream().map(WeTag::getTagId).collect(Collectors.toList()));
                }
            }

        }
    }

    @Override
    public void createTagGroup(String id) {
        List<String> list = new ArrayList<>();
        list.add(id);
        WeFindCropTagParam build = WeFindCropTagParam.builder().group_id(list).build();
        WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getCorpTagListByTagIds(build);
        List<WeCropGroupTagDto> tag_group = weCropGroupTagListDto.getTag_group();
        if (CollectionUtil.isNotEmpty(tag_group)) {
            List<WeTagGroup> tagGroupsList = new ArrayList<>();
            tag_group.stream().forEach(k -> {
                WeTagGroup tagGroupInfo = this.baseMapper.selectOne(new LambdaQueryWrapper<WeTagGroup>()
                        .eq(WeTagGroup::getGroupId, k.getGroup_id()));
                if (tagGroupInfo == null) {
                    WeTagGroup weTagGroup = new WeTagGroup();
                    weTagGroup.setGourpName(k.getGroup_name());
                    weTagGroup.setGroupId(k.getGroup_id());
                    tagGroupsList.add(weTagGroup);
                }
            });
            this.saveBatch(tagGroupsList);
        }
    }

    @Override
    public void deleteTagGroup(String id) {
        this.baseMapper.deleteWeTagGroupByIds(id.split(","));
    }

    @Override
    public void updateTagGroup(String id) {
        List<String> list = new ArrayList<>();
        list.add(id);
        WeFindCropTagParam build = WeFindCropTagParam.builder().group_id(list).build();
        WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getCorpTagListByTagIds(build);
        List<WeCropGroupTagDto> tag_group = weCropGroupTagListDto.getTag_group();
        if (CollectionUtil.isNotEmpty(tag_group)) {
            List<WeTagGroup> tagGroupsList = new ArrayList<>();
            WeTagGroup weTagGroup = new WeTagGroup();
            tag_group.stream().forEach(k -> {
                weTagGroup.setGourpName(k.getGroup_name());
                weTagGroup.setGroupId(k.getGroup_id());
                tagGroupsList.add(weTagGroup);
            });
            this.saveOrUpdateBatch(tagGroupsList);
        }
    }

//    @Override
//    public List<WeTagGroup> findCustomerTagByFlowerCustomerRelId(String externalUserid, String userid) {
//        return this.baseMapper.findCustomerTagByFlowerCustomerRelId(externalUserid, userid);
//    }


}
