package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.framework.web.domain.server.Sys;
import com.linkwechat.wecom.client.WeCropTagClient;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.customer.ExternalUserDetail;
import com.linkwechat.wecom.domain.dto.customer.ExternalUserList;
import com.linkwechat.wecom.domain.dto.customer.ExternalUserTag;
import com.linkwechat.wecom.domain.dto.customer.FollowUserList;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagDto;
import com.linkwechat.wecom.domain.dto.tag.WeCropGroupTagListDto;
import com.linkwechat.wecom.domain.dto.tag.WeCropTagDto;
import com.linkwechat.wecom.domain.dto.tag.WeFindCropTagParam;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 企业微信客户Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
@Service
public class WeCustomerServiceImpl extends ServiceImpl<WeCustomerMapper,WeCustomer> implements IWeCustomerService
{
    @Autowired
    private WeCustomerMapper weCustomerMapper;


    @Autowired
    private WeCustomerClient weFollowUserClient;


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


//    @Autowired
//    private WeUserClient weUserClient;






    /**
     * 查询企业微信客户
     * 
     * @param id 企业微信客户ID
     * @return 企业微信客户
     */
    @Override
    public WeCustomer selectWeCustomerById(Long id)
    {
        return weCustomerMapper.selectWeCustomerById(id);
    }

    /**
     * 查询企业微信客户列表
     * 
     * @param weCustomer 企业微信客户
     * @return 企业微信客户
     */
    @Override
    public List<WeCustomer> selectWeCustomerList(WeCustomer weCustomer)
    {
        return weCustomerMapper.selectWeCustomerList(weCustomer);
    }

//    /**
//     * 新增企业微信客户
//     *
//     * @param weCustomer 企业微信客户
//     * @return 结果
//     */
//    @Override
//    public int insertWeCustomer(WeCustomer weCustomer)
//    {
//        return weCustomerMapper.insertWeCustomer(weCustomer);
//    }
//
//    /**
//     * 修改企业微信客户
//     *
//     * @param weCustomer 企业微信客户
//     * @return 结果
//     */
//    @Override
//    public int updateWeCustomer(WeCustomer weCustomer)
//    {
//        return weCustomerMapper.updateWeCustomer(weCustomer);
//    }
//
//    /**
//     * 批量删除企业微信客户
//     *
//     * @param ids 需要删除的企业微信客户ID
//     * @return 结果
//     */
//    @Override
//    public int deleteWeCustomerByIds(Long[] ids)
//    {
//        return weCustomerMapper.deleteWeCustomerByIds(ids);
//    }
//
//    /**
//     * 删除企业微信客户信息
//     *
//     * @param id 企业微信客户ID
//     * @return 结果
//     */
//    @Override
//    public int deleteWeCustomerById(Long id)
//    {
//        return weCustomerMapper.deleteWeCustomerById(id);
//    }


