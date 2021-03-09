package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.client.WeCropTagClient;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.AllocateWeCustomerDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.dto.customer.*;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagDto;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagListDto;
import com.linkwechat.wecom.domain.dto.tag.WeCropTagDto;
import com.linkwechat.wecom.domain.dto.tag.WeFindCropTagParam;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 企业微信客户Service业务层处理
 *
 * @author ruoyi
 * @date 2020-09-13
 */
@Service
public class WeCustomerServiceImpl extends ServiceImpl<WeCustomerMapper, WeCustomer> implements IWeCustomerService {
    @Autowired
    private WeCustomerMapper weCustomerMapper;


    @Autowired
    private WeCustomerClient weCustomerClient;


    @Autowired
    private IWeFlowerCustomerRelService iWeFlowerCustomerRelService;

    @Autowired
    private WeCropTagClient weCropTagClient;

    @Autowired
    private IWeTagService iWeTagService;


    @Autowired
    private IWeTagGroupService iWeTagGroupService;


    @Autowired
    private IWeFlowerCustomerTagRelService iWeFlowerCustomerTagRelService;


    @Autowired
    private IWeAllocateCustomerService iWeAllocateCustomerService;


    @Autowired
    private WeUserClient weUserClient;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(WeCustomer weCustomer) {
        if (weCustomer.getExternalUserid() != null) {
            WeCustomer weCustomerBean = selectWeCustomerById(weCustomer.getExternalUserid());
            if (weCustomerBean != null) {
                return weCustomerMapper.updateWeCustomer(weCustomer) == 1;
            } else {
                return weCustomerMapper.insertWeCustomer(weCustomer) == 1;
            }
        }
        return false;
    }

    /**
     * 查询企业微信客户
     *
     * @param externalUserId 企业微信客户ID
     * @return 企业微信客户
     */
    @Override
    public WeCustomer selectWeCustomerById(String externalUserId) {
        return weCustomerMapper.selectWeCustomerById(externalUserId);
    }

    /**
     * 查询企业微信客户列表
     *
     * @param weCustomer 企业微信客户
     * @return 企业微信客户
     */
    @Override
    public List<WeCustomer> selectWeCustomerList(WeCustomer weCustomer) {
        return weCustomerMapper.selectWeCustomerList(weCustomer);
    }


