package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.DateUtils;
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
import com.linkwechat.wecom.domain.vo.WeCustomerDetailVo;
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


    @Autowired
    private  IWeCustomerTrajectoryService iWeCustomerTrajectoryService;


//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean saveOrUpdate(WeCustomer weCustomer) {
//        if (weCustomer.getExternalUserid() != null) {
//            WeCustomer weCustomerBean = selectWeCustomerById(weCustomer.getExternalUserid());
//            if (weCustomerBean != null) {
//                return weCustomerMapper.updateWeCustomer(weCustomer) == 1;
//            } else {
//                return weCustomerMapper.insertWeCustomer(weCustomer) == 1;
//            }
//        }
//        return false;
//    }

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
        FollowUserList followUserList = weCustomerClient.getFollowUserList();
        if (WeConstans.WE_SUCCESS_CODE.equals(followUserList.getErrcode())
                && ArrayUtil.isNotEmpty(followUserList.getFollow_user())) {
            Arrays.asList(followUserList.getFollow_user())
                    .parallelStream().forEach(k -> {
                try {
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

        List<WeCustomer> weCustomerList=new ArrayList<>();

        List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
        List<WeFlowerCustomerRel> weFlowerCustomerRel = new ArrayList<>();

        List<WeFlowerCustomerRel> oldWeFlowerCustomerRels =new ArrayList<>();



        list.forEach(userDetail -> {
            //客户入库
            WeCustomer weCustomer = new WeCustomer();
            BeanUtils.copyPropertiesignoreOther(userDetail.getExternal_contact(), weCustomer);


            Optional.ofNullable(userDetail.getFollow_info()).ifPresent(followInfo -> {

                weCustomer.setFirstUserId(userId);
                weCustomer.setFirstAddTime(new Date(followInfo.getCreatetime() * 1000L));

                weCustomerList.add(weCustomer);

                Long weFlowerCustomerRelId = SnowFlakeUtil.nextId();
                weFlowerCustomerRel.add(WeFlowerCustomerRel.builder()
                        .id(weFlowerCustomerRelId)
                        .userId(userId)
                        .operUserid(followInfo.getOper_userid())
                        .addWay(followInfo.getAdd_way())
                        .externalUserid(weCustomer.getExternalUserid())
                        .createTime(new Date(followInfo.getCreatetime() * 1000L))
                        .build());

                List<String> tags = Stream.of(followInfo.getTag_id()).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(tags)) {

                    tags.stream().forEach(tag->{
                        weFlowerCustomerTagRels.add(
                                WeFlowerCustomerTagRel.builder()
                                        .userId(userId)
                                        .externalUserid(weCustomer.getExternalUserid())
                                        .flowerCustomerRelId(weFlowerCustomerRelId)
                                        .tagId(tag)
                                        .build()
                        );
                    });
                }
            });


            oldWeFlowerCustomerRels.addAll(iWeFlowerCustomerRelService.list(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                    .eq(WeFlowerCustomerRel::getExternalUserid, weCustomer.getExternalUserid())
                    .eq(WeFlowerCustomerRel::getUserId,userId)));

        });



        if (CollectionUtil.isNotEmpty(oldWeFlowerCustomerRels)) {
            List<Long> weFlowerCustomerRelIds = oldWeFlowerCustomerRels.stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList());
            iWeFlowerCustomerTagRelService.remove(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                    .in(WeFlowerCustomerTagRel::getFlowerCustomerRelId, weFlowerCustomerRelIds)
            );
            iWeFlowerCustomerRelService.removeByIds(weFlowerCustomerRelIds);
        }


        if(CollectionUtil.isNotEmpty(weFlowerCustomerRel)){
            iWeFlowerCustomerRelService.saveBatch(
                    weFlowerCustomerRel.stream().collect(
                            Collectors.collectingAndThen(
                                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WeFlowerCustomerRel::getExternalUserid)
                                            .thenComparing(WeFlowerCustomerRel::getUserId)
                                    )), ArrayList::new)
                    )
            );
        }


        if(CollectionUtil.isNotEmpty(weFlowerCustomerTagRels)){
            iWeFlowerCustomerTagRelService.saveOrUpdateBatch(weFlowerCustomerTagRels);
        }


        if(CollectionUtil.isNotEmpty(weCustomerList)){

            //移除不存在的客户
            this.remove(new LambdaQueryWrapper<WeCustomer>()
                    .notIn(WeCustomer::getExternalUserid,weCustomerList.stream().map(WeCustomer::getExternalUserid).collect(Collectors.toList()))
            );
            //移除，同一个客户不同首位添加人id客户，保留最早时间客户
            this.saveOrUpdateBatch(
                    weCustomerList.stream().collect(
                            Collectors.collectingAndThen(
                                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WeCustomer::getExternalUserid))), ArrayList::new)
                    )
            );
        }




    }