    /**
     * 客户同步接口
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchWeCustomer() {

        FollowUserList followUserList = weFollowUserClient.getFollowUserList();

        if(WeConstans.WE_SUCCESS_CODE.equals(followUserList.getErrcode())
        && ArrayUtil.isNotEmpty(followUserList.getFollow_user())){

            Arrays.asList(followUserList.getFollow_user())
                    .stream().forEach(k->{

                //获取指定联系人对应的客户
                ExternalUserList externalUsers = weFollowUserClient.list(k);
                if(WeConstans.WE_SUCCESS_CODE.equals(externalUsers.getErrcode())
                || WeConstans.NOT_EXIST_CONTACT.equals(externalUsers.getErrcode())
                && ArrayUtil.isNotEmpty(externalUsers.getExternal_userid())){

                    Arrays.asList(externalUsers.getExternal_userid()).forEach(v->{

                        //获取指定客户的详情
                        ExternalUserDetail externalUserDetail = weFollowUserClient.get(v);

                        if(WeConstans.WE_SUCCESS_CODE.equals(externalUserDetail.getErrcode())){
                            //客户入库
                            WeCustomer  weCustomer=new WeCustomer();
                            BeanUtils.copyPropertiesignoreOther( externalUserDetail.getExternal_contact(),weCustomer);
                            this.saveOrUpdate(weCustomer);

                            //客户与通讯录客户关系
                            List<WeTag> weTags=new ArrayList<>();
                            List<WeTagGroup> weGroups=new ArrayList<>();
                            List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels=new ArrayList<>();
                            List<WeFlowerCustomerRel> weFlowerCustomerRel=new ArrayList<>();
                            externalUserDetail.getFollow_user().stream().forEach(kk->{
                                WeFlowerCustomerRel weFlowerCustomerRelOne=new WeFlowerCustomerRel();
                                BeanUtils.copyPropertiesignoreOther(kk,weFlowerCustomerRelOne);
                                Long weFlowerCustomerRelId=SnowFlakeUtil.nextId();
                                weFlowerCustomerRelOne.setId(weFlowerCustomerRelId);
                                weFlowerCustomerRelOne.setExternalUserid(weCustomer.getExternalUserid());
                                weFlowerCustomerRel.add(weFlowerCustomerRelOne);
                                List<ExternalUserTag> tags = kk.getTags();
                                if(CollectionUtil.isNotEmpty(tags)){

                                    //获取相关标签组
                                    WeCropGroupTagListDto weCropGroupTagListDto = weCropTagClient.getCorpTagListByTagIds(WeFindCropTagParam.builder()
                                            .tag_id(ArrayUtil.toArray(tags.stream().map(ExternalUserTag::getTag_id).collect(Collectors.toList()), String.class))
                                            .build());

                                    if(weCropGroupTagListDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
                                        List<WeCropGroupTagDto> tagGroups = weCropGroupTagListDto.getTag_group();
                                        if(CollectionUtil.isNotEmpty(tagGroups)){
                                            tagGroups.stream().forEach(tagGroup->{
                                                    weGroups.add(
                                                        WeTagGroup.builder()
                                                                .groupId(tagGroup.getGroup_id())
                                                                .gourpName(tagGroup.getGroup_name())
                                                                .createBy(SecurityUtils.getUsername())
                                                                .build()
                                                     );

                                                List<WeCropTagDto> weCropTagDtos= tagGroup.getTag();

                                                if(CollectionUtil.isNotEmpty(weCropTagDtos)){
                                                    Set<String> tagIdsSet = weCropTagDtos.stream().map(WeCropTagDto::getId).collect(Collectors.toSet());

                                                        tags.stream().forEach(tag->{

                                                            if(tagIdsSet.contains(tag.getTag_id())){

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

                            if(CollectionUtil.isNotEmpty(weFlowerCustomerRels)){
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
                            if(CollectionUtil.isNotEmpty(weTags)&&CollectionUtil.isNotEmpty(weGroups)){
                                iWeTagService.saveOrUpdateBatch(weTags);
                                iWeTagGroupService.saveOrUpdateBatch(weGroups);
                                iWeFlowerCustomerTagRelService.saveOrUpdateBatch(weFlowerCustomerTagRels);
                            }

                        }



                    });

                }

            });


        }

    }


    /**
     * 分配离职员工客户
     * @param weLeaveUserInfoAllocateVo
     */
    @Override
    @Transactional
    public void allocateWeCustomer(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo) {

        //分配客户
        List<WeFlowerCustomerRel> weFlowerCustomerRels = iWeFlowerCustomerRelService.selectWeFlowerCustomerRelList(WeFlowerCustomerRel.builder()
                .userId(weLeaveUserInfoAllocateVo.getHandoverUserid())
                .build());
        if(CollectionUtil.isNotEmpty(weFlowerCustomerRels)){
            //删除原有的
//            iWeFlowerCustomerRelService.batchLogicDeleteByIds(weFlowerCustomerRels.stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList()));
//            //保存新的
//            weFlowerCustomerRels.stream().forEach(k->{
////                k.setId(SnowFlakeUtil.nextId());
//                k.setUserId(weLeaveUserInfoAllocateVo.getTakeoverUserid());
//            });
//            //保存新
//            iWeFlowerCustomerRelService.batchInsetWeFlowerCustomerRel(weFlowerCustomerRels);
        }

    }


}
