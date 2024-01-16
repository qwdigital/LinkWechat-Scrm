package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.annotation.SynchRecord;
import com.linkwechat.common.constant.*;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.CustomerAddWay;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.enums.message.MessageTypeEnum;
import com.linkwechat.common.enums.*;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.customer.WeBacthMakeCustomerTag;
import com.linkwechat.domain.customer.WeMakeCustomerTag;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.query.WeOnTheJobCustomerQuery;
import com.linkwechat.domain.customer.vo.*;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.groupmsg.vo.WeGroupMessageExecuteUsertipVo;
import com.linkwechat.domain.wecom.entity.customer.WeCustomerFollowInfoEntity;
import com.linkwechat.domain.wecom.entity.customer.WeCustomerFollowUserEntity;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.customer.*;
import com.linkwechat.domain.wecom.query.customer.tag.WeMarkTagQuery;
import com.linkwechat.domain.wecom.query.customer.transfer.WeTransferCustomerQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.*;
import com.linkwechat.domain.wecom.vo.customer.transfer.WeTransferCustomerVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeCustomerMapper;
import com.linkwechat.service.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WeCustomerServiceImpl extends ServiceImpl<WeCustomerMapper, WeCustomer> implements IWeCustomerService {

    @Autowired
    private QwCustomerClient qwCustomerClient;


    @Autowired
    private IWeFlowerCustomerTagRelService iWeFlowerCustomerTagRelService;

    @Autowired
    private IWeTagService iWeTagService;

    @Autowired
    private IWeCustomerTrajectoryService iWeCustomerTrajectoryService;

    @Autowired
    private IWeAllocateCustomerService iWeAllocateCustomerService;


    @Autowired
    @Lazy
    private IWeGroupService iWeGroupService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IWeMessagePushService iWeMessagePushService;


    @Autowired
    private IWeCustomerSeasService iWeCustomerSeasService;


    @Autowired
    private IWeCustomerInfoExpandService iWeCustomerInfoExpandService;

    @Autowired
    @Lazy
    private IWeFissionService iWeFissionService;


    @Resource
    private IWeMessageNotificationService weMessageNotificationService;

    @Autowired
    private IWeCustomerLinkService iWeCustomerLinkService;


    @Override
    public List<WeCustomersVo> findWeCustomerList(WeCustomersQuery weCustomersQuery, PageDomain pageDomain) {
        List<WeCustomersVo> weCustomersVos = new ArrayList<>();
        List<String> ids = this.baseMapper.findWeCustomerListIds(weCustomersQuery, pageDomain);

        if (CollectionUtil.isNotEmpty(ids)) {
            weCustomersVos = this.baseMapper.findWeCustomerList(ids);
        }

        return weCustomersVos;
    }

    @Override
    public List<WeCustomersVo> findWeCustomerInfoFromWechat(List<String> externalUserids) {
        List<WeCustomersVo> weCustomersVos = new ArrayList<>();


        if (CollectionUtil.isNotEmpty(externalUserids)) {

            List<WeCustomer> weCustomers = this.list(new LambdaQueryWrapper<WeCustomer>()
                    .in(WeCustomer::getExternalUserid, externalUserids));


            externalUserids.stream().forEach(k -> {
                WeCustomersVo weCustomersVo = new WeCustomersVo();
                weCustomersVo.setCustomerName("@微信客户");
                if (CollectionUtil.isNotEmpty(weCustomers)) {
                    List<WeCustomer> weCustomerss
                            = weCustomers.stream().filter(item -> item.getExternalUserid().equals(k)).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(weCustomerss)) {

                        weCustomersVo.setCustomerName(weCustomerss.stream().findFirst().get().getCustomerName());
                    }
                }


                weCustomersVo.setExternalUserid(k);
                weCustomersVos.add(weCustomersVo);
            });


        }


        return weCustomersVos;
    }

    @Override
    public TableDataInfo<List<WeCustomersVo>> findWeCustomerListByApp(WeCustomersQuery weCustomersQuery, PageDomain pageDomain) {

        TableDataInfo<List<WeCustomersVo>> tableDataInfo = new TableDataInfo<>();
        tableDataInfo.setCode(HttpStatus.SUCCESS);
        if (weCustomersQuery.getDataScope()) {//这里指的全部数据,是角色的那些数据
            tableDataInfo.setRows(
                    this.findWeCustomerList(weCustomersQuery, pageDomain)
            );
            tableDataInfo.setTotal(
                    this.countWeCustomerList(weCustomersQuery)
            );
        } else {//个人数据
            weCustomersQuery.setFirstUserId(
                    SecurityUtils.getLoginUser().getSysUser().getWeUserId()
            );
            List<String> customerIds = this.baseMapper.findWeCustomerListIdsByApp(weCustomersQuery, pageDomain);
            if (CollectionUtil.isNotEmpty(customerIds)) {
                tableDataInfo.setRows(
                        this.baseMapper.findWeCustomerList(customerIds)
                );
                tableDataInfo.setTotal(
                        this.countWeCustomerListByApp(weCustomersQuery)
                );
            }
        }
        List<WeCustomersVo> rows = tableDataInfo.getRows();
        if (CollectionUtil.isEmpty(rows)) {
            tableDataInfo.setRows(new ArrayList<>());
        }

        return tableDataInfo;
    }

    @Override
    public long countWeCustomerList(WeCustomersQuery weCustomersQuery) {
        return this.baseMapper.countWeCustomerList(weCustomersQuery);
    }

    @Override
    public long countWeCustomerListByApp(WeCustomersQuery weCustomersQuery) {
        return this.baseMapper.countWeCustomerListByApp(weCustomersQuery);
    }

    @Override
    public long noRepeatCountCustomer(WeCustomersQuery weCustomersQuery) {
        return this.baseMapper.noRepeatCountCustomer(weCustomersQuery);
    }

    @Override
    @SynchRecord(synchType = SynchRecordConstants.SYNCH_CUSTOMER)
    public void synchWeCustomer() {

        LoginUser loginUser = SecurityUtils.getLoginUser();

        WeFollowUserListVo followUserList = qwCustomerClient.getFollowUserList(new WeBaseQuery()).getData();

        if (null != followUserList && CollectionUtil.isNotEmpty(followUserList.getFollowUser())) {

            List<List<String>> partition = ListUtil.partition(followUserList.getFollowUser(), 10);

            for(List<String> weUserIds:partition){
                loginUser.setWeUserIds(weUserIds);
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(), rabbitMQSettingConfig.getWeCustomerRk(), JSONObject.toJSONString(loginUser));

            }

        }


    }


    @Override
    public void synchWeCustomerHandler(String msg) {
        LoginUser loginUser = JSONObject.parseObject(msg, LoginUser.class);
        SecurityContextHolder.setCorpId(loginUser.getCorpId());
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setUserId(String.valueOf(loginUser.getSysUser().getUserId()));
        SecurityContextHolder.setUserType(loginUser.getUserType());
        List<String> weUserIds = loginUser.getWeUserIds();
        if(CollectionUtil.isNotEmpty(weUserIds)){
            //跟进人批量更新获取同步
            this.synchWeCustomerByAddIds(weUserIds);



        }



    }




    /**
     * 通过跟进人id同步客户
     *
     * @param followUserIds
     * @return
     */
    @Override
    public void synchWeCustomerByAddIds(List<String> followUserIds) {
        if (CollectionUtil.isNotEmpty(followUserIds)) {


            Map<String, SysUser> currentTenantSysUser = findCurrentTenantSysUser();

            this.getByUser(followUserIds, null,currentTenantSysUser);



        }
    }





    /**
     * 入库构建离职待分配客户
     *
     * @param followUserIds
     */
    @Override
    public void buildAllocateWecustomer(List<String> followUserIds) {
        this.synchWeCustomerByAddIds(followUserIds);
        this.saveOrUpdate(WeCustomer.builder()
                .addUserLeave(1)
                .build(), new LambdaQueryWrapper<WeCustomer>()
                .in(WeCustomer::getAddUserId, followUserIds));
    }

    private void getByUser(List<String> followUser, String nextCursor, Map<String, SysUser> currentTenantSysUser) {

        WeBatchCustomerDetailVo weBatchCustomerDetails = qwCustomerClient
                .getBatchCustomerDetail(new WeBatchCustomerQuery(followUser, nextCursor, 100)).getData();

        if (WeErrorCodeEnum.ERROR_CODE_0.getErrorCode().equals(weBatchCustomerDetails.getErrCode())
                || WeConstans.NOT_EXIST_CONTACT.equals(weBatchCustomerDetails.getErrCode())
                && ArrayUtil.isNotEmpty(weBatchCustomerDetails.getExternalContactList())) {

            this.weFlowerCustomerHandle(weBatchCustomerDetails.getExternalContactList(), currentTenantSysUser);


            if (StringUtils.isNotEmpty(weBatchCustomerDetails.getNext_cursor())) {
                getByUser(followUser, weBatchCustomerDetails.getNext_cursor(),currentTenantSysUser);
            }
        }

    }


    //通过单个客户id获取详情
    private void getBySingleUser(String externalUserid,String nextCursor,List<WeCustomerDetailVo> list){
        AjaxResult<WeCustomerDetailVo> customerDetail = qwCustomerClient.getCustomerDetail(WeCustomerQuery.builder().external_userid(externalUserid)
                .cursor(nextCursor).build());

        if(null != customerDetail){
            WeCustomerDetailVo weBatchCustomerDetails = customerDetail.getData();
            if (WeErrorCodeEnum.ERROR_CODE_0.getErrorCode().equals(weBatchCustomerDetails.getErrCode())
                    || weBatchCustomerDetails.getExternalContact() != null) {
                list.add(weBatchCustomerDetails);
                if (StringUtils.isNotEmpty(weBatchCustomerDetails.getNextCursor())) {
                    getBySingleUser(externalUserid, weBatchCustomerDetails.getNextCursor(), list);
                }
            }
        }

    }

    //客户同步业务处理,入库
    private void weFlowerCustomerHandle(List<WeCustomerDetailVo> details, Map<String, SysUser> currentTenantSysUser) {

        List<WeCustomer> weCustomerList = new ArrayList<>();

        List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();

        details.stream().forEach(k -> {

            WeCustomerDetailVo.ExternalContact externalContact = k.getExternalContact();

            WeCustomerFollowInfoEntity followInfo = k.getFollowInfo();

            if (null != followInfo && null != externalContact) {
                WeCustomer weCustomer = new WeCustomer();
                weCustomer.setId(SnowFlakeUtil.nextId());

                SysUser sysUser = currentTenantSysUser.get(followInfo.getUserId());

                if (null != sysUser) {
                    weCustomer.setCreateBy(sysUser.getUserName());
                    weCustomer.setCreateById(sysUser.getUserId());
                    weCustomer.setUpdateBy(sysUser.getUserName());
                    weCustomer.setUpdateById(sysUser.getUserId());
                }
                weCustomer.setCreateTime(new Date());
                weCustomer.setUpdateTime(new Date());
                weCustomer.setExternalUserid(externalContact.getExternalUserId());
                weCustomer.setCustomerName(externalContact.getName());
                weCustomer.setCustomerType(externalContact.getType());
                weCustomer.setAvatar(externalContact.getAvatar());
                weCustomer.setGender(externalContact.getGender());
                weCustomer.setUnionid(externalContact.getUnionId());
                weCustomer.setCorpName(StringUtils.isNotEmpty(externalContact.getCorpFullName())?externalContact.getCorpFullName():externalContact.getCorpName());
                weCustomer.setAddUserId(followInfo.getUserId());
                weCustomer.setAddTime(new Date(followInfo.getCreateTime() * 1000L));
                weCustomer.setAddMethod(followInfo.getAddWay());
                weCustomer.setState(followInfo.getState());
                weCustomer.setDelFlag(0);
                weCustomer.setRemarkName(followInfo.getRemark());
                weCustomer.setOtherDescr(followInfo.getDescription());
                weCustomer.setPhone(String.join(",", Optional.ofNullable(followInfo.getRemarkMobiles()).orElseGet(ArrayList::new)));

                List<String> tagIds = followInfo.getTagId();
                if (CollectionUtil.isNotEmpty(tagIds)) {
                    weCustomer.setTagIds(tagIds.stream().map(String::valueOf).collect(Collectors.joining(",")));

                    tagIds.stream().forEach(tagId -> {
                        WeFlowerCustomerTagRel weFlowerCustomerTagRel = WeFlowerCustomerTagRel.builder()
                                .id(SnowFlakeUtil.nextId())
                                .externalUserid(externalContact.getExternalUserId())
                                .tagId(tagId)
                                .userId(followInfo.getUserId())
                                .isCompanyTag(true)
                                .delFlag(0)
                                .build();
                        weFlowerCustomerTagRel.setCreateTime(new Date());
                        weFlowerCustomerTagRel.setUpdateTime(new Date());
                        if (null != sysUser) {
                            weFlowerCustomerTagRel.setCreateBy(sysUser.getUserName());
                            weFlowerCustomerTagRel.setCreateById(sysUser.getUserId());
                            weFlowerCustomerTagRel.setUpdateBy(sysUser.getUserName());
                            weFlowerCustomerTagRel.setUpdateById(sysUser.getUserId());
                        }
                        weFlowerCustomerTagRels.add(weFlowerCustomerTagRel);
                    });
                }else{
                    weCustomer.setTagIds(null);
                }
                weCustomerList.add(weCustomer);
            }



        });

        //添加客户标签
        if (CollectionUtil.isNotEmpty(weFlowerCustomerTagRels)) {
            List<List<WeFlowerCustomerTagRel>> tagRels = Lists.partition(weFlowerCustomerTagRels, 500);
            for (List<WeFlowerCustomerTagRel> tagRelss : tagRels) {
                iWeFlowerCustomerTagRelService.batchAddOrUpdate(tagRelss);
            }
        }
        //更新客户数据
        if (CollectionUtil.isNotEmpty(weCustomerList)) {
            List<List<WeCustomer>> partition = Lists.partition(weCustomerList, 500);
            for (List<WeCustomer> weCustomers : partition) {
                this.baseMapper.batchAddOrUpdate(weCustomers);
                List<WeCustomer> noTagWeCustomer
                        = weCustomers.stream().filter(item -> StringUtils.isEmpty(item.getTagIds())).collect(Collectors.toList());
                if(CollectionUtil.isNotEmpty(noTagWeCustomer)){
                    iWeFlowerCustomerTagRelService.removeConcatNowAddWeFlowerCustomerTagRel(noTagWeCustomer);
                }

                weCustomers.forEach(fWeCustomer ->{
                    iWeFissionService.handleTaskFissionRecord(fWeCustomer.getState(), fWeCustomer);
                } );
            }

            //更新已流失的客户数据
//            LambdaQueryWrapper<WeCustomer> wrapper = Wrappers.lambdaQuery(WeCustomer.class);
//            wrapper.select(WeCustomer::getExternalUserid, WeCustomer::getAddUserId);
//            wrapper.eq(WeCustomer::getDelFlag, Constants.COMMON_STATE);
//            wrapper.ne(WeCustomer::getTrackState, 5);
//            List<WeCustomer> list = this.list(wrapper);
//            List<WeCustomer> collect = weCustomerList.stream().map(i -> WeCustomer.builder().externalUserid(i.getExternalUserid()).addUserId(i.getAddUserId()).build()).collect(Collectors.toList());
//            if (list.removeAll(collect)) {
//                for (WeCustomer weCustomer : list) {
//                    LambdaUpdateWrapper<WeCustomer> updateWrapper = Wrappers.lambdaUpdate(WeCustomer.class);
//                    updateWrapper.eq(WeCustomer::getAddUserId, weCustomer.getAddUserId());
//                    updateWrapper.eq(WeCustomer::getExternalUserid, weCustomer.getExternalUserid());
//                    updateWrapper.set(WeCustomer::getTrackState, 5);
//                    this.update(updateWrapper);
//                }
//            }
        }
    }


    @Override
    @Transactional
    public void makeLabel(WeMakeCustomerTag weMakeCustomerTag) {

        //移除相关标签
        List<WeTag> removeTag = weMakeCustomerTag.getRemoveTag();

        if (CollectionUtil.isNotEmpty(removeTag)) {
            iWeFlowerCustomerTagRelService.remove(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                            .eq(WeFlowerCustomerTagRel::getUserId,weMakeCustomerTag.getUserId())
                            .eq(WeFlowerCustomerTagRel::getExternalUserid,weMakeCustomerTag.getExternalUserid())
                    .in(WeFlowerCustomerTagRel::getTagId, removeTag.stream().map(WeTag::getTagId).collect(Collectors.toList())));
            this.updateWeCustomerTagIds(weMakeCustomerTag.getUserId(), weMakeCustomerTag.getExternalUserid());
        }


        //新增的标签
        List<WeTag> addTag = weMakeCustomerTag.getAddTag();


        if (CollectionUtil.isNotEmpty(addTag)) {

            List<WeFlowerCustomerTagRel> tagRels = new ArrayList<>();
            addTag.stream().forEach(k -> {
                List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                        .eq(WeTag::getTagId, k.getTagId()));
                if(CollectionUtil.isEmpty(weTags)){
                    throw new WeComException("当前标签中存在已删除的标签");
                }


                WeFlowerCustomerTagRel weFlowerCustomerTagRel = WeFlowerCustomerTagRel.builder()
                        .id(SnowFlakeUtil.nextId())
                        .externalUserid(weMakeCustomerTag.getExternalUserid())
                        .userId(weMakeCustomerTag.getUserId())
                        .tagId(k.getTagId())
                        .isCompanyTag(weMakeCustomerTag.getIsCompanyTag())
                        .delFlag(Constants.COMMON_STATE)
                        .build();
                weFlowerCustomerTagRel.setUpdateTime(new Date());
                weFlowerCustomerTagRel.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
                tagRels.add(weFlowerCustomerTagRel);
            });
            iWeFlowerCustomerTagRelService.batchAddOrUpdate(tagRels);
        }

        if (weMakeCustomerTag.getIsCompanyTag()) {//企业标签,同步企业微信客户端

            WeMarkTagQuery cutomerTagEdit = WeMarkTagQuery.builder()
                    .external_userid(weMakeCustomerTag.getExternalUserid())
                    .userid(weMakeCustomerTag.getUserId())
                    .build();

            if (CollectionUtil.isNotEmpty(addTag)) {
                cutomerTagEdit.setAdd_tag(
                        addTag.stream().map(WeTag::getTagId).collect(Collectors.toList())
                );
            }

            if (CollectionUtil.isNotEmpty(removeTag)) {
                cutomerTagEdit.setRemove_tag(
                        removeTag.stream().map(WeTag::getTagId).collect(Collectors.toList())
                );
            }


            //发送消息客户标签同步企业微信端
            AjaxResult weResultVoAjaxResult = qwCustomerClient.makeCustomerLabel(
                    cutomerTagEdit
            );

            if (null != weResultVoAjaxResult && weResultVoAjaxResult.getCode() != 200) {
                log.error(weResultVoAjaxResult.getMsg());
                throw new WeComException("打标签失败，稍后请重实");
            } else {


                if (CollectionUtil.isNotEmpty(addTag) && !weMakeCustomerTag.isRecord()) {
                    iWeCustomerTrajectoryService.createEditTrajectory(weMakeCustomerTag.getExternalUserid(),
                            weMakeCustomerTag.getUserId(),
                            weMakeCustomerTag.getIsCompanyTag() ?
                                    TrajectorySceneType.TRAJECTORY_TITLE_GXQYBQ.getType() :
                                    TrajectorySceneType.TRAJECTORY_TITLE_GXGRBQ.getType(),
                            String.join(",", addTag.stream().map(WeTag::getName).collect(Collectors.toList()))
                    );
                } else {

                    if(CollectionUtil.isNotEmpty(removeTag)){
                        iWeCustomerTrajectoryService.createEditTrajectory(weMakeCustomerTag.getExternalUserid(),
                                weMakeCustomerTag.getUserId(),
                                weMakeCustomerTag.getIsCompanyTag() ?
                                        TrajectorySceneType.TRAJECTORY_TITLE_QXKHQYBQ.getType() :
                                        TrajectorySceneType.TRAJECTORY_TITLE_QXKHGRBQ.getType(),
                                String.join(",", removeTag.stream().map(WeTag::getName).collect(Collectors.toList()))
                        );
                    }
                }


            }


        }

        this.updateWeCustomerTagIds(weMakeCustomerTag.getUserId(), weMakeCustomerTag.getExternalUserid());


    }


    @Override
    @Transactional
    public void updateWeCustomerTagIds(String userId, String externalUserid) {
        WeCustomer weCustomer = this.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getAddUserId, userId)
                .eq(WeCustomer::getExternalUserid, externalUserid));
        if (null != weCustomer) {

            /**
             * 更新客户表标签ids冗余字段
             */
            List<WeFlowerCustomerTagRel> nowAddWeFlowerCustomerTagRel
                    = iWeFlowerCustomerTagRelService.findNowAddWeFlowerCustomerTagRel(externalUserid, userId);
            if (CollectionUtil.isNotEmpty(nowAddWeFlowerCustomerTagRel)) {
                weCustomer.setTagIds(
                        nowAddWeFlowerCustomerTagRel.stream().map(WeFlowerCustomerTagRel::getTagId).collect(Collectors.toList())
                                .stream().map(String::valueOf).collect(Collectors.joining(","))
                );
            } else {
                weCustomer.setTagIds(null);
            }
            this.updateById(weCustomer);
        }
    }


    @Override
    public void batchUpdateWeCustomerTagIds(List<WeCustomer> weCustomers){

        if(CollectionUtil.isNotEmpty(weCustomers)){
            List<WeFlowerCustomerTagRel> concatNowAddWeFlowerCustomerTagRel
                    = iWeFlowerCustomerTagRelService.findConcatNowAddWeFlowerCustomerTagRel(weCustomers);
            if(CollectionUtil.isNotEmpty(concatNowAddWeFlowerCustomerTagRel)){
                 weCustomers.stream().forEach(k->{
                     List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels
                             = concatNowAddWeFlowerCustomerTagRel.stream().filter(item -> item.getExternalUserid().equals(k.getExternalUserid())
                             && item.getUserId().equals(k.getAddUserId())).collect(Collectors.toList());

                     if(CollectionUtil.isNotEmpty(weFlowerCustomerTagRels)){
                          k.setTagIds(
                                  weFlowerCustomerTagRels.stream().map(WeFlowerCustomerTagRel::getTagId).collect(Collectors.toList())
                                          .stream().map(String::valueOf).collect(Collectors.joining(","))
                          );
                     }else{
                         k.setTagIds(null);
                     }
                 });
            }
            this.updateBatchById(weCustomers);
        }


    }

    @Override
    @Transactional
    public void allocateOnTheJobCustomer(WeOnTheJobCustomerQuery weOnTheJobCustomerQuery) {
        if (this.getOne(
                new LambdaQueryWrapper<WeCustomer>()
                        .eq(WeCustomer::getExternalUserid, weOnTheJobCustomerQuery.getExternalUserid())
                        .eq(WeCustomer::getAddUserId, weOnTheJobCustomerQuery.getTakeoverUserId())
                        .eq(WeCustomer::getDelFlag, Constants.COMMON_STATE)) != null) {

            throw new WeComException("当前接手人已经拥有此客户,不可继承");
        }

        List<WeAllocateCustomer> weAllocateCustomers = iWeAllocateCustomerService.list(new LambdaQueryWrapper<WeAllocateCustomer>()
                .eq(WeAllocateCustomer::getExternalUserid, weOnTheJobCustomerQuery.getExternalUserid())
                .between(WeAllocateCustomer::getAllocateTime, DateUtils.getBeforeByDayTime(-90)
                        , DateUtils.getBeforeByDayTime(0))
        );

        if (CollectionUtil.isNotEmpty(weAllocateCustomers)
                && weAllocateCustomers.size() >= 2) {
            throw new WeComException("当前客户在90个自然日内已被转接2次,暂时无法操作");
        }


        //获取当前需要被移交客户的相关消息
        WeCustomer weCustomer = this.getOne(
                new LambdaQueryWrapper<WeCustomer>()
                        .eq(WeCustomer::getExternalUserid, weOnTheJobCustomerQuery.getExternalUserid())
                        .eq(WeCustomer::getAddUserId, weOnTheJobCustomerQuery.getHandoverUserId())
                        .eq(WeCustomer::getDelFlag, Constants.COMMON_STATE)
        );

        if (null != weCustomer) {

            try {
                this.extentCustomer(weCustomer, weOnTheJobCustomerQuery);
            } catch (WeComException e) {
                throw e;
            }


        }

    }

    @Override
    public WeCustomerDetailInfoVo findWeCustomerDetail(String externalUserid, String userId, Integer delFlag) {
        WeCustomerDetailInfoVo weCustomerDetailInfoVo = new WeCustomerDetailInfoVo();

        List<WeCustomersVo> weCustomersVos = this.findWeCustomerList(
                WeCustomersQuery.builder()
                        .externalUserid(externalUserid)
                        .delFlag(delFlag)
                        .build(),
                null
        );


        if (CollectionUtil.isNotEmpty(weCustomersVos)) {
            BeanUtils.copyBeanProp(
                    weCustomerDetailInfoVo,
                    weCustomersVos.stream().findFirst().get()
            );
            List<WeCustomerDetailInfoVo.TrackUser> trackUsers = new ArrayList<>();

            weCustomersVos.stream().forEach(k -> {
                trackUsers.add(WeCustomerDetailInfoVo.TrackUser.builder()
                        .addMethod(k.getAddMethod())
                        .trackUserId(k.getFirstUserId())
                        .firstAddTime(k.getFirstAddTime())
                        .trackState(k.getTrackState())
                        .trackTime(k.getTrackTime())
                        .userName(k.getUserName())
                        .build());
            });
            weCustomerDetailInfoVo.setTrackUsers(
                    trackUsers
            );

            weCustomerDetailInfoVo.setGroups(
                    this.baseMapper.findWecustomerGroups(externalUserid)
            );


        }


        return weCustomerDetailInfoVo;

    }

    @Override
    public WeCustomerDetailInfoVo findWeCustomerInfoSummary(String externalUserid, String userId, Integer delFlag) {

        WeCustomerDetailInfoVo weCustomerDetail = new WeCustomerDetailInfoVo();

        List<WeCustomersVo> weCustomerList = this.findWeCustomerList(WeCustomersQuery.builder()
                .externalUserid(externalUserid)
                .userIds(userId)
                .delFlag(delFlag)
                .build(), null);

        if (CollectionUtil.isNotEmpty(weCustomerList)) {
            List<WeCustomerDetailInfoVo.CompanyOrPersonTag> companyTags = new ArrayList<>();

            List<WeCustomerDetailInfoVo.CompanyOrPersonTag> personTags = new ArrayList<>();

            weCustomerList.stream().forEach(weCustomersVo -> {

                if (StringUtils.isNotEmpty(weCustomersVo.getTagNames())) {
                    companyTags.add(
                            WeCustomerDetailInfoVo.CompanyOrPersonTag.builder()
                                    .tagNames(weCustomersVo.getTagNames())
                                    .userName(weCustomersVo.getUserName())
                                    .build()

                    );
                }

                if (StringUtils.isNotEmpty(weCustomersVo.getPersonTagNames())) {
                    personTags.add(
                            WeCustomerDetailInfoVo.CompanyOrPersonTag.builder()
                                    .tagNames(weCustomersVo.getPersonTagNames())
                                    .userName(weCustomersVo.getUserName())
                                    .build()

                    );
                }

            });

            weCustomerDetail.setCompanyTags(companyTags);
            weCustomerDetail.setPersonTags(personTags);
        }


        return weCustomerDetail;


    }

    @Override
    public WeCustomerDetailInfoVo findWeCustomerInfoByUserId(String externalUserid, String userId, Integer delFlag) {

        WeCustomerDetailInfoVo weCustomerDetail
                = this.findWeCustomerInfoSummary(externalUserid, userId, delFlag);


        WeCustomer weCustomer = this.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getExternalUserid, externalUserid)
                .eq(WeCustomer::getAddUserId, userId));
        if (null != weCustomer) {
            BeanUtils.copyBeanProp(weCustomerDetail, weCustomer);
        }

        List<WeCustomerAddGroupVo> groups
                = iWeGroupService.findWeGroupByCustomer(userId, externalUserid);
        if (CollectionUtil.isNotEmpty(groups)) {
            List<WeCustomerAddGroupVo> commonGroup
                    = groups.stream().filter(e -> e.getCommonGroup().equals(new Integer(0))).collect(Collectors.toList());
            weCustomerDetail.setCommonGroupChat(
                    CollectionUtil.isNotEmpty(commonGroup) ?
                            commonGroup.stream().map(WeCustomerAddGroupVo::getGroupName).collect(Collectors.joining(","))
                            : null
            );
        }

        //客户当前客户拓展字段
        if (weCustomer != null) {
            weCustomerDetail.setWeCustomerInfoExpands(
                    iWeCustomerInfoExpandService.list(new LambdaQueryWrapper<WeCustomerInfoExpand>()
                            .eq(WeCustomerInfoExpand::getCustomerId, weCustomer.getId()))
            );
        }


        return weCustomerDetail;

    }

    @Override
    public String findUserNameByUserId(String userId) {
        return this.baseMapper.findUserNameByUserId(userId);
    }

    @Override
    public WeCustomerPortraitVo findCustomerByOperUseridAndCustomerId(String externalUserid, String userid) throws Exception {
        WeCustomerPortraitVo weCustomerPortrait
                = this.baseMapper.findCustomerByOperUseridAndCustomerId(externalUserid, userid);
        //添加方式
        weCustomerPortrait.setAddMethodStr(CustomerAddWay.of(weCustomerPortrait.getAddMethod()).getVal());

        if (null != weCustomerPortrait) {
            if (weCustomerPortrait.getBirthday() != null) {
                weCustomerPortrait.setAge(DateUtils.getAge(weCustomerPortrait.getBirthday()));
            }

            //客户社交关系
            weCustomerPortrait.setSocialConn(
                    this.baseMapper.countSocialConn(externalUserid, userid)
            );

            //客户当前客户拓展字段
            weCustomerPortrait.setWeCustomerInfoExpands(
                    iWeCustomerInfoExpandService.list(new LambdaQueryWrapper<WeCustomerInfoExpand>()
                            .eq(WeCustomerInfoExpand::getCustomerId, weCustomerPortrait.getCustomerId()))
            );

        } else {
            weCustomerPortrait = new WeCustomerPortraitVo();
        }


        return weCustomerPortrait;
    }

    @Override
    @Transactional
    public void updateWeCustomerPortrait(WeCustomerPortraitVo weCustomerPortrait) {
        WeCustomer weCustomer
                = WeCustomer.builder().build();
        BeanUtils.copyBeanProp(weCustomer, weCustomerPortrait);
        //手机号
        weCustomer.setPhone(weCustomerPortrait.getRemarkMobiles());
        weCustomer.setCorpName(weCustomerPortrait.getRemarkCorpName());
        weCustomer.setAddUserId(weCustomerPortrait.getUserId());
        weCustomer.setOtherDescr(weCustomerPortrait.getOtherDescr());


        //相关备注信息同步企业微信
        UpdateCustomerRemarkQuery remarkQuery = new UpdateCustomerRemarkQuery();
        remarkQuery.setUserid(weCustomerPortrait.getUserId());
        remarkQuery.setExternal_userid(weCustomerPortrait.getExternalUserid());
        remarkQuery.setDescription(weCustomerPortrait.getOtherDescr());
        remarkQuery.setRemark_company(weCustomerPortrait.getRemarkCorpName());
        remarkQuery.setRemark_mobiles(ListUtil.toList(
                weCustomerPortrait.getRemarkMobiles()
        ));
        remarkQuery.setRemark(
                weCustomerPortrait.getCustomerFullName()
        );
        AjaxResult<WeResultVo> weResultVoAjaxResult = qwCustomerClient.updateCustomerRemark(remarkQuery);
        if (null != weResultVoAjaxResult) {
            WeResultVo weResultVo = weResultVoAjaxResult.getData();
            if (weResultVo != null && weResultVo.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)) {
                if (this.update(weCustomer, new LambdaQueryWrapper<WeCustomer>()
                        .eq(WeCustomer::getAddUserId, weCustomerPortrait.getUserId())
                        .eq(WeCustomer::getExternalUserid, weCustomerPortrait.getExternalUserid()))) {
                    //更新拓展字段
                    List<WeCustomerInfoExpand> weCustomerInfoExpands
                            = weCustomerPortrait.getWeCustomerInfoExpands();
                    if (CollectionUtil.isNotEmpty(weCustomerInfoExpands)) {
                        iWeCustomerInfoExpandService.saveOrUpdateBatch(
                                weCustomerInfoExpands
                        );
                    }

                    iWeCustomerTrajectoryService.createEditTrajectory(
                            weCustomer.getExternalUserid(), weCustomer.getAddUserId(), TrajectorySceneType.TRAJECTORY_TITLE_BJBQ.getType(), null
                    );
                }
            }
        } else {
            throw new WeComException("数据同步企微端失败,请稍后重试");
        }


    }

    @Override
    public List<WeCustomerAddUserVo> findWeUserByCustomerId(String externalUserid) {
        return this.baseMapper.findWeUserByCutomerId(externalUserid);
    }

    @Override
    @Transactional
    public void addOrEditWaitHandle(WeCustomerTrackRecord trajectory) {
        WeCustomer weCustomer = this.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getExternalUserid, trajectory.getExternalUserid())
                .eq(WeCustomer::getAddUserId, trajectory.getWeUserId()));
        if (weCustomer != null) {
            weCustomer.setTrackState(trajectory.getTrackState());
            weCustomer.setTrackContent(trajectory.getTrackContent());
            weCustomer.setTrackTime(new Date());
            if (this.update(weCustomer, new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getAddUserId,
                            weCustomer.getAddUserId())
                    .eq(WeCustomer::getExternalUserid, weCustomer.getExternalUserid()))) {
                iWeCustomerTrajectoryService.createTrackTrajectory(trajectory.getExternalUserid(), trajectory.getWeUserId(),
                        trajectory.getTrackState(), trajectory.getTrackContent());
            }
        }

    }

    //接替客户
    private void extentCustomer(WeCustomer weCustomer, WeOnTheJobCustomerQuery weOnTheJobCustomerQuery) throws WeComException {
        weCustomer.setTakeoverUserId(weOnTheJobCustomerQuery.getTakeoverUserId());

        if (this.update(
                weCustomer, new LambdaQueryWrapper<WeCustomer>()
                        .eq(WeCustomer::getExternalUserid, weCustomer.getExternalUserid())
                        .eq(WeCustomer::getAddUserId, weCustomer.getAddUserId()))) {
            AjaxResult<WeTransferCustomerVo> jobExtendsCustomer = qwCustomerClient.transferCustomer(
                    WeTransferCustomerQuery.builder()
                            .external_userid(
                                    ListUtil.toList(weOnTheJobCustomerQuery.getExternalUserid().split(","))
                            )
                            .handover_userid(weOnTheJobCustomerQuery.getHandoverUserId())
                            .takeover_userid(weOnTheJobCustomerQuery.getTakeoverUserId())
                            .build()
            );

            if (null != jobExtendsCustomer) {
                WeTransferCustomerVo transferCustomerVo = jobExtendsCustomer.getData();

                if (null != transferCustomerVo) {

                    List<WeTransferCustomerVo.TransferCustomerVo> transferCustomerVos
                            = transferCustomerVo.getCustomer();

                    if (CollectionUtil.isNotEmpty(transferCustomerVos)) {
                        WeTransferCustomerVo.TransferCustomerVo wtransferCustomerVo
                                = transferCustomerVos.stream().findFirst().get();
                        if (wtransferCustomerVo.getErrCode() != null
                                && WeConstans.WE_SUCCESS_CODE.equals(wtransferCustomerVo.getErrCode())) {
                            iWeAllocateCustomerService.batchAddOrUpdate(
                                    ListUtil.toList(
                                            WeAllocateCustomer.builder()
                                                    .id(SnowFlakeUtil.nextId())
                                                    .allocateTime(new Date())
                                                    .extentType(new Integer(1))
                                                    .externalUserid(weOnTheJobCustomerQuery.getExternalUserid())
                                                    .handoverUserid(weOnTheJobCustomerQuery.getHandoverUserId())
                                                    .takeoverUserid(weOnTheJobCustomerQuery.getTakeoverUserId())
                                                    .failReason("在职继承")
                                                    .build()
                                    )
                            );
                        } else {

                            throw new WeComException(WeErrorCodeEnum
                                    .parseEnum(wtransferCustomerVo.getErrCode()).getErrorMsg());

                        }


                    }

                }

            }
        }

    }


    @Override
    public void addCustomer(String externalUserId, String userId, String state) {
        //检索当前客户在库里面是否存在,该流失客户记录，如果存在则修改状态
        this.baseMapper.deleteWeCustomer(externalUserId, userId);
        //获取指定客户的详情
        WeCustomerQuery query = new WeCustomerQuery();
        query.setExternal_userid(externalUserId);
        WeCustomerDetailVo weCustomerDetail = qwCustomerClient.getCustomerDetail(query).getData();

        if (weCustomerDetail != null && weCustomerDetail.getExternalContact() != null) {
            WeCustomerDetailVo.ExternalContact externalContact = weCustomerDetail.getExternalContact();
            //客户入库
            WeCustomer weCustomer = new WeCustomer();
            weCustomer.setId(SnowFlakeUtil.nextId());
            weCustomer.setExternalUserid(externalContact.getExternalUserId());
            weCustomer.setCustomerName(externalContact.getName());
            weCustomer.setCustomerType(externalContact.getType());
            weCustomer.setAvatar(externalContact.getAvatar());
            weCustomer.setGender(externalContact.getGender());
            weCustomer.setUnionid(externalContact.getUnionId());
            weCustomer.setDelFlag(Constants.COMMON_STATE);
            weCustomer.setAddUserId(userId);
            weCustomer.setCreateTime(new Date());
            weCustomer.setUpdateTime(new Date());


            List<WeCustomerFollowUserEntity> followUserList = weCustomerDetail.getFollowUser();
            //先清理标签关系表中当前客户对应的原有标签数据
            if(StringUtils.isNotEmpty(weCustomer.getExternalUserid())
                    &&StringUtils.isNotEmpty(weCustomer.getAddUserId())){
                iWeFlowerCustomerTagRelService.remove(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                        .eq(WeFlowerCustomerTagRel::getExternalUserid,weCustomer.getExternalUserid())
                        .eq(WeFlowerCustomerTagRel::getUserId,weCustomer.getAddUserId()));
            }
            if (CollectionUtil.isNotEmpty(followUserList)) {
                WeCustomerFollowUserEntity followUserEntity = followUserList.stream().filter(followUserInfo -> followUserInfo.getUserId().equals(userId)).findFirst().get();

                weCustomer.setState(state);
                weCustomer.setAddTime(new Date(followUserEntity.getCreateTime() * 1000L));
                weCustomer.setAddMethod(followUserEntity.getAddWay());
                //如果添加方式为管理员分配,则属于在职继承客户新增回掉，删除当前客户上一级的继承人对应的数据
                if (CustomerAddWay.ADD_WAY_GLYFP.getKey().equals(followUserEntity.getAddWay())) {
                    this.remove(new LambdaQueryWrapper<WeCustomer>()
                            .eq(WeCustomer::getExternalUserid, externalUserId)
                            .eq(WeCustomer::getTakeoverUserId, userId));
                }

                weCustomer.setCorpName(followUserEntity.getRemarkCompany());
                weCustomer.setRemarkName(followUserEntity.getRemark());
                weCustomer.setOtherDescr(followUserEntity.getDescription());
                weCustomer.setPhone(String.join(",", Optional.ofNullable(followUserEntity.getRemarkMobiles()).orElseGet(ArrayList::new)));

                Map<String, SysUser> currentTenantSysUser = findCurrentTenantSysUser();

                if (CollectionUtil.isNotEmpty(currentTenantSysUser)) {
                    SysUser sysUser = currentTenantSysUser.get(followUserEntity.getUserId());

                    if (null != sysUser) {
                        weCustomer.setCreateBy(sysUser.getUserName());
                        weCustomer.setCreateById(sysUser.getUserId());
                        weCustomer.setUpdateBy(sysUser.getUserName());
                        weCustomer.setUpdateById(sysUser.getUserId());
                    }
                }

                //设置标签
                List<WeCustomerDetailVo.ExternalUserTag> tags = followUserEntity.getTags();
                if (CollectionUtil.isNotEmpty(tags)) {
                    List<WeFlowerCustomerTagRel> tagRels = tags.stream().map(tagInfo -> WeFlowerCustomerTagRel.builder()
                            .id(SnowFlakeUtil.nextId())
                            .externalUserid(externalContact.getExternalUserId())
                            .tagId(tagInfo.getTagId())
                            .userId(followUserEntity.getUserId())
                            .isCompanyTag(true)
                            .delFlag(0)
                            .build()).collect(Collectors.toList());
                    iWeFlowerCustomerTagRelService.batchAddOrUpdate(ListUtil.toList(tagRels));
                    weCustomer.setTagIds(
                            String.join(", ",
                                    tagRels.stream().map(WeFlowerCustomerTagRel::getTagId).collect(Collectors.toSet()))
                    );
                }
            }
            this.baseMapper.batchAddOrUpdate(ListUtil.toList(weCustomer));

            //添加方式为手机号搜索,更新客户公海中对应的状态
            if (CustomerAddWay.ADD_WAY_SSSJH.getKey().equals(weCustomer.getAddMethod())) {

                if (StringUtils.isNotEmpty(weCustomer.getPhone())) {
                    List<WeCustomerSeas> weCustomerSeasList = iWeCustomerSeasService.list(new LambdaQueryWrapper<WeCustomerSeas>()
                            .eq(WeCustomerSeas::getAddUserId, weCustomer.getAddUserId())
                            .eq(WeCustomerSeas::getPhone, weCustomer.getPhone()));
                    if (CollectionUtil.isNotEmpty(weCustomerSeasList)) {
                        weCustomerSeasList.stream().forEach(k -> k.setAddState(1));
                        iWeCustomerSeasService.updateBatchById(weCustomerSeasList);
                        //更新用户标签
                        WeCustomerSeas weCustomerSeas = weCustomerSeasList.stream().findFirst().get();
                        if (StringUtils.isNotEmpty(weCustomerSeas.getTagIds())) {
                            List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                                    .in(WeTag::getTagId, ListUtil.toList(weCustomerSeas.getTagIds().split(","))));
                            if (CollectionUtil.isNotEmpty(weTags)) {
                                makeLabel(WeMakeCustomerTag.builder()
                                        .userId(weCustomerSeas.getAddUserId())
                                        .isCompanyTag(true)
                                        .externalUserid(externalUserId)
                                        .addTag(weTags)
                                        .build());
                            }
                        }

                    }


                }


            }

            //添加方式为获客助手
            if(CustomerAddWay.ADD_WAY_HKZS.getKey().equals(weCustomer.getAddMethod())){
                if(StringUtils.isNotEmpty(weCustomer.getState())){
                    WeCustomerLink weCustomerLink = iWeCustomerLinkService
                            .getById(StringUtils.substringAfter(weCustomer.getState(),
                                    WelcomeMsgTypeEnum.WE_CUSTOMER_LINK_PREFIX.getType()));
                    if(null != weCustomerLink && StringUtils.isNotEmpty(weCustomerLink.getTagIds())){


                        List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                                .in(WeTag::getTagId, ListUtil.toList(weCustomerLink.getTagIds().split(","))));
                        if(CollectionUtil.isNotEmpty(weTags)){
                            this.makeLabel( WeMakeCustomerTag.builder()
                                    .isCompanyTag(true)
                                    .addTag(weTags)
                                    .userId(userId)
                                    .externalUserid(externalUserId)
                                    .build());
                        }

                    }


                }


            }

            iWeFissionService.handleTaskFissionRecord(state, weCustomer);




            //生成轨迹
            iWeCustomerTrajectoryService.createAddOrRemoveTrajectory(externalUserId, userId, true, true);
            //为被添加员工发送一条消息提醒
            iWeMessagePushService.pushMessageSelfH5(ListUtil.toList(userId), "【客户动态】<br/><br/> 客户@" + weCustomer.getCustomerName() + "刚刚添加了您", MessageNoticeType.ADDCUTOMER.getType(), false);

            //添加消息通知
            weMessageNotificationService.save(MessageTypeEnum.CUSTOMER.getType(),userId, MessageConstants.CUSTOMER_ADD,weCustomer.getCustomerName());


            //通知新客sop
            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getSopEx(), rabbitMQSettingConfig.getNewWeCustomerSopRk(),
                    JSONObject.toJSONString(weCustomer));

        }
    }

    @Override
    public WeCustomer updateCustomer(String externalUserId, String userId) {
        //客户入库
        WeCustomer weCustomer = new WeCustomer();

        //获取指定客户的详情
        WeCustomerQuery query = new WeCustomerQuery();
        query.setExternal_userid(externalUserId);
        WeCustomerDetailVo weCustomerDetail = qwCustomerClient.getCustomerDetail(query).getData();

        if (weCustomerDetail != null && weCustomerDetail.getExternalContact() != null) {

            WeCustomerDetailVo.ExternalContact externalContact = weCustomerDetail.getExternalContact();
            //客户入库
            weCustomer = new WeCustomer();
            weCustomer.setId(SnowFlakeUtil.nextId());
            weCustomer.setExternalUserid(externalContact.getExternalUserId());
            weCustomer.setCustomerName(externalContact.getName());
            weCustomer.setCustomerType(externalContact.getType());
            weCustomer.setAvatar(externalContact.getAvatar());
            weCustomer.setGender(externalContact.getGender());
            weCustomer.setUnionid(externalContact.getUnionId());
            weCustomer.setDelFlag(0);
            weCustomer.setAddUserId(userId);

            List<WeCustomerFollowUserEntity> followUserList = weCustomerDetail.getFollowUser();
            if (CollectionUtil.isNotEmpty(followUserList)) {
                WeCustomerFollowUserEntity followUserEntity = followUserList.stream().filter(followUserInfo -> followUserInfo.getUserId().equals(userId)).findFirst().get();

                weCustomer.setState(followUserEntity.getState());
                weCustomer.setAddTime(new Date(followUserEntity.getCreateTime() * 1000L));
                weCustomer.setAddMethod(followUserEntity.getAddWay());
                weCustomer.setCorpName(followUserEntity.getRemarkCompany());
                weCustomer.setRemarkName(followUserEntity.getRemark());
                weCustomer.setOtherDescr(followUserEntity.getDescription());
                weCustomer.setPhone(String.join(",", Optional.ofNullable(followUserEntity.getRemarkMobiles()).orElseGet(ArrayList::new)));
                //设置标签
                List<WeCustomerDetailVo.ExternalUserTag> tags = followUserEntity.getTags();

                iWeFlowerCustomerTagRelService.remove(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                        .eq(WeFlowerCustomerTagRel::getExternalUserid,externalUserId)
                        .eq(WeFlowerCustomerTagRel::getUserId,userId));

                if (CollectionUtil.isNotEmpty(tags)) {
                    weCustomer.setTagIds(StringUtils.join(tags.stream().map(WeCustomerDetailVo.ExternalUserTag::getTagId)
                            .collect(Collectors.toList()),","));
                    List<WeFlowerCustomerTagRel> tagRels = tags.stream().map(tagInfo -> WeFlowerCustomerTagRel.builder()
                            .id(SnowFlakeUtil.nextId())
                            .externalUserid(externalContact.getExternalUserId())
                            .tagId(tagInfo.getTagId())
                            .userId(followUserEntity.getUserId())
                            .isCompanyTag(true)
                            .delFlag(0)
                            .build()).collect(Collectors.toList());
                    iWeFlowerCustomerTagRelService.batchAddOrUpdate(ListUtil.toList(tagRels));
                } else {

                    iWeFlowerCustomerTagRelService.remove(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                            .eq(WeFlowerCustomerTagRel::getExternalUserid, externalUserId)
                            .eq(WeFlowerCustomerTagRel::getUserId, userId));
                    this.updateWeCustomerTagIds(userId,externalUserId);
                }

            }
            this.baseMapper.batchAddOrUpdate(ListUtil.toList(weCustomer));
            //生成轨迹
            iWeCustomerTrajectoryService.createEditTrajectory(
                    weCustomer.getExternalUserid(), weCustomer.getAddUserId(), TrajectorySceneType.TRAJECTORY_TITLE_BJBQ.getType(), null
            );

        }

        return weCustomer;
    }

    @Override
    public Map<String, SysUser> findCurrentTenantSysUser() {
        Map<String, SysUser> sysUserMap = new HashMap<>();
        List<SysUser> sysUsers = this.baseMapper.findCurrentTenantSysUser();
        if (CollectionUtil.isNotEmpty(sysUsers)) {
            sysUserMap = sysUsers.stream().collect(Collectors.toMap(SysUser::getWeUserId, Function.identity(), (key1, key2) -> key2));
        }
        return sysUserMap;
    }

    @Override
    public SysUser findSysUserInfoByWeUserId(String weUserId) {
        return this.baseMapper.findSysUserInfoByWeUserId(weUserId);
    }

    @Override
    public SysUser findCurrentSysUserInfo(Long userId) {
        return this.baseMapper.findCurrentSysUserInfo(userId);
    }

    @Async
    @Override
    public void updateCustomerUnionId(String unionId) {
        List<WeCustomer> list = list(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getUnionid, unionId).eq(WeCustomer::getDelFlag, 0));
        if (CollectionUtil.isEmpty(list)) {
            UnionidToExternalUserIdQuery query = new UnionidToExternalUserIdQuery(SecurityUtils.getCorpId(), SecurityUtils.getWxLoginUser().getOpenId(), unionId);
            UnionidToExternalUserIdVo externalUserIdVo = qwCustomerClient.unionIdToExternalUserId3rd(query).getData();
            if (externalUserIdVo != null && CollectionUtil.isNotEmpty(externalUserIdVo.getExternalUseridInfo())) {
                List<UnionidToExternalUserIdVo.UnionIdToExternalUserIdList> externalUseridInfo = externalUserIdVo.getExternalUseridInfo();
                String externaklUserId = externalUseridInfo.stream().filter(item -> ObjectUtil.equal(SecurityUtils.getCorpId(), item.getCorpId())).map(UnionidToExternalUserIdVo.UnionIdToExternalUserIdList::getExternalUserId).findFirst().orElseGet(null);
                WeCustomer weCustomer = new WeCustomer();
                weCustomer.setUnionid(unionId);
                update(weCustomer, new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getExternalUserid, externaklUserId).eq(WeCustomer::getDelFlag, 0));
            }
        }
    }

    @Override
    public List<WeCustomer> getCustomerListByCondition(WeCustomersQuery query) {
        return this.baseMapper.getCustomerListByCondition(query);
    }


    @Override
    public List<SysUser> findAllSysUser() {
        List<SysUser> allSysUser = this.baseMapper.findAllSysUser();
        if (CollectionUtil.isEmpty(allSysUser)) {
            allSysUser = new ArrayList<>();
        }
        return allSysUser;
    }

    @Override
    public List<String> findWeUserIds() {
        return this.baseMapper.findWeUserIds();
    }

    @Override
    public List<WeCustomersVo> findWeCustomerList(List<String> customerIds) {
        return this.baseMapper.findWeCustomerList(customerIds);
    }


    @Override
    public WeCustomer findOrSynchWeCustomer(String externalUserid) {
        List<WeCustomer> weCustomerList = this.list(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getExternalUserid, externalUserid));
        if (CollectionUtil.isEmpty(weCustomerList)) {

            return this.updateCustomer(externalUserid, null);

        }

        return weCustomerList.stream().findFirst().get();
    }

    @Override
    @Transactional
    public void batchMakeLabel(WeBacthMakeCustomerTag makeCustomerTags) {



        List<WeMakeCustomerTag> weMakeCustomerTagList = makeCustomerTags.getWeMakeCustomerTagList();

        if(CollectionUtil.isNotEmpty(weMakeCustomerTagList)){
            weMakeCustomerTagList.stream().forEach(kk->{
                this.makeLabel(kk);
            });

        }

    }


    @Override
    public List<WeCustomerChannelCountVo> countCustomerChannel(String state, String startTime, String endTime, Integer delFlag) {
        return this.baseMapper.countCustomerChannel(state, startTime, endTime, delFlag);
    }

    @Override
    public Integer totalScanCodeNumber(String state) {
        return this.baseMapper.totalScanCodeNumber(state);
    }

    @Override
    public List<WeCustomersVo> findWeCustomersForCommonAssembly(WeGroupMessageExecuteUsertipVo executeUserOrGroup) {


        WeCustomersQuery weCustomersQuery = WeCustomersQuery.builder()
                .delFlag(Constants.COMMON_STATE)
                .build();

        if (executeUserOrGroup != null) {
            weCustomersQuery.setUserIds(
                    executeUserOrGroup.getWeUserIds()
            );
            weCustomersQuery.setGender(
                    executeUserOrGroup.getGender()
            );


            if (executeUserOrGroup.getBeginTime() != null) {
                weCustomersQuery.setBeginTime(
                        DateUtils.dateTime(executeUserOrGroup.getBeginTime())
                );
            }


            weCustomersQuery.setTagIds(
                    executeUserOrGroup.getTagIds()
            );

            if (executeUserOrGroup.getEndTime() != null) {
                weCustomersQuery.setEndTime(
                        DateUtils.dateTime(executeUserOrGroup.getEndTime())
                );
            }

            weCustomersQuery.setTrackState(
                    executeUserOrGroup.getTrackState()
            );

        }


        return this.findWeCustomerList(
                weCustomersQuery, null
        );

    }

    @Override
    public List<WeCustomerSimpleInfoVo> getCustomerSimpleInfo(List<String> externalUserIds) {
        return this.baseMapper.getCustomerSimpleInfo(externalUserIds);
    }

    @Override
    public List<WeCustomersVo> findLimitWeCustomerList() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(1);
        pageDomain.setPageSize(10000);
        WeCustomersQuery weCustomersQuery = new WeCustomersQuery();
        weCustomersQuery.setDelFlag(Constants.COMMON_STATE);
        weCustomersQuery.setIsJoinBlacklist(1);
        return  this.findWeCustomerList(weCustomersQuery,pageDomain);
    }


    @Override
    public List<WeCustomersVo> findLimitWeCustomerList(WeCustomersQuery weCustomersQuery) {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(1);
        pageDomain.setPageSize(10000);
        weCustomersQuery.setDelFlag(Constants.COMMON_STATE);
        weCustomersQuery.setIsJoinBlacklist(1);
        return  this.findWeCustomerList(weCustomersQuery,pageDomain);
    }

    @Override
    public List<WeAddGroupMessageQuery.SenderInfo> findLimitSenderInfoWeCustomerList() {
        List<WeAddGroupMessageQuery.SenderInfo> senderInfos=new ArrayList<>();
        List<WeCustomersVo> limitWeCustomerList = this.findLimitWeCustomerList();
        if (CollectionUtil.isNotEmpty(limitWeCustomerList)) {
            Map<String, List<WeCustomersVo>> customerMap = limitWeCustomerList.stream().collect(Collectors.groupingBy(WeCustomersVo::getFirstUserId));
            customerMap.forEach((userId, customers) -> {
                List<String> eids = customers.stream().map(WeCustomersVo::getExternalUserid).collect(Collectors.toList());
                WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
                senderInfo.setCustomerList(eids);
                senderInfo.setUserId(userId);
                senderInfos.add(senderInfo);
            });
        } else {
            throw new WeComException("暂无客户可发送");
        }

        return senderInfos;
    }
    @Override
    public void makeTagWeCustomer(String exId, List<WeTag> weTags) {
        List<WeCustomer> weCustomers = this.list(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getExternalUserid, exId));
        if(CollectionUtil.isNotEmpty(weCustomers)){
            weCustomers.stream().forEach(weCustomer -> {
                WeMakeCustomerTag weMakeCustomerTag=new WeMakeCustomerTag();
                weMakeCustomerTag.setExternalUserid(exId);
                weMakeCustomerTag.setUserId(weCustomer.getAddUserId());
                weMakeCustomerTag.setAddTag(weTags);
                weMakeCustomerTag.setIsCompanyTag(true);
                weMakeCustomerTag.setRecord(false);
                weMakeCustomerTag.setSource(false);
                this.makeLabel(weMakeCustomerTag);

            });


        }

    }

    @Override
    public List<WeCustomerChannelCountVo> getCustomerNumByState(String state, Date startTime, Date endTime) {
        return baseMapper.getCustomerNumByState(state,startTime,endTime);
    }


}