//    public static void main(String[] args) {
//
//
//        List<WeCustomer> weCustomerList=new ArrayList<>();
//
//
//        weCustomerList.add(
//                WeCustomer.builder()
//                        .externalUserid("abcdef")
//                        .firstUserId("1234")
//                        .firstAddTime(DateUtil.lastWeek())
//                        .build()
//        );
//
//        weCustomerList.add(
//                WeCustomer.builder()
//                        .externalUserid("abcdef")
//                        .firstUserId("4321")
//                        .firstAddTime(DateUtil.nextWeek())
//                        .build()
//        );
//
//        weCustomerList.stream().filter((o1,o2)->)
//
//        ArrayList<WeCustomer> collect = weCustomerList.stream().collect(
//                Collectors.collectingAndThen(
//                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WeCustomer::getExternalUserid)
//                                .thenComparing(WeCustomer::getFirstAddTime)
//                        )), ArrayList::new)
//        );
//
//        System.out.println("=============");
//
//
//
//    }

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
        List<WeTag> addTags = weMakeCustomerTag.getAddTag();


        if(CollectionUtil.isNotEmpty(addTags)){

            if(StringUtils.isEmpty(weMakeCustomerTag.getUserId())){
                //设置首位添加人打标签
                WeCustomer weCustomer
                        = this.getById(weMakeCustomerTag.getExternalUserid());
                if(null != weCustomer){
                    weMakeCustomerTag.setUserId(weCustomer.getFirstUserId());
                }
            }


            WeFlowerCustomerRel weFlowerCustomerRel = iWeFlowerCustomerRelService.getOne(new LambdaQueryWrapper<WeFlowerCustomerRel>()
                    .eq(WeFlowerCustomerRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                    .eq(WeFlowerCustomerRel::getUserId, weMakeCustomerTag.getUserId())
            );


            if(weFlowerCustomerRel != null){
                weMakeCustomerTag.setFlowerCustomerRelId(weFlowerCustomerRel.getId());
            }



            List<WeFlowerCustomerTagRel> tagRels = new ArrayList<>();


            List<WeFlowerCustomerTagRel> tagRelList = iWeFlowerCustomerTagRelService.list(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                    .eq(WeFlowerCustomerTagRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
                    .eq(WeFlowerCustomerTagRel::getUserId,weMakeCustomerTag.getUserId()));

            CutomerTagEdit cutomerTagEdit = CutomerTagEdit.builder()
                    .userid(weMakeCustomerTag.getUserId())
                    .external_userid(weMakeCustomerTag.getExternalUserid())
                    .add_tag(ArrayUtil.toArray(addTags.stream().map(WeTag::getTagId).collect(Collectors.toList()), String.class))
                    .build();

            if(CollectionUtil.isNotEmpty(tagRelList)){

                cutomerTagEdit.setRemove_tag(ArrayUtil.toArray(tagRelList.stream().map(WeFlowerCustomerTagRel::getTagId).collect(Collectors.toList())
                ,String.class));

                iWeFlowerCustomerTagRelService.removeByIds(
                        tagRelList.stream().map(WeFlowerCustomerTagRel::getId).collect(Collectors.toList())
                );
            }


            //后面在该表加一个字段标准一下标签关系类型，企业标签和企业微信类型标签
            addTags.stream().forEach(k->{
                tagRels.add(
                        WeFlowerCustomerTagRel.builder()
                                .flowerCustomerRelId(SnowFlakeUtil.nextId())
                                .externalUserid(weMakeCustomerTag.getExternalUserid())
                                .flowerCustomerRelId(weMakeCustomerTag.getFlowerCustomerRelId())
                                .userId(weMakeCustomerTag.getUserId())
                                .relTagType(new Integer(1))
                                .tagId(k.getTagId())
                                .build()
                );
            });
            iWeFlowerCustomerTagRelService.saveOrUpdateBatch(tagRels);


            //标签同步企业微信端
            if(StringUtils.isNotEmpty(cutomerTagEdit.getUserid())
            &&StringUtils.isNotEmpty(cutomerTagEdit.getExternal_userid())){
                weCustomerClient.makeCustomerLabel(cutomerTagEdit);
            }

        }


//        LambdaQueryWrapper<WeFlowerCustomerRel> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(WeFlowerCustomerRel::getExternalUserid, weMakeCustomerTag.getExternalUserid());
//
//        if(StrUtil.isNotBlank(weMakeCustomerTag.getUserId())){
//            wrapper.eq(WeFlowerCustomerRel::getUserId,weMakeCustomerTag.getUserId());
//        }

//        //查询出首位添加人当前用户对应的
//        List<WeFlowerCustomerRel> flowerCustomerRels = iWeFlowerCustomerRelService.list(
//               new LambdaQueryWrapper<WeFlowerCustomerRel>()
//                       .eq(WeFlowerCustomerRel::getExternalUserid, weMakeCustomerTag.getExternalUserid())
//                       .eq(WeFlowerCustomerRel::getUserId,weMakeCustomerTag.getUserId())
//        );
//        if (CollectionUtil.isNotEmpty(flowerCustomerRels)) {
//
//
//            List<WeTag> addTags = weMakeCustomerTag.getAddTag();
//
//
//            if (CollectionUtil.isNotEmpty(addTags)) {
//                addTags.removeAll(Collections.singleton(null));
//
//                //移除重复标签(避免客户重复打标签)
//                this.removeLabel(WeMakeCustomerTag.builder()
//                        .externalUserid(weMakeCustomerTag.getExternalUserid())
//                        .addTag(addTags)
//                        .build());
//
//                List<WeFlowerCustomerTagRel> tagRels = new ArrayList<>();
//
//                List<CutomerTagEdit> cutomerTagEdits = new ArrayList<>();
//
//                flowerCustomerRels.stream().forEach(customer -> {
//                    CutomerTagEdit cutomerTagEdit = CutomerTagEdit.builder()
//                            .userid(customer.getUserId())
//                            .external_userid(customer.getExternalUserid())
//                            .build();
//
//                    List<String> tags = new ArrayList<>();
//                    addTags.stream().forEach(tag -> {
//                        tags.add(tag.getTagId());
//                        tagRels.add(
//                                WeFlowerCustomerTagRel.builder()
//                                        .flowerCustomerRelId(customer.getId())
//                                        .externalUserid(customer.getExternalUserid())
//                                        .tagId(tag.getTagId())
//                                        .relTagType(1)
//                                        .createTime(new Date())
//                                        .build()
//                        );
//                    });
//
//                    cutomerTagEdit.setAdd_tag(ArrayUtil.toArray(tags, String.class));
//                    cutomerTagEdits.add(cutomerTagEdit);
//                });
//
//                if (iWeFlowerCustomerTagRelService.saveOrUpdateBatch(tagRels)) {
////                    if (CollectionUtil.isNotEmpty(cutomerTagEdits)) {
////                        cutomerTagEdits.stream().forEach(k -> {
////                            weCustomerClient.makeCustomerLabel(
////                                    k
////                            );
////                        });
////                    }
//                }
//            }
//
//
//
//            iWeCustomerTrajectoryService.inforMationNews(weMakeCustomerTag.getUserId(),weMakeCustomerTag.getExternalUserid(),
//                    TrajectorySceneType.TRAJECTORY_TYPE_XXDT_SZBQ.getKey());
//
//        }


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
        String userId=null;
        if(Constants.USER_TYPE_WECOME.equals(SecurityUtils.getLoginUser().getUser().getUserType())){
             userId=SecurityUtils.getLoginUser().getUser().getWeUserId();
        }
        return this.baseMapper.getCustomersByUserId(externalUserid,userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getCustomersInfoAndSynchWeCustomer(String externalUserid,String userId) {

        //获取指定客户的详情
        ExternalUserDetail externalUserDetail = weCustomerClient.get(externalUserid);

        if (WeConstans.WE_SUCCESS_CODE.equals(externalUserDetail.getErrcode())) {
            //客户入库
            WeCustomer weCustomer = new WeCustomer();
            BeanUtils.copyPropertiesASM(externalUserDetail.getExternal_contact(), weCustomer);

            //判断该客户是否存在
            if(this.baseMapper.selectWeCustomerById(externalUserid) != null){//直接更新无需修改首位添加人和时间
                weCustomer.setDelFlag(new Integer(0));
                this.baseMapper.updateWeCustomer(weCustomer);
            }else {
                weCustomer.setFirstUserId(userId);
                weCustomer.setFirstAddTime(new Date());
                this.save(weCustomer);
            }

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


                    List<WeFlowerCustomerTagRel> tagRels
                            = weFlowerCustomerTagRels.stream().filter(rel -> Objects.nonNull(rel.getUserId())).collect(Collectors.toList());

                    if(CollectionUtil.isNotEmpty(tagRels)){

                        iWeFlowerCustomerTagRelService.saveOrUpdateBatch(tagRels);

                    }

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
        return this.baseMapper.updateWeCustomer(weCustomer) == 1;
    }

    @Override
    public List<WeUser> getCustomerByTag(List<String> ids) {
        return this.baseMapper.getCustomerByTag(ids);
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


}
