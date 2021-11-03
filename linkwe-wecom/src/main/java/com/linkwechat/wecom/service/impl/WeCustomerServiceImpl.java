package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.client.WeCropTagClient;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.AllocateWeCustomerDto;
import com.linkwechat.wecom.domain.dto.WeCustomerDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.dto.customer.*;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagDto;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagListDto;
import com.linkwechat.wecom.domain.dto.tag.WeCropTagDto;
import com.linkwechat.wecom.domain.dto.tag.WeFindCropTagParam;
import com.linkwechat.wecom.domain.vo.WeCustomerDetailVo;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private WeCustomerClient weCustomerClient;





    @Autowired
    private IWeTagGroupService iWeTagGroupService;


    @Autowired
    private IWeFlowerCustomerTagRelService iWeFlowerCustomerTagRelService;


    @Autowired
    private IWeAllocateCustomerService iWeAllocateCustomerService;


    @Autowired
    private WeUserClient weUserClient;


    @Autowired
    private  IWeCustomerTrajectoryService iWeCustomerTrajectoryService;



    /**
     * 查询企业微信客户
     *
     * @param externalUserId 企业微信客户ID
     * @return 企业微信客户
     */
    @Override
    public WeCustomer selectWeCustomerById(String externalUserId) {
        return this.baseMapper.selectWeCustomerById(externalUserId);
    }

    /**
     * 查询企业微信客户列表
     *
     * @param weCustomer 企业微信客户
     * @return 企业微信客户
     */
    @Override
    public List<WeCustomer> selectWeCustomerList(WeCustomer weCustomer) {
        //当前登录用户为企业用户
        if(Constants.USER_TYPE_WECOME
                .equals(SecurityUtils.getLoginUser().getUser().getUserType())){
            weCustomer.setUserIds((SecurityUtils.getLoginUser().getUser().getWeUserId()));
        }

        return this.baseMapper.selectWeCustomerList(weCustomer);
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
        //获取所有可以添加客户的企业员工
        FollowUserList followUserList = weCustomerClient.getFollowUserList();
        if (WeConstans.WE_SUCCESS_CODE.equals(followUserList.getErrcode())
                && ArrayUtil.isNotEmpty(followUserList.getFollow_user())) {
            String[] follow_user = followUserList.getFollow_user();
            if(ArrayUtil.isNotEmpty(follow_user)){

                List<ExternalUserDetail> externalUserDetails = new ArrayList<>();
                getByUser(follow_user, null, externalUserDetails);

                if(CollectionUtil.isNotEmpty(externalUserDetails)){
                    this.weFlowerCustomerHandle(
                            externalUserDetails
                    );
                }

            }




        }


    }


    /**
     * 客户同步业务处理
     *
     */
    private void weFlowerCustomerHandle(List<ExternalUserDetail> list) {


        List<WeCustomer> weCustomerList=new ArrayList<>();

        List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();


        list.forEach(userDetail -> {
            //客户入库
            WeCustomer weCustomer = new WeCustomer();
            BeanUtils.copyPropertiesignoreOther(userDetail.getExternal_contact(), weCustomer);


            Optional.ofNullable(userDetail.getFollow_info()).ifPresent(followInfo -> {
                weCustomer.setFirstUserId(followInfo.getUserid());
                weCustomer.setFirstAddTime(new Date(followInfo.getCreatetime() * 1000L));
                weCustomerList.add(weCustomer);
                List<String> tags = Stream.of(followInfo.getTag_id()).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(tags)) {
                    tags.stream().forEach(tag->{
                        weFlowerCustomerTagRels.add(
                                WeFlowerCustomerTagRel.builder()
                                        .userId(followInfo.getUserid())
                                        .externalUserid(weCustomer.getExternalUserid())
                                        .tagId(tag)
                                        .build()
                        );
                    });
                }
            });
        });


        if(CollectionUtil.isNotEmpty(weFlowerCustomerTagRels)){
            List<List<WeFlowerCustomerTagRel>> tagRels = Lists.partition(weFlowerCustomerTagRels, 500);
            for(List<WeFlowerCustomerTagRel> tagRelss:tagRels){
                iWeFlowerCustomerTagRelService.batchAddOrUpdate(tagRelss);
            }
        }

        if(CollectionUtil.isNotEmpty(weCustomerList)){
            List<List<WeCustomer>> weCustomers = Lists.partition(weCustomerList, 500);
            for(List<WeCustomer> weCustomer : weCustomers){
                this.baseMapper.batchAddOrUpdate(weCustomer);
            }

        }
    }



    /**
     * 批量获取客户详情
     *
     * @param userIds     企业成员的userid
     * @param nextCursor 用于分页查询的游标
     * @param list       返回结果
     */
    private void getByUser(String[] userIds, String nextCursor, List<ExternalUserDetail> list) {
        ExternalUserList externalUserList = weCustomerClient.getByUser((WeCustomerDto.BatchCustomerParam.builder()
                .userid_list(userIds)
                .cursor(nextCursor)
                .limit(100)
                .build()));
        if (WeConstans.WE_SUCCESS_CODE.equals(externalUserList.getErrcode())
                || WeConstans.NOT_EXIST_CONTACT.equals(externalUserList.getErrcode())
                && ArrayUtil.isNotEmpty(externalUserList.getExternal_contact_list())) {
            list.addAll(externalUserList.getExternal_contact_list());
            if (StringUtils.isNotEmpty(externalUserList.getNext_cursor())) {
                getByUser(userIds, externalUserList.getNext_cursor(), list);
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

        //所需分配的客户
        List<WeCustomer> allocateWeCustomers = this.list(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getFirstUserId, weLeaveUserInfoAllocateVo.getHandoverUserid()));
        if(CollectionUtil.isNotEmpty(allocateWeCustomers)){
            //删除原有的
            this.baseMapper.deleteWeCustomerByUserIds(
                    new String[]{weLeaveUserInfoAllocateVo.getHandoverUserid()}
            );
            allocateWeCustomers.stream().forEach(k->{
                k.setFirstUserId(weLeaveUserInfoAllocateVo.getTakeoverUserid());
            });
            //新增
            this.baseMapper.batchAddOrUpdate(
                    allocateWeCustomers
            );
                //记录接受离职表
                List<WeAllocateCustomer> weAllocateCustomers = new ArrayList<>();
                allocateWeCustomers.stream().forEach(k->{
                    weAllocateCustomers.add(
                        WeAllocateCustomer.builder()
                                .allocateTime(new Date())
                                .externalUserid(k.getExternalUserid())
                                .handoverUserid(weLeaveUserInfoAllocateVo.getHandoverUserid())
                                .takeoverUserid(weLeaveUserInfoAllocateVo.getTakeoverUserid())
                                .build()
                    );
                });

                if(CollectionUtil.isNotEmpty(weAllocateCustomers)
                &&iWeAllocateCustomerService.saveBatch(weAllocateCustomers)){
                    //同步企业微信
                   weUserClient.allocateCustomer(AllocateWeCustomerDto.builder()
                                .external_userid(
                                        ArrayUtil.toArray(allocateWeCustomers.stream().map(WeCustomer::getExternalUserid).collect(Collectors.toList()), String.class)
                                )
                                .handover_userid(weLeaveUserInfoAllocateVo.getHandoverUserid())
                                .takeover_userid(weLeaveUserInfoAllocateVo.getTakeoverUserid())
                                .build());
                }

            }


    }


    /**
     * 编辑客户标签
     *
     * @param weMakeCustomerTag
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeLabel(WeMakeCustomerTag weMakeCustomerTag) {

        List<WeTag> addTag = weMakeCustomerTag.getAddTag();
        if(CollectionUtil.isNotEmpty(addTag)){

            CutomerTagEdit cutomerTagEdit = CutomerTagEdit.builder()
                    .external_userid(weMakeCustomerTag.getExternalUserid())
                    .userid(weMakeCustomerTag.getUserId())
                    .add_tag(ArrayUtil.toArray(addTag.stream().map(WeTag::getTagId).collect(Collectors.toList()), String.class))
                    .build();

            //获取需要移除的标签
            List<WeFlowerCustomerTagRel> tagRels = iWeFlowerCustomerTagRelService.list(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                    .eq(WeFlowerCustomerTagRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                    .eq(WeFlowerCustomerTagRel::getUserId, weMakeCustomerTag.getUserId())
                    .notIn(WeFlowerCustomerTagRel::getTagId, addTag.stream().map(WeTag::getTagId).collect(Collectors.toList()))
            );
            if(CollectionUtil.isNotEmpty(tagRels)){
                //设置需要移除的标签
                if(iWeFlowerCustomerTagRelService.remove(
                        new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                                .eq(WeFlowerCustomerTagRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                                .eq(WeFlowerCustomerTagRel::getUserId, weMakeCustomerTag.getUserId())
                                .notIn(WeFlowerCustomerTagRel::getTagId, addTag.stream().map(WeTag::getTagId).collect(Collectors.toList())
                 ))){
                    cutomerTagEdit.setRemove_tag(
                            ArrayUtil.toArray(tagRels.stream().map(WeFlowerCustomerTagRel::getTagId).collect(Collectors.toList()), String.class)
                    );
                }
            }

            List<WeFlowerCustomerTagRel> newTagRels = new ArrayList<>();
            addTag.stream().forEach(k->{
                newTagRels.add(
                        WeFlowerCustomerTagRel.builder()
                                .userId(weMakeCustomerTag.getUserId())
                                .externalUserid(weMakeCustomerTag.getExternalUserid())
                                .tagId(k.getTagId())
                                .build()
                );
            });

                iWeFlowerCustomerTagRelService.batchAddOrUpdate(newTagRels);

                weCustomerClient.makeCustomerLabel(
                        cutomerTagEdit
                );

        }else{//为空，取消当前客户所有标签
            List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = iWeFlowerCustomerTagRelService.list(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                    .eq(WeFlowerCustomerTagRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                    .eq(WeFlowerCustomerTagRel::getUserId, weMakeCustomerTag.getUserId()));
            if(CollectionUtil.isNotEmpty(weFlowerCustomerTagRels)){
                if(iWeFlowerCustomerTagRelService.remove(
                        new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                                .eq(WeFlowerCustomerTagRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                                .eq(WeFlowerCustomerTagRel::getUserId, weMakeCustomerTag.getUserId()))){
                    //同步企业微信端
                    weCustomerClient.makeCustomerLabel(
                            CutomerTagEdit.builder()
                                      .external_userid(weMakeCustomerTag.getExternalUserid())
                                      .userid(weMakeCustomerTag.getUserId())
                                      .remove_tag(ArrayUtil.toArray(weFlowerCustomerTagRels.stream().map(WeFlowerCustomerTagRel::getTagId).collect(Collectors.toList()), String.class))
                                      .build()
                   );
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
        String userId=null;
//        if(Constants.USER_TYPE_WECOME.equals(SecurityUtils.getLoginUser().getUser().getUserType())){
//             userId=SecurityUtils.getLoginUser().getUser().getWeUserId();
//        }
        return this.baseMapper.getCustomersByUserId(externalUserid,userId);
    }


    /**
     * 通过单个客户消息
     * @param externalUserid 客户id
     * @param userId 添加人id
      */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getCustomersInfoAndSynchWeCustomer(String externalUserid,String userId) {

        //获取指定客户的详情
        ExternalUserDetail externalUserDetail = weCustomerClient.get(externalUserid);

        if (WeConstans.WE_SUCCESS_CODE.equals(externalUserDetail.getErrcode())) {
            //客户入库
            WeCustomer weCustomer = new WeCustomer();
            BeanUtils.copyPropertiesASM(externalUserDetail.getExternal_contact(), weCustomer);
            weCustomer.setFirstUserId(userId);
            List<ExternalUserDetail.FollowUser> follow_user = externalUserDetail.getFollow_user();
            if(CollectionUtil.isNotEmpty(follow_user)){
                ExternalUserDetail.FollowUser followUser = follow_user.stream().filter(e -> e.getUserid().equals(userId)).findFirst().get();

                if(null != followUser){
                    weCustomer.setFirstAddTime(new Date(followUser.getCreatetime() * 1000L));

                    this.baseMapper.batchAddOrUpdate(
                            ListUtil.toList(weCustomer)
                    );

                    //设置标签
                    List<ExternalUserTag> tags = followUser.getTags();
                    if(CollectionUtil.isNotEmpty(tags)){
                        List<WeFlowerCustomerTagRel> tagRels=new ArrayList<>();
                        tags.stream().forEach(k->{
                            tagRels.add(
                                    WeFlowerCustomerTagRel.builder()
                                            .createTime(new Date())
                                            .userId(userId)
                                            .tagId(k.getTag_id())
                                            .externalUserid(externalUserid)
                                            .build()
                            );
                        });
                        iWeFlowerCustomerTagRelService.batchAddOrUpdate(tagRels);
                    }


                }


            }

        }


//            //判断该客户是否存在
//            if(this.baseMapper.selectWeCustomerById(externalUserid) != null){//直接更新无需修改首位添加人和时间
//                weCustomer.setDelFlag(new Integer(0));
//                this.baseMapper.updateWeCustomer(weCustomer);
//            }else {
//                weCustomer.setFirstUserId(userId);
//                weCustomer.setFirstAddTime(new Date());
//                this.save(weCustomer);
//            }
//
//            //客户与通讯录客户关系
//            List<WeTag> weTags = new ArrayList<>();
//            List<WeTagGroup> weGroups = new ArrayList<>();
//            List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
//            List<WeFlowerCustomerRel> weFlowerCustomerRel = new ArrayList<>();
//            externalUserDetail.getFollow_user().stream().forEach(kk -> {
//
//                Long weFlowerCustomerRelId = SnowFlakeUtil.nextId();
//                weFlowerCustomerRel.add(WeFlowerCustomerRel.builder()
//                        .id(weFlowerCustomerRelId)
//                        .userId(kk.getUserid())
//                        .description(kk.getDescription())
//                        .remarkCorpName(kk.getRemarkCorpName())
//                        .remarkMobiles(Arrays.stream(kk.getRemarkMobiles()).collect(Collectors.joining(",")))
//                        .operUserid(kk.getUserid())
//                        .addWay(kk.getAddWay())
//                        .state(kk.getState())
//                        .status("0")
//                        .externalUserid(weCustomer.getExternalUserid())
//                        .createTime(new Date(kk.getCreatetime() * 1000L))
//                        .build());
//
//                List<ExternalUserTag> tags = kk.getTags();
//                if (CollectionUtil.isNotEmpty(tags)) {
//
//                    //获取相关标签组
//                    WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getCorpTagListByTagIds(WeFindCropTagParam.builder()
//                            .tag_id(ArrayUtil.toArray(tags.stream().map(ExternalUserTag::getTag_id).collect(Collectors.toList()), String.class))
//                            .build());
//
//                    if (weCropGroupTagListDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)) {
//                        List<WeCropGroupTagDto> tagGroups = weCropGroupTagListDto.getTag_group();
//                        if (CollectionUtil.isNotEmpty(tagGroups)) {
//                            tagGroups.stream().forEach(tagGroup -> {
//                                weGroups.add(
//                                        WeTagGroup.builder()
//                                                .groupId(tagGroup.getGroup_id())
//                                                .gourpName(tagGroup.getGroup_name())
//                                                .build()
//                                );
//
//                                List<WeCropTagDto> weCropTagDtos = tagGroup.getTag();
//
//                                if (CollectionUtil.isNotEmpty(weCropTagDtos)) {
//                                    Set<String> tagIdsSet = weCropTagDtos.stream().map(WeCropTagDto::getId).collect(Collectors.toSet());
//
//                                    tags.stream().forEach(tag -> {
//
//                                        if (tagIdsSet.contains(tag.getTag_id())) {
//
//                                            weTags.add(
//                                                    WeTag.builder()
//                                                            .groupId(tagGroup.getGroup_id())
//                                                            .tagId(tag.getTag_id())
//                                                            .name(tag.getTag_name())
//                                                            .build()
//                                            );
//
//                                            weFlowerCustomerTagRels.add(
//                                                    WeFlowerCustomerTagRel.builder()
//                                                            .tagId(tag.getTag_id())
//                                                            .createTime(new Date())
//                                                            .build()
//                                            );
//
//
//                                        }
//
//
//                                    });
//
//
//                                }
//
//
//                            });
//
//
//                        }
//                    }
//
//
//                }
//            });
//
//
//            List<WeFlowerCustomerRel> weFlowerCustomerRels = iWeFlowerCustomerRelService.list(new LambdaQueryWrapper<WeFlowerCustomerRel>()
//                    .eq(WeFlowerCustomerRel::getExternalUserid, weCustomer.getExternalUserid()));
//
//            try {
//                if (CollectionUtil.isNotEmpty(weFlowerCustomerRels)) {
//                    List<Long> weFlowerCustomerRelIds = weFlowerCustomerRels.stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList());
//
//                    iWeFlowerCustomerRelService.removeByIds(
//                            weFlowerCustomerRelIds
//                    );
//                }
//
//
//                iWeFlowerCustomerRelService.saveBatch(weFlowerCustomerRel);
//
//                //设置标签跟客户关系，标签和标签组,saveOrUpdate，建立标签与添加人关系
//                if (CollectionUtil.isNotEmpty(weTags) && CollectionUtil.isNotEmpty(weGroups)) {
//                    iWeTagService.saveOrUpdateBatch(weTags);
//                    iWeTagGroupService.saveOrUpdateBatch(weGroups);
//
//
//                    List<WeFlowerCustomerTagRel> tagRels
//                            = weFlowerCustomerTagRels.stream().filter(rel -> Objects.nonNull(rel.getUserId())).collect(Collectors.toList());
//
//                    if(CollectionUtil.isNotEmpty(tagRels)){
//
//                        iWeFlowerCustomerTagRelService.saveOrUpdateBatch(tagRels);
//
//                    }
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                log.error("Exception:【{}】",e);
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            }

//        }


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
        return this.baseMapper.updateWeCustomer(weCustomer) == 1;
    }





    @Override
    public WeCustomerPortrait findCustomerByOperUseridAndCustomerId(String externalUserid, String userid) throws Exception {
        WeCustomerPortrait weCustomerPortrait
                = this.baseMapper.findCustomerByOperUseridAndCustomerId(externalUserid, userid);

        if(null != weCustomerPortrait){

          if(weCustomerPortrait.getBirthday() != null){
                        weCustomerPortrait.setAge(DateUtils.getAge(weCustomerPortrait.getBirthday()));
          }

            //获取当前客户拥有得标签
            weCustomerPortrait.setWeTagGroupList(
                    iWeTagGroupService.findCustomerTagByFlowerCustomerRelId(
                            externalUserid,userid
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
        //手机号
        weCustomer.setPhone(weCustomerPortrait.getRemarkMobiles());
        weCustomer.setCorpName(weCustomerPortrait.getRemarkCorpName());
        weCustomer.setFirstUserId(weCustomerPortrait.getUserId());


        this.update(weCustomer,new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getFirstUserId,weCustomerPortrait.getUserId())
                .eq(WeCustomer::getExternalUserid,weCustomerPortrait.getExternalUserid()));
        //更新用户基本信息表
//        this.baseMapper.batchAddOrUpdate(ListUtil.toList(weCustomer));
//        this.updateById(
//                weCustomer
//        );
//        WeFlowerCustomerRel weFlowerCustomerRel = WeFlowerCustomerRel.builder().build();
//        BeanUtils.copyBeanProp(weFlowerCustomerRel,weCustomerPortrait);
        //添加轨迹内容(信息动态)
        iWeCustomerTrajectoryService.inforMationNews(weCustomerPortrait.getUserId(),weCustomerPortrait.getExternalUserid(), TrajectorySceneType.TRAJECTORY_TYPE_XXDT_BCZL.getKey());
    }

    @Override
    public List<WeCustomer> selectWeCustomerAllList(WeCustomer weCustomer) {
        return this.baseMapper.selectWeCustomerList(weCustomer);
    }

    @Override
    public List<WeCustomer> selectWeCustomerListNoRel(WeCustomer weCustomer) {
        return this.baseMapper.selectWeCustomerListNoRel(weCustomer);
    }


    /**
     * 重构版客户列表
     * @param weCustomerList
     * @return
     */
    @Override
    public List<WeCustomerList> findWeCustomerList(WeCustomerList weCustomerList) {

        return this.baseMapper.findWeCustomerList(weCustomerList);
    }




    @Override
    public WeCustomerDetailVo findWeCustomerDetail(String externalUserid) {
        WeCustomerDetailVo weCustomerDetailVo=new WeCustomerDetailVo();
        WeCustomer weCustomer
                = this.getById(externalUserid);
        BeanUtils.copyBeanProp(weCustomer,weCustomerDetailVo);
        weCustomerDetailVo.setAddWeGroupNames(this.baseMapper.findAddWeUserNames(externalUserid));
        weCustomerDetailVo.setAddWeuserNames(this.baseMapper.findAddWeUserNames(externalUserid));
        weCustomerDetailVo.setGroupTags(this.baseMapper.findCustomerGroupTag(externalUserid));
        weCustomerDetailVo.setBelongUserInfos(this.baseMapper.findCusertomerBelongUserInfo(externalUserid));
        return weCustomerDetailVo;
    }

    @Override
    public void getCustomerByCondition(JSONObject params) {

    }


}
