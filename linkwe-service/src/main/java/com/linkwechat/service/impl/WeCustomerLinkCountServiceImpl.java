package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeCustomerLink;
import com.linkwechat.domain.WeCustomerLinkCount;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTabVo;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTableVo;
import com.linkwechat.domain.customer.vo.WeCustomerLinkCountTrendVo;
import com.linkwechat.domain.wecom.entity.customer.WeCustomerFollowInfoEntity;
import com.linkwechat.domain.wecom.entity.customer.WeCustomerFollowUserEntity;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.customer.WeCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.link.WeLinkCustomerCountQuery;
import com.linkwechat.domain.wecom.vo.customer.WeCustomerDetailVo;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkCustomerAcquisitionQuotaVo;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkWecustomerCountVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.SysLeaveUserMapper;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeCustomerLinkCountService;
import com.linkwechat.mapper.WeCustomerLinkCountMapper;
import com.linkwechat.service.IWeCustomerLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author robin
* @description 针对表【we_customer_link_count】的数据库操作Service实现
* @createDate 2023-07-26 14:51:19
*/
@Service
public class WeCustomerLinkCountServiceImpl extends ServiceImpl<WeCustomerLinkCountMapper, WeCustomerLinkCount>
    implements IWeCustomerLinkCountService {

    @Autowired
    private QwCustomerClient qwCustomerClient;

    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;

    @Autowired
    private IWeCustomerLinkService iWeCustomerLinkService;

    @Autowired
    private SysLeaveUserMapper sysLeaveUserMapper;

    @Autowired
    private RedisService redisService;

    @Override
    @Async
    public void synchWeCustomerLinkCount(String linkId) {



        List<WeCustomerLink> weCustomerLinks = iWeCustomerLinkService.list(
                new LambdaQueryWrapper<WeCustomerLink>()
                        .eq(WeCustomerLink::getLinkId,linkId)
                        .eq(WeCustomerLink::getDelFlag, Constants.COMMON_STATE)
        );

        if(CollectionUtil.isNotEmpty(weCustomerLinks)){
            List<WeCustomerLinkCount> weCustomerLinkCounts=new ArrayList<>();
            weCustomerLinks.stream().forEach(weCustomerLinkCount->{


                this.findCustomerLinkCount(null,weCustomerLinkCount.getLinkId(),weCustomerLinkCounts);

            });

            if(CollectionUtil.isNotEmpty(weCustomerLinkCounts)){
                this.baseMapper.batchAddOrUpdate(
                        weCustomerLinkCounts
                );
            }


        }




    }


    /**
     * 同步链接剩余量
     */
    @Override
    public void synchAcquisitionQuota() {

        //剩余量更新
        AjaxResult<WeLinkCustomerAcquisitionQuotaVo> result
                = qwCustomerClient.customerAcquisitionQuota(new WeBaseQuery());
        if(null != result){
            WeLinkCustomerAcquisitionQuotaVo data = result.getData();
            if(data != null && data.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
                List<WeCorpAccount> weCorpAccounts
                        = iWeCorpAccountService.list(new LambdaQueryWrapper<WeCorpAccount>());
                if(CollectionUtil.isNotEmpty(weCorpAccounts)){
                    WeCorpAccount weCorpAccount = weCorpAccounts.stream().findFirst().get();
                    weCorpAccount.setCustomerLinkTotal(data.getTotal());
                    weCorpAccount.setCustomerLinkMargin(data.getBalance());
                    iWeCorpAccountService.updateById(
                            weCorpAccount
                    );
                    redisService.deleteObject(Constants.CORP_ACCOUNT_KEY);
                }
            }
        }
    }


    //从企业微信同步相关数据
    private void findCustomerLinkCount(String nextCursor,String linkId, List<WeCustomerLinkCount> weCustomerLinkCounts){
        AjaxResult<WeLinkWecustomerCountVo> result = qwCustomerClient.customerLinkCount(WeLinkCustomerCountQuery.builder()
                .link_id(linkId)
                .cursor(nextCursor)
                .build());
        if(null != result ){
            WeLinkWecustomerCountVo data = result.getData();
            if(null != data && data.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
                List<WeLinkWecustomerCountVo.CustomerList> customerList = data.getCustomer_list();
                if(CollectionUtil.isNotEmpty(customerList)){
                    customerList.stream().forEach(k->{
                        WeCustomerLinkCount weCustomerLinkCount = WeCustomerLinkCount.builder()
                                .addTime(new Date())
                                .id(SnowFlakeUtil.nextId())
                                .linkId(linkId)
                                .chatStatus(k.getChat_status())
                                .externalUserid(k.getExternal_userid())
                                .weUserId(k.getUserid())
                                .build();

                        AjaxResult<WeCustomerDetailVo> customerDetail = qwCustomerClient.getCustomerDetail(WeCustomerQuery.builder()
                                .external_userid(k.getExternal_userid())
                                .build());

                        if(null != customerDetail){
                            WeCustomerDetailVo data1 = customerDetail.getData();
                            if(null != data1 && data1.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
                                List<WeCustomerFollowUserEntity> followUsers = data1.getFollowUser();
                                if(CollectionUtil.isNotEmpty(followUsers)){
                                    List<WeCustomerFollowUserEntity> followUserEntities
                                            = followUsers.stream().filter(item -> item.getUserId().equals(k.getUserid())).collect(Collectors.toList());
                                    if(CollectionUtil.isNotEmpty(followUserEntities)){
                                        WeCustomerFollowUserEntity weCustomerFollowUserEntity = followUserEntities.stream().findFirst().get();
                                        weCustomerLinkCount.setAddTime(
                                                new Date(weCustomerFollowUserEntity.getCreateTime() * 1000L)
                                        );
                                        SysUser sysUser
                                                = sysLeaveUserMapper.findSysUserByWeUserId(weCustomerFollowUserEntity.getUserId());

                                        if(null != sysUser){
                                            weCustomerLinkCount.setUserName(sysUser.getUserName());
                                        }else{
                                            weCustomerLinkCount.setUserName("@员工");
                                        }

                                        WeCustomerDetailVo.ExternalContact externalContact = data1.getExternalContact();

                                        if(null != externalContact){
                                            weCustomerLinkCount.setCustomerType(externalContact.getType());
                                            weCustomerLinkCount.setCustomerName(externalContact.getName());
                                            weCustomerLinkCount.setAvatar(externalContact.getAvatar());
                                            weCustomerLinkCount.setGender(externalContact.getGender());
                                        }

                                    }
                                }
                            }

                        }


                        weCustomerLinkCounts.add(
                                weCustomerLinkCount
                        );



                    });
                }

                if (StringUtils.isNotEmpty(data.getNextCursor())) {
                    findCustomerLinkCount(data.getNextCursor(),linkId, weCustomerLinkCounts);
                }
            }
        }

    }

    @Override
    public List<WeCustomerLinkCountTrendVo> selectLinkCountTrend(String linkId, String beginTime, String endTime) {
        return this.baseMapper.selectLinkCountTrend(linkId,beginTime,endTime);
    }

    @Override
    public List<WeCustomerLinkCountTableVo> selectLinkCountTable(String linkId, String beginTime, String endTime) {
        return this.baseMapper.selectLinkCountTable(linkId,beginTime,endTime);
    }

    @Override
    public WeCustomerLinkCountTabVo selectLinkCountTab(String linkId) {
        return this.baseMapper.selectLinkCountTab(linkId);
    }
}




