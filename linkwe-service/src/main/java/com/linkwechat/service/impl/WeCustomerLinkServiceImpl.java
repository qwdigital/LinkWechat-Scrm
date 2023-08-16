package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomerLink;
import com.linkwechat.domain.WeCustomerLinkAttachments;
import com.linkwechat.domain.WeCustomerLinkCount;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.wecom.query.customer.link.WeLinkCustomerQuery;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkCustomerVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.fegin.QwUserClient;
import com.linkwechat.service.IWeCustomerLinkAttachmentsService;
import com.linkwechat.service.IWeCustomerLinkService;
import com.linkwechat.mapper.WeCustomerLinkMapper;
import com.linkwechat.service.IWeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author robin
 * @description 针对表【we_customer_link(获客助手)】的数据库操作Service实现
 * @createDate 2023-07-04 17:41:13
 */
@Service
public class WeCustomerLinkServiceImpl extends ServiceImpl<WeCustomerLinkMapper, WeCustomerLink>
        implements IWeCustomerLinkService {

    @Autowired
    private IWeCustomerLinkAttachmentsService iWeCustomerLinkAttachmentsService;

    @Autowired
    private IWeTagService iWeTagService;

    @Autowired
    private QwCustomerClient qwCustomerClient;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Autowired
    private QwSysUserClient qwSysUserClient;

    @Override
    public List<WeCustomerLinkCount> findLinkWeCustomer(WeCustomerLinkCount weCustomerLinkCount) {
        return this.baseMapper.findLinkWeCustomer(weCustomerLinkCount);
    }

    @Override
    @Transactional
    public void createOrUpdateCustomerLink(WeCustomerLink customerLink,boolean createOrUpdate) {


        if(customerLink.getId() == null){
            customerLink.setId(SnowFlakeUtil.nextId());
        }

        if(StringUtils.isNotEmpty(customerLink.getWeUserList())){

            WeLinkCustomerQuery customerQuery = WeLinkCustomerQuery.builder()
                    .link_id(customerLink.getLinkId())
                    .link_name(customerLink.getLinkName())
                    .range(
                            WeLinkCustomerQuery.Range.builder()
                                    .user_list(customerLink.getWeUserList().split(","))
                                    .build()
                    )
                    .skip_verify(customerLink.getSkipVerify().equals(new Integer(1)) ? true : false)
                    .build();




            WeLinkCustomerVo weLinkCustomerVo =  createOrUpdate ? qwCustomerClient.createCustomerLink(
                    customerQuery
            ).getData():qwCustomerClient.updateCustomerLink(customerQuery).getData();




            if(!weLinkCustomerVo.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
                throw new WeComException(weLinkCustomerVo.getErrMsg());
            }


            if(null != weLinkCustomerVo){
                WeLinkCustomerVo.Link link = weLinkCustomerVo.getLink();
                if(null != link){
                    customerLink.setLinkId(link.getLink_id());
                    customerLink.setState(WelcomeMsgTypeEnum.WE_CUSTOMER_LINK_PREFIX.getType()+customerLink.getId());
                    customerLink.setLinkUrl(link.getUrl()+"?customer_channel="+customerLink.getState());
                    customerLink.setLinkShortUrl(
                            linkWeChatConfig.getCustomerShortLinkDomainName() + Base62NumUtil.encode(customerLink.getId())
                    );
                }
            }

            if(saveOrUpdate(customerLink)){
                if(createOrUpdate){
                    iWeCustomerLinkAttachmentsService.saveBatchByCustomerLinkId(customerLink.getId(),customerLink.getAttachments());

                }else{
                    iWeCustomerLinkAttachmentsService.updateBatchByCustomerLinkId(customerLink.getId(),customerLink.getAttachments());
                }
            }

        }else{
            throw new WeComException("链接员工不可为空");

        }


    }

    @Override
    public WeCustomerLink findWeCustomerLinkById(Long id) {

        WeCustomerLink weCustomerLink
                = this.getById(id);

        //获取附件等信息
        if(null != weCustomerLink){

            if(StringUtils.isNotEmpty(weCustomerLink.getTagIds())){
                List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                        .in(WeTag::getTagId, weCustomerLink.getTagIds().split(",")));
                if(CollectionUtil.isNotEmpty(weTags)){
                    weCustomerLink.setTagNames(
                            weTags.stream().map(WeTag::getName).collect(Collectors.joining(","))
                    );
                }
            }


            if(StringUtils.isNotEmpty(weCustomerLink.getWeUserList())){
                List<SysUser> allSysUsers =
                        qwSysUserClient.findAllSysUser(weCustomerLink.getWeUserList(), null, null).getData();

                if(CollectionUtil.isNotEmpty(allSysUsers)){
                    weCustomerLink.setWeUserNames(
                            allSysUsers.stream().map(SysUser::getUserName).collect(Collectors.joining(","))
                    );
                }
            }


            weCustomerLink.setLinkAttachments(
                    iWeCustomerLinkAttachmentsService.list(new LambdaQueryWrapper<WeCustomerLinkAttachments>()
                            .eq(WeCustomerLinkAttachments::getCustomerLinkId,id))
            );


        }





        return weCustomerLink;
    }

    @Override
    public JSONObject getShort2LongUrl(String shortUrl) {
        long id = Base62NumUtil.decode(shortUrl);
        WeCustomerLink weCustomerLink = getById(id);
        JSONObject resObj = new JSONObject();
        if (Objects.isNull(weCustomerLink)) {
            resObj.put("errorMsg", "无效链接");
            return resObj;
        }
        resObj.put("type",0);

        if (StringUtils.isNotEmpty(weCustomerLink.getLinkUrl())) {
            resObj.put("linkUrl", weCustomerLink.getLinkUrl());
        }
        return resObj;
    }



    @Override
    @Transactional
    public void removeLink(List<Long> ids) {

        if(this.removeByIds(ids)){
            ids.stream().forEach(k->{
                WeCustomerLink weCustomerLink = this.getById(k);

                if(weCustomerLink != null && StringUtils.isNotEmpty(weCustomerLink.getLinkUrl())){
                    qwCustomerClient.deleteCustomerLink(WeLinkCustomerQuery.builder()
                            .link_id(weCustomerLink.getLinkId())
                            .build());
                }

            });
        }

    }


}




