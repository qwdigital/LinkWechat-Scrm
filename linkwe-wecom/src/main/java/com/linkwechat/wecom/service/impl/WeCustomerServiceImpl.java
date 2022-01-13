package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.enums.*;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.annotation.SynchRecord;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.constants.SynchRecordConstants;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.AllocateWeCustomerDto;
import com.linkwechat.wecom.domain.dto.WeCustomerDto;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.WeWelcomeMsg;
import com.linkwechat.wecom.domain.dto.customer.*;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagListDto;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;
import com.linkwechat.wecom.domain.vo.WeOnTheJobCustomerVo;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
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
    private IWeFlowerCustomerTagRelService iWeFlowerCustomerTagRelService;


    @Autowired
    private IWeAllocateCustomerService iWeAllocateCustomerService;


    @Autowired
    private WeUserClient weUserClient;


    @Autowired
    private IWeCustomerTrajectoryService iWeCustomerTrajectoryService;

    @Autowired
    private IWeTagService iWeTagService;

    @Autowired
    private IWeGroupService iWeGroupService;

    @Autowired
    private IWeUserService iWeUserService;



    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;




    /**
     * 重构版客户列表
     *
     * @param weCustomerList
     * @return
     */
    @Override
    public List<WeCustomerList> findWeCustomerList(WeCustomerList weCustomerList, PageDomain pageDomain) {

        return this.baseMapper.findWeCustomerList(weCustomerList,pageDomain);
    }


    /**
     * 客户总数统计
     * @param weCustomerList
     * @return
     */
    @Override
    public long countWeCustomerList(WeCustomerList weCustomerList) {
        return this.baseMapper.countWeCustomerList(weCustomerList);
    }


    /**
     * 客户同步接口
     *
     * @return
     */
    @Override
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER)
    @Async
    public void synchWeCustomer(){

        //获取所有可以添加客户的企业员工
        FollowUserList followUserList = weCustomerClient.getFollowUserList();
        if (WeConstans.WE_SUCCESS_CODE.equals(followUserList.getErrcode())
                && ArrayUtil.isNotEmpty(followUserList.getFollow_user())) {
            List<List<String>> partition = Lists.partition(Arrays.asList(followUserList.getFollow_user()), 100);
            //此处逻辑,根据分片的数据，每片从线程池获取线程，然后启动不同线程处理下面的逻辑
            for(List<String> follow_user:partition) {
                List<ExternalUserDetail> externalUserDetails = new ArrayList<>();
                getByUser(follow_user.stream().toArray(String[]::new), null, externalUserDetails);
                if (CollectionUtil.isNotEmpty(externalUserDetails)) {
                    List<List<ExternalUserDetail>> userDetailPartition = Lists.partition(externalUserDetails, 1000);
                    for(List<ExternalUserDetail> details:userDetailPartition){
                        taskExecutor.submit(()->{
                            //此处也可多线程数据分片处理
                            weFlowerCustomerHandle(
                                    details,true
                            );

                        });
                    }
                }

            }





        }


    }


    /**
     * 客户同步业务处理
     */
    private void weFlowerCustomerHandle(List<ExternalUserDetail> list,boolean isSynchCustomerTagRel) {


        List<WeCustomer> weCustomerList = new ArrayList<>();

        List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();



        list.forEach(userDetail -> {
            //客户入库
            WeCustomer weCustomer = new WeCustomer();
            BeanUtils.copyPropertiesignoreOther(userDetail.getExternal_contact(), weCustomer);
            weCustomer.setCustomerType(userDetail.getExternal_contact().getType());
            weCustomer.setCustomerName(userDetail.getExternal_contact().getName());
            weCustomer.setDelFlag(new Integer(0));
            Optional.ofNullable(userDetail.getFollow_info()).ifPresent(followInfo -> {
                weCustomer.setState(followInfo.getState());
                weCustomer.setFirstUserId(followInfo.getUserid());
                weCustomer.setFirstAddTime(new Date(followInfo.getCreatetime() * 1000L));
                weCustomer.setAddMethod(followInfo.getAdd_way());

                if(isSynchCustomerTagRel){
                    List<String> tags = Stream.of(followInfo.getTag_id()).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(tags)) {
                        //获取标签
                        weCustomer.setTagIds(Joiner.on(",").join(tags));

                        tags.stream().forEach(tag -> {
                            weFlowerCustomerTagRels.add(
                                    WeFlowerCustomerTagRel.builder()
                                            .userId(followInfo.getUserid())
                                            .externalUserid(weCustomer.getExternalUserid())
                                            .tagId(tag)
                                            .delFlag(new Integer(0))
                                            .isCompanyTag(true)
                                            .build()
                            );
                        });
                    }
                }

                weCustomerList.add(weCustomer);

            });


        });



        if (CollectionUtil.isNotEmpty(weFlowerCustomerTagRels)) {
            List<List<WeFlowerCustomerTagRel>> tagRels = Lists.partition(weFlowerCustomerTagRels, 500);
            for (List<WeFlowerCustomerTagRel> tagRelss : tagRels) {
                iWeFlowerCustomerTagRelService.batchAddOrUpdate(tagRelss);
            }
        }

        if (CollectionUtil.isNotEmpty(weCustomerList)) {
            List<List<WeCustomer>> weCustomers = Lists.partition(weCustomerList, 500);
            for (List<WeCustomer> weCustomer : weCustomers) {
                this.baseMapper.batchAddOrUpdate(weCustomer);
            }

        }
    }


    /**
     * 批量获取客户详情
     *
     * @param userIds    企业成员的userid
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
    @Transactional(rollbackFor = Exception.class)
    public void allocateWeCustomer(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {

        List<WeAllocateCustomer> weAllocateCustomerList = iWeAllocateCustomerService.list(new LambdaQueryWrapper<WeAllocateCustomer>()
                .eq(WeAllocateCustomer::getHandoverUserid, weLeaveUserInfoAllocateVo.getHandoverUserid()));

        if(CollectionUtil.isNotEmpty(weAllocateCustomerList)){

            //所需分配的客户
            List<WeCustomer> allocateWeCustomers = this.list(new LambdaQueryWrapper<WeCustomer>()
                    .in(WeCustomer::getExternalUserid, weAllocateCustomerList.stream().map(WeAllocateCustomer::getExternalUserid).collect(Collectors.toList()))
                    .eq(WeCustomer::getFirstUserId,weLeaveUserInfoAllocateVo.getHandoverUserid()));

            if (CollectionUtil.isNotEmpty(allocateWeCustomers)) {

                //设置客户的接手人和状态设置为删除
                allocateWeCustomers.stream().forEach(k -> {
                    k.setTakeoverUserId(weLeaveUserInfoAllocateVo.getTakeoverUserid());
                    k.setDelFlag(Constants.DELETE_STATE);
                });


                WeResultDto weResultDto = weUserClient.allocateCustomer(
                        AllocateWeCustomerDto.builder()
                                .external_userid(
                                        allocateWeCustomers.stream().map(WeCustomer::getExternalUserid).collect(Collectors.toList()).stream().toArray(String[]::new)
                                )
                                .handover_userid(weLeaveUserInfoAllocateVo.getHandoverUserid())
                                .takeover_userid(weLeaveUserInfoAllocateVo.getTakeoverUserid())
                                .build()
                );

                if(weResultDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                    this.baseMapper.batchAddOrUpdate(allocateWeCustomers);
                    weAllocateCustomerList.stream().forEach(k->{
                        k.setAllocateTime(new Date());
                        k.setTakeoverUserid(weLeaveUserInfoAllocateVo.getTakeoverUserid());
                    });
                    iWeAllocateCustomerService.batchAddOrUpdate(weAllocateCustomerList);
                }


            }



        }





    }


    /**
     * 在职员工客户分配
     *
     * @param weOnTheJobCustomerVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateOnTheJobCustomer(WeOnTheJobCustomerVo weOnTheJobCustomerVo) {


        if (this.getOne(
                new LambdaQueryWrapper<WeCustomer>()
                        .eq(WeCustomer::getExternalUserid, weOnTheJobCustomerVo.getExternalUserid())
                        .eq(WeCustomer::getFirstUserId, weOnTheJobCustomerVo.getTakeoverUserId())
                        .eq(WeCustomer::getDelFlag, new Integer(0))) != null) {

            throw new WeComException("当前接手人已经拥有此客户,不可继承");
        }

        List<WeAllocateCustomer> weAllocateCustomers = iWeAllocateCustomerService.list(new LambdaQueryWrapper<WeAllocateCustomer>()
                .eq(WeAllocateCustomer::getExternalUserid, weOnTheJobCustomerVo.getExternalUserid())
                .between(WeAllocateCustomer::getAllocateTime,DateUtils.getBeforeByDayTime(-90)
                        ,DateUtils.getBeforeByDayTime(0))
        );

        if (CollectionUtil.isNotEmpty(weAllocateCustomers)
                && weAllocateCustomers.size() >= 2) {
            throw new WeComException("当前客户在90个自然日内已被转接2次,暂时无法操作");
        }


        //修改库中关系
        WeCustomer weCustomer = this.getOne(
                new LambdaQueryWrapper<WeCustomer>()
                        .eq(WeCustomer::getExternalUserid, weOnTheJobCustomerVo.getExternalUserid())
                        .eq(WeCustomer::getFirstUserId, weOnTheJobCustomerVo.getHandoverUserId())
                        .eq(WeCustomer::getDelFlag, new Integer(0))
        );

        if (null != weCustomer) {

            if (StringUtils.isEmpty(weCustomer.getTakeoverUserId())) { //首次被继承

                this.extentCustomer(weCustomer, weOnTheJobCustomerVo);

            } else { //非首次继承(二个情况)

                //校验上一个人的移交状态
                List<JobExtendsCustomer.ExtendsCustomer> extendsCustomers = weUserClient.transferResult(JobExtendsCustomer.JobExtendsParam.builder()
                        .handover_userid(weCustomer.getFirstUserId())
                        .takeover_userid(weCustomer.getTakeoverUserId())
                        .build()).getCustomer();

                if (CollectionUtil.isNotEmpty(extendsCustomers)) {
                    JobExtendsCustomer.ExtendsCustomer extendsCustomer
                            = extendsCustomers.stream().collect(Collectors.toMap(JobExtendsCustomer.ExtendsCustomer::getExternal_userid
                            , ExtendsCustomer -> ExtendsCustomer)).get(weOnTheJobCustomerVo.getExternalUserid());
                    if (null != extendsCustomer) {

                        if (extendsCustomer.getStatus().equals(AllocateCustomerStatus.JOB_EXTENDS_DDJT.getCode())) {
                            WeUser weUser
                                    = iWeUserService.getById(weCustomer.getTakeoverUserId());
                            if (weUser != null) {
                                throw new WeComException(
                                        "当前客户:" + weCustomer.getCustomerName() + ",已被:" + weUser.getName() + "接替,暂时不可被继承"
                                );
                            }
                        } else { //可继续被新用户接替

                             this.extentCustomer(weCustomer, weOnTheJobCustomerVo);

                        }


                    }

                }
            }


        }
    }



    //接替客户
    private void extentCustomer(WeCustomer weCustomer,WeOnTheJobCustomerVo weOnTheJobCustomerVo){
        weCustomer.setTakeoverUserId(weOnTheJobCustomerVo.getTakeoverUserId());

        if(this.update(
                weCustomer,new LambdaQueryWrapper<WeCustomer>()
                        .eq(WeCustomer::getExternalUserid,weCustomer.getExternalUserid())
                        .eq(WeCustomer::getFirstUserId,weCustomer.getFirstUserId()))){
            JobExtendsCustomer jobExtendsCustomer = weUserClient.transferCustomer(
                    AllocateWeCustomerDto.builder()
                            .external_userid(
                                    weOnTheJobCustomerVo.getExternalUserid().split(",")
                            )
                            .handover_userid(weOnTheJobCustomerVo.getHandoverUserId())
                            .takeover_userid(weOnTheJobCustomerVo.getTakeoverUserId())
                            .build()
            );

            if(jobExtendsCustomer.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                iWeAllocateCustomerService.batchAddOrUpdate(
                        ListUtil.toList(
                                WeAllocateCustomer.builder()
                                        .allocateTime(new Date())
                                        .extentType(new Integer(1))
                                        .externalUserid(weOnTheJobCustomerVo.getExternalUserid())
                                        .handoverUserid(weOnTheJobCustomerVo.getHandoverUserId())
                                        .takeoverUserid(weOnTheJobCustomerVo.getTakeoverUserId())
                                        .failReason("在职继承")
                                        .build()
                        )
                );
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
        List<WeTag> lodAddTag = weMakeCustomerTag.getAddTag();
        if(CollectionUtil.isNotEmpty(addTag)){
            //校验是否有标签在库里不存在
            List<WeTag> weTagList = iWeTagService.list(
                    new LambdaQueryWrapper<WeTag>()
                            .in(WeTag::getTagId, addTag.stream().map(WeTag::getTagId).collect(Collectors.toList()))
            );
            if(CollectionUtil.isNotEmpty(weTagList)){
                if(addTag.size()!=weTagList.size()){
                    new WeComException("部门标签不存在");
                }
            }else{
                 new WeComException("部门标签不存在");
            }
            lodAddTag=weTagList;

            CutomerTagEdit cutomerTagEdit = CutomerTagEdit.builder()
                    .external_userid(weMakeCustomerTag.getExternalUserid())
                    .userid(weMakeCustomerTag.getUserId())
                    .add_tag(ArrayUtil.toArray(addTag.stream().map(WeTag::getTagId).collect(Collectors.toList()), String.class))
                    .build();

            //获取需要移除的标签
            List<WeFlowerCustomerTagRel> tagRels = iWeFlowerCustomerTagRelService.list(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                    .eq(WeFlowerCustomerTagRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                    .eq(WeFlowerCustomerTagRel::getUserId, weMakeCustomerTag.getUserId())
                    .eq(WeFlowerCustomerTagRel::getIsCompanyTag,weMakeCustomerTag.getIsCompanyTag())
                     .eq(WeFlowerCustomerTagRel::getDelFlag,WeConstans.WE_SUCCESS_CODE)
                    .notIn(WeFlowerCustomerTagRel::getTagId, addTag.stream().map(WeTag::getTagId).collect(Collectors.toList()))
            );
            if(CollectionUtil.isNotEmpty(tagRels)){
                //设置需要移除的标签
                if(iWeFlowerCustomerTagRelService.remove(
                        new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                                .eq(WeFlowerCustomerTagRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                                .eq(WeFlowerCustomerTagRel::getUserId, weMakeCustomerTag.getUserId())
                                .eq(WeFlowerCustomerTagRel::getIsCompanyTag,weMakeCustomerTag.getIsCompanyTag())
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
                                .isCompanyTag(weMakeCustomerTag.getIsCompanyTag())
                                .externalUserid(weMakeCustomerTag.getExternalUserid())
                                .tagId(k.getTagId())
                                .delFlag(WeConstans.WE_SUCCESS_CODE)
                                .build()
                );
            });

                iWeFlowerCustomerTagRelService.batchAddOrUpdate(newTagRels);

                if(weMakeCustomerTag.getIsCompanyTag()){
                    weCustomerClient.makeCustomerLabel(
                            cutomerTagEdit
                    );
                }





        }else{//为空，取消当前客户所有标签
            List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = iWeFlowerCustomerTagRelService.list(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                    .eq(WeFlowerCustomerTagRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                    .eq(WeFlowerCustomerTagRel::getUserId, weMakeCustomerTag.getUserId())
                    .eq(WeFlowerCustomerTagRel::getIsCompanyTag,weMakeCustomerTag.getIsCompanyTag()));
            if(CollectionUtil.isNotEmpty(weFlowerCustomerTagRels)){
                if(iWeFlowerCustomerTagRelService.remove(
                        new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                                .eq(WeFlowerCustomerTagRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                                .eq(WeFlowerCustomerTagRel::getUserId, weMakeCustomerTag.getUserId())
                                .eq(WeFlowerCustomerTagRel::getIsCompanyTag,weMakeCustomerTag.getIsCompanyTag()))){
                    if(weMakeCustomerTag.getIsCompanyTag()){
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


        iWeCustomerTrajectoryService.createTrajectory(
                ListUtil.toList(WeCustomerTrajectory.TrajectRel.builder()
                        .customerId(weMakeCustomerTag.getExternalUserid())
                        .userId(weMakeCustomerTag.getUserId())
                        .build()),TrajectoryType.TRAJECTORY_TYPE_SJDT.getType(),
                weMakeCustomerTag.getIsCompanyTag()?
                        TrajectorySceneType.TRAJECTORY_TITLE_GXQYBQ.getType():
                        TrajectorySceneType.TRAJECTORY_TITLE_GXGRBQ.getType(),
                CollectionUtil.isNotEmpty(lodAddTag)?
                String.join(",",lodAddTag.stream().map(WeTag::getName).collect(Collectors.toList())):"暂无标签",null);
    }



//    /**
//     * 根据员工ID获取客户
//     *
//     * @param externalUserid
//     * @return
//     */
//    @Override
//    public List<WeUser> getCustomersByUserId(String externalUserid) {
//        String userId=null;
//        if(Constants.USER_TYPE_WECOME.equals(SecurityUtils.getLoginUser().getUser().getUserType())){
//             userId=SecurityUtils.getLoginUser().getUser().getWeUserId();
//        }
//        return this.baseMapper.getCustomersByUserId(externalUserid,userId);
//    }


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
            weCustomer.setCustomerName(externalUserDetail.getExternal_contact().getName());
            weCustomer.setCustomerType(externalUserDetail.getExternal_contact().getType());
            weCustomer.setDelFlag(new Integer(0));

            List<ExternalUserDetail.FollowUser> follow_user = externalUserDetail.getFollow_user();
            if(CollectionUtil.isNotEmpty(follow_user)){
                ExternalUserDetail.FollowUser followUser = follow_user.stream().filter(e -> e.getUserid().equals(userId)).findFirst().get();

                if(null != followUser){
                    weCustomer.setState(followUser.getState());
                    weCustomer.setFirstAddTime(new Date(followUser.getCreatetime() * 1000L));
                    weCustomer.setAddMethod(followUser.getAddWay());


                    //设置标签
                    List<ExternalUserTag> tags = followUser.getTags();
                    if(CollectionUtil.isNotEmpty(tags)){
                        weCustomer.setTagIds(Joiner.on(",").join(tags));
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

                    this.baseMapper.batchAddOrUpdate(
                            ListUtil.toList(weCustomer)
                    );

                    //生成轨迹
                    iWeCustomerTrajectoryService.createTrajectory(ListUtil.toList(
                            WeCustomerTrajectory.TrajectRel.builder()
                                    .userId(userId)
                                    .customerId(externalUserid)
                                    .build()
                    ),TrajectoryType.TRAJECTORY_TYPE_XXDT.getType(),TrajectorySceneType.TRAJECTORY_TITLE_TJYG.getType(),
                            CustomerAddWay.of(followUser.getAddWay()).getVal()
                            ,null);



                }
            }
        }
    }



    @Override
    public void sendWelcomeMsg(WeWelcomeMsg weWelcomeMsg) {
        weCustomerClient.sendWelcomeMsg(weWelcomeMsg);
    }

    @Override
    public boolean updateCustomerChatStatus(String externalUserId) {

        return this.update(WeCustomer.builder()
                .isOpenChat(1)
                .build(),new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getExternalUserid,externalUserId));
    }





    @Override
    public WeCustomerPortrait findCustomerByOperUseridAndCustomerId(String externalUserid, String userid) throws Exception {


        WeCustomerPortrait weCustomerPortrait
                = this.baseMapper.findCustomerByOperUseridAndCustomerId(externalUserid, userid);

        if(null != weCustomerPortrait){
            if(weCustomerPortrait.getBirthday() != null){
                weCustomerPortrait.setAge(DateUtils.getAge(weCustomerPortrait.getBirthday()));
            }

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
        weCustomer.setOtherDescr(weCustomerPortrait.getDescription());


        if(this.update(weCustomer,new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getFirstUserId,weCustomerPortrait.getUserId())
                .eq(WeCustomer::getExternalUserid,weCustomerPortrait.getExternalUserid()))){


            iWeCustomerTrajectoryService.createTrajectory( ListUtil.toList(WeCustomerTrajectory.TrajectRel.builder()
                    .customerId(weCustomer.getExternalUserid())
                    .userId(weCustomer.getFirstUserId())
                    .build()),TrajectoryType.TRAJECTORY_TYPE_SJDT.getType(),TrajectorySceneType.TRAJECTORY_TITLE_BJBQ.getType(),null,null);
        }

//        //添加轨迹内容(信息动态)
//        iWeCustomerTrajectoryService.inforMationNews(weCustomerPortrait.getUserId(),weCustomerPortrait.getExternalUserid(), TrajectorySceneType.TRAJECTORY_TYPE_XXDT_BCZL.getKey());
    }



    @Override
    public WeCustomerDetail findWeCustomerDetail(String externalUserid,String userId,Integer delFlag) {
        WeCustomerDetail weCustomerDetail=new WeCustomerDetail();


        List<WeCustomerList> weCustomerList = this.findWeCustomerList(
                WeCustomerList.builder()
                        .externalUserid(externalUserid)
                        .delFlag(delFlag)
                        .build(),
                       null
        );
        if(CollectionUtil.isNotEmpty(weCustomerList)){
            BeanUtils.copyBeanProp(
                    weCustomerDetail,
                    weCustomerList.stream().findFirst().get()
                    );
            List<WeCustomerDetail.TrackUser> trackUsers=new ArrayList<>();

            weCustomerList.stream().forEach(k->{
                trackUsers.add(WeCustomerDetail.TrackUser.builder()
                                .addMethod(k.getAddMethod())
                                .trackUserId(k.getFirstUserId())
                                .firstAddTime(k.getFirstAddTime())
                                .trackState(k.getTrackState())
                                .userName(k.getUserName())
                        .build());
            });
            weCustomerDetail.setTrackUsers(
                    trackUsers
            );

            weCustomerDetail.setGroups(
                  this.baseMapper.findWecustomerGroups(externalUserid)
            );




        }


        return weCustomerDetail;
    }


    /**
     * 客户画像汇总
     * @param externalUserid
     * @return
     */
    @Override
    public WeCustomerDetail findWeCustomerInfoSummary(String externalUserid,String userId,Integer delFlag) {

        WeCustomerDetail weCustomerDetail=new WeCustomerDetail();

        List<WeCustomerList> weCustomerList = this.findWeCustomerList(WeCustomerList.builder()
                .externalUserid(externalUserid)
                        .userIds(userId)
                        .delFlag(delFlag)
                .build(),null);

        if(CollectionUtil.isNotEmpty(weCustomerList)){
            List<WeCustomerDetail.CompanyOrPersonTag> companyTags=new ArrayList<>();

            List<WeCustomerDetail.CompanyOrPersonTag> personTags=new ArrayList<>();

            List<WeCustomerDetail.TrackUser>  trackUsers=new ArrayList<>();

            List<WeCustomerDetail.TrackStates>  trackStates=new ArrayList<>();


            weCustomerList.stream().forEach(k->{

                //检索当前员工对应的客户有没有带跟进状态，如果没有插入
                List<WeCustomerTrajectory> weCustomerTrajectories = iWeCustomerTrajectoryService.list(new LambdaQueryWrapper<WeCustomerTrajectory>()
                        .eq(WeCustomerTrajectory::getExternalUserid, k.getExternalUserid())
                        .eq(WeCustomerTrajectory::getUserId, k.getFirstUserId())
                        .eq(WeCustomerTrajectory::getTrajectoryType, TrajectoryType.TRAJECTORY_TYPE_DBDT.getType())
                        .eq(WeCustomerTrajectory::getTrackState,TrackState.STATE_DGJ.getType()));

                if(CollectionUtil.isEmpty(weCustomerTrajectories)){
                    iWeCustomerTrajectoryService.save(
                            WeCustomerTrajectory.builder()
                                    .externalUserid(k.getExternalUserid())
                                    .trajectoryType(TrajectoryType.TRAJECTORY_TYPE_DBDT.getType())
                                    .userId(k.getFirstUserId())
                                    .trackState(TrackState.STATE_DGJ.getType())
                                    .trackTime(k.getFirstAddTime())
                                    .title("新增成员")
                                    .content("新增待跟进")
                                    .build()
                    );
                }



                trackStates.add(WeCustomerDetail.TrackStates
                        .builder()
                        .userName(k.getUserName())
                        .trackStateList(iWeCustomerTrajectoryService.list(new LambdaQueryWrapper<WeCustomerTrajectory>()
                                .eq(WeCustomerTrajectory::getExternalUserid, k.getExternalUserid())
                                .eq(WeCustomerTrajectory::getUserId, k.getFirstUserId())
                                .eq(WeCustomerTrajectory::getTrajectoryType, TrajectoryType.TRAJECTORY_TYPE_DBDT.getType())
                                .orderByAsc(WeCustomerTrajectory::getTrackTime)))
                        .build());

                trackUsers.add(
                        WeCustomerDetail.TrackUser.builder()
                                .userName(k.getUserName())
                                .addMethod(k.getAddMethod())
                                .trackState(k.getTrackState())
                                .trackUserId(k.getFirstUserId())
                                .firstAddTime(k.getFirstAddTime())
                                .build()
                );

                if(StringUtils.isNotEmpty(k.getTagIds()) && StringUtils.isNotEmpty(k.getTagNames())){
                    companyTags.add(
                            WeCustomerDetail.CompanyOrPersonTag.builder()
                                    .tagIds(k.getTagIds())
                                    .tagNames(k.getTagNames())
                                    .userName(k.getUserName())
                                    .build()
                    );
                }


                if(StringUtils.isNotEmpty(k.getPersonTagIds()) && StringUtils.isNotEmpty(k.getPersonTagNames())){
                    personTags.add(
                            WeCustomerDetail.CompanyOrPersonTag.builder()
                                    .tagIds(k.getPersonTagIds())
                                    .tagNames(k.getPersonTagNames())
                                    .userName(k.getUserName())
                                    .build()
                    );
                }


            });

            weCustomerDetail.setCompanyTags(companyTags);
            weCustomerDetail.setPersonTags(personTags);
            weCustomerDetail.setTrackUsers(trackUsers);
            weCustomerDetail.setTrackStates(trackStates);
        }







        return weCustomerDetail;
    }


    /**
     * 单个跟进人客户
     * @param externalUserid
     * @param userId
     * @return
     */
    @Override
    public WeCustomerDetail findWeCustomerInfoByUserId(String externalUserid, String userId,Integer delFlag) {

        WeCustomerDetail weCustomerDetail
                = this.findWeCustomerInfoSummary(externalUserid, userId,delFlag);

        WeCustomer weCustomer = this.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getExternalUserid, externalUserid)
                .eq(WeCustomer::getFirstUserId, userId));
        if(null != weCustomer){
            BeanUtils.copyBeanProp(weCustomerDetail,weCustomer);
        }

        List<WeCustomerAddGroup> groups
                = iWeGroupService.findWeGroupByCustomer(userId, externalUserid);
        if(CollectionUtil.isNotEmpty(groups)){
            List<WeCustomerAddGroup> commonGroup
                    = groups.stream().filter(e -> e.getCommonGroup().equals(new Integer(0))).collect(Collectors.toList());
            weCustomerDetail.setCommonGroupChat(
                  CollectionUtil.isNotEmpty(commonGroup)?
                          commonGroup.stream().map(WeCustomerAddGroup::getGroupName).collect(Collectors.joining(","))
                 :null
            );
        }

        return weCustomerDetail;
    }


    /**
     * 去重统计
     * @return
     */
    @Override
    public long noRepeatCountCustomer(WeCustomerList weCustomerList) {

        return this.baseMapper.noRepeatCountCustomer(weCustomerList);
    }


    /**
     * 客户跟进记录
     * @param trajectory
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrEditWaitHandle(WeCustomerTrajectory trajectory) {


            WeCustomer weCustomer = this.getOne(new LambdaQueryWrapper<WeCustomer>()
                    .eq(WeCustomer::getExternalUserid, trajectory.getExternalUserid())
                    .eq(WeCustomer::getFirstUserId, trajectory.getUserId()));
            if(weCustomer != null){
                weCustomer.setTrackState(trajectory.getTrackState());
                weCustomer.setTrackContent(trajectory.getContent());
                weCustomer.setTrackTime(new Date());
                if(this.update(weCustomer,new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getFirstUserId,
                                weCustomer.getFirstUserId())
                        .eq(WeCustomer::getExternalUserid,weCustomer.getExternalUserid()))){

                    iWeCustomerTrajectoryService.createTrajectory(
                            ListUtil.toList(WeCustomerTrajectory.TrajectRel.builder()
                                    .customerId(trajectory.getExternalUserid())
                                    .userId(trajectory.getUserId())
                                    .build()),TrajectoryType.TRAJECTORY_TYPE_DBDT.getType(),
                            TrajectorySceneType.TRAJECTORY_TITLE_TJGJ.getType(),trajectory.getContent(),trajectory.getTrackState());

                }
            }




    }


}