    /**
     * 客户同步接口
     *
     * @return
     */
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void synchWeCustomer() {
        FollowUserList followUserList = weCustomerClient.getFollowUserList();
        if (WeConstans.WE_SUCCESS_CODE.equals(followUserList.getErrcode())
                && ArrayUtil.isNotEmpty(followUserList.getFollow_user())) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Arrays.asList(followUserList.getFollow_user())
                    .parallelStream().forEach(k -> {
                try {
                    SecurityContextHolder.setContext(securityContext);
                    weFlowerCustomerHandle(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 客户同步业务处理
     *
     * @param userId 开通权限的企业成员id
     */
    private void weFlowerCustomerHandle(String userId) {
        List<ExternalUserDetail> list = new ArrayList<>();
        getByUser(userId, null, list);
        list.forEach(userDetail -> {
            //客户入库
            WeCustomer weCustomer = new WeCustomer();
            BeanUtils.copyPropertiesignoreOther(userDetail.getExternal_contact(), weCustomer);
            this.saveOrUpdate(weCustomer);

            //客户与通讯录客户关系
            List<WeTag> weTags = new ArrayList<>();
            List<WeTagGroup> weGroups = new ArrayList<>();
            List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
            List<WeFlowerCustomerRel> weFlowerCustomerRel = new ArrayList<>();
            Optional.ofNullable(userDetail.getFollow_info()).ifPresent(followInfo -> {
                Long weFlowerCustomerRelId = SnowFlakeUtil.nextId();
                weFlowerCustomerRel.add(WeFlowerCustomerRel.builder()
                        .id(weFlowerCustomerRelId)
                        .userId(userId)
                        .description(followInfo.getDescription())
                        .remarkCorpName(followInfo.getRemark_company())
                        .remarkMobiles(Arrays.stream(followInfo.getRemark_mobiles()).collect(Collectors.joining(",")))
                        .operUserid(followInfo.getOper_userid())
                        .addWay(followInfo.getAdd_way())
                        .externalUserid(weCustomer.getExternalUserid())
                        .createTime(new Date(followInfo.getCreatetime() * 1000L))
                        .build());

                List<String> tags = Stream.of(followInfo.getTag_id()).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(tags)) {
                    //获取相关标签组
                    WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getCorpTagListByTagIds(WeFindCropTagParam.builder()
                            .tag_id(followInfo.getTag_id())
                            .build());

                    if (weCropGroupTagListDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
                        List<WeCropGroupTagDto> tagGroups = weCropGroupTagListDto.getTag_group();
                        if (CollectionUtil.isNotEmpty(tagGroups)) {
                            tagGroups.stream().forEach(tagGroup -> {
                                weGroups.add(
                                        WeTagGroup.builder()
                                                .groupId(tagGroup.getGroup_id())
                                                .gourpName(tagGroup.getGroup_name())
                                                .createBy(SecurityUtils.getUsername())
                                                .build()
                                );

                                List<WeCropTagDto> weCropTagDtos = tagGroup.getTag();
                                if (CollectionUtil.isNotEmpty(weCropTagDtos)) {
                                    Map<String, String> map = weCropTagDtos.stream().collect(Collectors.toMap(WeCropTagDto::getId, WeCropTagDto::getName));
                                    tags.forEach(tag -> {
                                        if (map.containsKey(tag)) {
                                            weTags.add(
                                                    WeTag.builder()
                                                            .groupId(tagGroup.getGroup_id())
                                                            .tagId(tag)
                                                            .name(map.get(tag))
                                                            .build()
                                            );

                                            weFlowerCustomerTagRels.add(
                                                    WeFlowerCustomerTagRel.builder()
                                                            .flowerCustomerRelId(weFlowerCustomerRelId)
                                                            .tagId(tag)
                                                            .createTime(new Date())
                                                            .build()
                                            );
                                        }

                                    });
                                }

                            });


                        }
                    }
                }
            });

            List<WeFlowerCustomerRel> weFlowerCustomerRels = iWeFlowerCustomerRelService.list(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                    .eq(WeFlowerCustomerRel::getExternalUserid, weCustomer.getExternalUserid()));

            if (CollectionUtil.isNotEmpty(weFlowerCustomerRels)) {
                List<Long> weFlowerCustomerRelIds = weFlowerCustomerRels.stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList());
                iWeFlowerCustomerTagRelService.remove(
                        new LambdaQueryWrapper<WeFlowerCustomerTagRel>().in(WeFlowerCustomerTagRel::getFlowerCustomerRelId,
                                weFlowerCustomerRelIds)
                );

                iWeFlowerCustomerRelService.removeByIds(
                        weFlowerCustomerRelIds
                );
            }


            iWeFlowerCustomerRelService.saveBatch(weFlowerCustomerRel);

            //设置标签跟客户关系，标签和标签组,saveOrUpdate，建立标签与添加人关系
            if (CollectionUtil.isNotEmpty(weTags) && CollectionUtil.isNotEmpty(weGroups)) {
                iWeTagService.saveOrUpdateBatch(weTags);
                iWeTagGroupService.saveOrUpdateBatch(weGroups);
                iWeFlowerCustomerTagRelService.saveOrUpdateBatch(weFlowerCustomerTagRels);
            }
        });
    }

    /**
     * 批量获取客户详情
     *
     * @param userId     企业成员的userid
     * @param nextCursor 用于分页查询的游标
     * @param list       返回结果
     */
    private void getByUser(String userId, String nextCursor, List<ExternalUserDetail> list) {
        Map<String, Object> query = new HashMap<>(16);
        query.put(WeConstans.USER_ID, userId);
        query.put(WeConstans.CURSOR, nextCursor);
        ExternalUserList externalUserList = weCustomerClient.getByUser(query);
        if (WeConstans.WE_SUCCESS_CODE.equals(externalUserList.getErrcode())
                || WeConstans.NOT_EXIST_CONTACT.equals(externalUserList.getErrcode())
                && ArrayUtil.isNotEmpty(externalUserList.getExternal_contact_list())) {
            list.addAll(externalUserList.getExternal_contact_list());
            if (StringUtils.isNotEmpty(externalUserList.getNext_cursor())) {
                getByUser(userId, externalUserList.getNext_cursor(), list);
            }
        }
    }


    /**
     * 分配离职员工客户
     *
     * @param weLeaveUserInfoAllocateVo
     */
    @Override
    @Transactional
    public void allocateWeCustomer(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {

        List<WeFlowerCustomerRel> weFlowerCustomerRels = iWeFlowerCustomerRelService.list(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                .eq(WeFlowerCustomerRel::getUserId, weLeaveUserInfoAllocateVo.getHandoverUserid()));

        if (CollectionUtil.isNotEmpty(weFlowerCustomerRels)) {


            List<WeAllocateCustomer> weAllocateCustomers = new ArrayList<>();
            weFlowerCustomerRels.stream().forEach(k -> {
                k.setUserId(weLeaveUserInfoAllocateVo.getTakeoverUserid());
                weAllocateCustomers.add(
                        WeAllocateCustomer.builder()
                                .allocateTime(new Date())
                                .externalUserid(k.getExternalUserid())
                                .handoverUserid(weLeaveUserInfoAllocateVo.getHandoverUserid())
                                .takeoverUserid(weLeaveUserInfoAllocateVo.getTakeoverUserid())
                                .build()
                );
            });

            //更新当前接手用户的id
            iWeFlowerCustomerRelService.saveOrUpdateBatch(weFlowerCustomerRels);


            if (CollectionUtil.isNotEmpty(weAllocateCustomers)) {

                //记录分配历史
                if (iWeAllocateCustomerService.saveBatch(weAllocateCustomers)) {
                    //同步企业微信端
                    weAllocateCustomers.stream().forEach(v -> {
                        weUserClient.allocateCustomer(AllocateWeCustomerDto.builder()
                                .external_userid(v.getExternalUserid())
                                .handover_userid(v.getHandoverUserid())
                                .takeover_userid(v.getTakeoverUserid())
                                .build());
                    });

                }

            }


        }

    }


    /**
     * 客户打标签
     *
     * @param weMakeCustomerTag
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeLabel(WeMakeCustomerTag weMakeCustomerTag) {

        //查询出当前用户对应的
        List<WeFlowerCustomerRel> flowerCustomerRels = iWeFlowerCustomerRelService.list(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                .eq(WeFlowerCustomerRel::getExternalUserid, weMakeCustomerTag.getExternalUserid()));
        if (CollectionUtil.isNotEmpty(flowerCustomerRels)) {


            List<WeTag> addTags = weMakeCustomerTag.getAddTag();


            if (CollectionUtil.isNotEmpty(addTags)) {
                addTags.removeAll(Collections.singleton(null));
                List<WeFlowerCustomerTagRel> tagRels = new ArrayList<>();

                List<CutomerTagEdit> cutomerTagEdits = new ArrayList<>();
                flowerCustomerRels.stream().forEach(customer -> {
                    CutomerTagEdit cutomerTagEdit = CutomerTagEdit.builder()
                            .userid(customer.getUserId())
                            .external_userid(customer.getExternalUserid())
                            .build();
                    List<String> tags = new ArrayList<>();
                    addTags.stream().forEach(tag -> {
                        tags.add(tag.getTagId());
                        tagRels.add(
                                WeFlowerCustomerTagRel.builder()
                                        .flowerCustomerRelId(customer.getId())
                                        .tagId(tag.getTagId())
                                        .createTime(new Date())
                                        .build()
                        );
                    });

                    cutomerTagEdit.setAdd_tag(ArrayUtil.toArray(tags, String.class));
                    cutomerTagEdits.add(cutomerTagEdit);
                });

                if (iWeFlowerCustomerTagRelService.saveOrUpdateBatch(tagRels)) {
                    if (CollectionUtil.isNotEmpty(cutomerTagEdits)) {
                        cutomerTagEdits.stream().forEach(k -> {
                            weCustomerClient.makeCustomerLabel(
                                    k
                            );
                        });
                    }
                }
            }
        }


    }


    /**
     * 移除客户标签
     *
     * @param weMakeCustomerTag
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeLabel(WeMakeCustomerTag weMakeCustomerTag) {

        List<WeTag> addTags = weMakeCustomerTag.getAddTag();

        if (CollectionUtil.isNotEmpty(addTags)) {



            //查询出当前用户对应的
            List<WeFlowerCustomerRel> flowerCustomerRels = iWeFlowerCustomerRelService.list(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                    .eq(WeFlowerCustomerRel::getExternalUserid, weMakeCustomerTag.getExternalUserid()));



            if (CollectionUtil.isNotEmpty(flowerCustomerRels)) {

                List<WeFlowerCustomerTagRel> removeTag = iWeFlowerCustomerTagRelService.list(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                        .in(WeFlowerCustomerTagRel::getFlowerCustomerRelId,flowerCustomerRels.stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList()))
                        .in(WeFlowerCustomerTagRel::getTagId, addTags.stream().map(WeTag::getTagId).collect(Collectors.toList())));

                if(CollectionUtil.isNotEmpty(removeTag)){
                    if (iWeFlowerCustomerTagRelService.remove(
                            new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                                    .in(WeFlowerCustomerTagRel::getFlowerCustomerRelId, flowerCustomerRels.stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList()))
                                    .in(WeFlowerCustomerTagRel::getTagId, removeTag.stream().map(WeFlowerCustomerTagRel::getTagId).collect(Collectors.toList()))
                    )) {

                        flowerCustomerRels.stream().forEach(k -> {
                            weCustomerClient.makeCustomerLabel(
                                    CutomerTagEdit.builder()
                                            .external_userid(k.getExternalUserid())
                                            .userid(k.getUserId())
                                            .remove_tag(ArrayUtil.toArray(removeTag.stream().map(WeFlowerCustomerTagRel::getTagId).collect(Collectors.toList()), String.class))
                                            .build()
                            );

                        });

                    }
                }








            }


        }


    }


    /**
     * 根据员工ID获取客户
     *
     * @param externalUserid
     * @return
     */
    @Override
    public List<WeUser> getCustomersByUserId(String externalUserid) {
        return this.baseMapper.getCustomersByUserId(externalUserid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getCustomersInfoAndSynchWeCustomer(String externalUserid) {

        //获取指定客户的详情
        ExternalUserDetail externalUserDetail = weCustomerClient.get(externalUserid);

        if (WeConstans.WE_SUCCESS_CODE.equals(externalUserDetail.getErrcode())) {
            //客户入库
            WeCustomer weCustomer = new WeCustomer();
            BeanUtils.copyPropertiesASM(externalUserDetail.getExternal_contact(), weCustomer);
            this.saveOrUpdate(weCustomer);

            //客户与通讯录客户关系
            List<WeTag> weTags = new ArrayList<>();
            List<WeTagGroup> weGroups = new ArrayList<>();
            List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
            List<WeFlowerCustomerRel> weFlowerCustomerRel = new ArrayList<>();
            externalUserDetail.getFollow_user().stream().forEach(kk -> {

                Long weFlowerCustomerRelId = SnowFlakeUtil.nextId();
                weFlowerCustomerRel.add(WeFlowerCustomerRel.builder()
                        .id(weFlowerCustomerRelId)
                        .userId(kk.getUserid())
                        .description(kk.getDescription())
                        .remarkCorpName(kk.getRemarkCorpName())
                        .remarkMobiles(Arrays.stream(kk.getRemarkMobiles()).collect(Collectors.joining(",")))
                        .operUserid(kk.getUserid())
                        .addWay(kk.getAddWay())
                        .state(kk.getState())
                        .status("0")
                        .externalUserid(weCustomer.getExternalUserid())
                        .createTime(new Date(kk.getCreatetime() * 1000L))
                        .build());

                List<ExternalUserTag> tags = kk.getTags();
                if (CollectionUtil.isNotEmpty(tags)) {

                    //获取相关标签组
                    WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getCorpTagListByTagIds(WeFindCropTagParam.builder()
                            .tag_id(ArrayUtil.toArray(tags.stream().map(ExternalUserTag::getTag_id).collect(Collectors.toList()), String.class))
                            .build());

                    if (weCropGroupTagListDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
                        List<WeCropGroupTagDto> tagGroups = weCropGroupTagListDto.getTag_group();
                        if (CollectionUtil.isNotEmpty(tagGroups)) {
                            tagGroups.stream().forEach(tagGroup -> {
                                weGroups.add(
                                        WeTagGroup.builder()
                                                .groupId(tagGroup.getGroup_id())
                                                .gourpName(tagGroup.getGroup_name())
                                                .createBy(kk.getUserid())
                                                .build()
                                );

                                List<WeCropTagDto> weCropTagDtos = tagGroup.getTag();

                                if (CollectionUtil.isNotEmpty(weCropTagDtos)) {
                                    Set<String> tagIdsSet = weCropTagDtos.stream().map(WeCropTagDto::getId).collect(Collectors.toSet());

                                    tags.stream().forEach(tag -> {

                                        if (tagIdsSet.contains(tag.getTag_id())) {

                                            weTags.add(
                                                    WeTag.builder()
                                                            .groupId(tagGroup.getGroup_id())
                                                            .tagId(tag.getTag_id())
                                                            .name(tag.getTag_name())
                                                            .build()
                                            );

                                            weFlowerCustomerTagRels.add(
                                                    WeFlowerCustomerTagRel.builder()
                                                            .flowerCustomerRelId(weFlowerCustomerRelId)
                                                            .tagId(tag.getTag_id())
                                                            .createTime(new Date())
                                                            .build()
                                            );


                                        }


                                    });


                                }


                            });


                        }
                    }


                }
            });
            List<WeFlowerCustomerRel> weFlowerCustomerRels = iWeFlowerCustomerRelService.list(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                    .eq(WeFlowerCustomerRel::getExternalUserid, weCustomer.getExternalUserid()));

            try {
                if (CollectionUtil.isNotEmpty(weFlowerCustomerRels)) {
                    List<Long> weFlowerCustomerRelIds = weFlowerCustomerRels.stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList());
                    iWeFlowerCustomerTagRelService.remove(
                            new LambdaQueryWrapper<WeFlowerCustomerTagRel>().in(WeFlowerCustomerTagRel::getFlowerCustomerRelId,
                                    weFlowerCustomerRelIds)
                    );

                    iWeFlowerCustomerRelService.removeByIds(
                            weFlowerCustomerRelIds
                    );
                }


                iWeFlowerCustomerRelService.saveBatch(weFlowerCustomerRel);

                //设置标签跟客户关系，标签和标签组,saveOrUpdate，建立标签与添加人关系
                if (CollectionUtil.isNotEmpty(weTags) && CollectionUtil.isNotEmpty(weGroups)) {
                    iWeTagService.saveOrUpdateBatch(weTags);
                    iWeTagGroupService.saveOrUpdateBatch(weGroups);
                    iWeFlowerCustomerTagRelService.saveOrUpdateBatch(weFlowerCustomerTagRels);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Exception:【{}】",e);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomersByEid(String externalUserid) {
        this.removeById(externalUserid);
    }

    @Override
    public void sendWelcomeMsg(WeWelcomeMsg weWelcomeMsg) {
        weCustomerClient.sendWelcomeMsg(weWelcomeMsg);
    }

    @Override
    public boolean updateCustomerChatStatus(String externalUserId) {
        WeCustomer weCustomer = new WeCustomer();
        weCustomer.setExternalUserid(externalUserId);
        weCustomer.setIsOpenChat(1);
        return weCustomerMapper.updateWeCustomer(weCustomer) == 1;
    }

    @Override
    public List<WeUser> getCustomerByTag(List<String> ids) {
        return weCustomerMapper.getCustomerByTag(ids);
    }



    @Override
    public WeCustomerPortrait findCustomerByOperUseridAndCustomerId(String externalUserid, String userid) {
        WeCustomerPortrait weCustomerPortrait
                = weCustomerMapper.findCustomerByOperUseridAndCustomerId(externalUserid, userid);

        if(null != weCustomerPortrait){
            //获取当前客户拥有得标签
            weCustomerPortrait.setWeTagGroupList(
                    iWeTagGroupService.findCustomerTagByFlowerCustomerRelId(
                            weCustomerPortrait.getFlowerCustomerRelId()
                    )
            );

           //客户社交关系
            weCustomerPortrait.setSocialConn(
                   this.baseMapper.countSocialConn(externalUserid,userid)
            );
        }else {
            weCustomerPortrait=new WeCustomerPortrait();
        }


        return weCustomerPortrait;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWeCustomerPortrait(WeCustomerPortrait weCustomerPortrait) {
        WeCustomer weCustomer
                = WeCustomer.builder().build();
        BeanUtils.copyBeanProp(weCustomer,weCustomerPortrait);
        //更新用户基本信息表
        this.updateById(
                weCustomer
        );


        WeFlowerCustomerRel weFlowerCustomerRel = WeFlowerCustomerRel.builder().build();
        BeanUtils.copyBeanProp(weFlowerCustomerRel,weCustomerPortrait);
        //更新企业添加人表
        iWeFlowerCustomerRelService.update(weFlowerCustomerRel,new LambdaQueryWrapper<WeFlowerCustomerRel>()
        .eq(WeFlowerCustomerRel::getExternalUserid,weCustomerPortrait.getExternalUserid())
        .eq(WeFlowerCustomerRel::getUserId,weCustomerPortrait.getUserId()));

    }





}
