package com.linkwechat.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.utils.QREncode;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.storecode.entity.WeStoreCodeConfig;
import com.linkwechat.domain.storecode.query.WxStoreCodeQuery;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.mapper.WeStoreCodeConfigMapper;
import com.linkwechat.mapper.WeStoreCodeCountMapper;
import com.linkwechat.service.IWeQrAttachmentsService;
import com.linkwechat.service.IWeStoreCodeConfigService;
import com.linkwechat.service.IWeStoreCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * 门店导购码或门店群活码
 */
@Service
public class WeStoreCodeConfigServiceImpl extends ServiceImpl<WeStoreCodeConfigMapper, WeStoreCodeConfig> implements IWeStoreCodeConfigService {

    @Autowired
    private QwCustomerClient qwCustomerClient;

    @Autowired
    private IWeQrAttachmentsService attachmentsService;

    @Autowired
    private QwFileClient qwFileClient;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


    @Autowired
    private IWeStoreCodeService iWeStoreCodeService;




    @Override
    public void createOrUpdate(WeStoreCodeConfig storeCodeConfig) throws IOException {


        if(StringUtils.isNotEmpty(storeCodeConfig.getConfigId())){//更新联系我
            qwCustomerClient.updateContactWay(WeAddWayQuery.builder()
                    .config_id(storeCodeConfig.getConfigId())
                    .user(ListUtil.toList(storeCodeConfig.getCustomerServiceId()))
                    .build());
        }else{//新增联系我
            storeCodeConfig.setState(WelcomeMsgTypeEnum.WE_STORE_CODE_CONFIG_PREFIX.getType() + SnowFlakeUtil.nextId());
            WeAddWayVo weAddWayResult = qwCustomerClient.addContactWay(WeAddWayQuery.builder()
                    .type(1)
                    .scene(2)
                    .state(storeCodeConfig.getState())
                    .user(ListUtil.toList(storeCodeConfig.getCustomerServiceId()))
                    .build()).getData();
            storeCodeConfig.setConfigId(weAddWayResult.getConfigId());
            storeCodeConfig.setCustomerServiceUrl(weAddWayResult.getQrCode());
        }

        if(StringUtils.isEmpty(storeCodeConfig.getStoreCodeConfigUrl())){//生成对应的唯一二维码


            String contentUrl =new Integer(1).equals(storeCodeConfig.getStoreCodeType())?linkWeChatConfig.getGuideCodeUrl():linkWeChatConfig.getGuideGroupUrl();

            FileEntity fileEntity =
                    qwFileClient.upload(QREncode.getQRCodeMultipartFile(contentUrl, null)).getData();
            if(null != fileEntity){
                storeCodeConfig.setStoreCodeConfigQr(fileEntity.getUrl());
            }
               storeCodeConfig.setStoreCodeConfigUrl(contentUrl);
        }

         if(saveOrUpdate(storeCodeConfig)){

             attachmentsService.updateBatchByQrId(storeCodeConfig.getId(),2, storeCodeConfig.getAttachments());

         }
    }

    @Override
    public WeStoreCodeConfig getWeStoreCodeConfig(WxStoreCodeQuery wxStoreCodeQuery) {

        if(wxStoreCodeQuery.getIsCount()){
            iWeStoreCodeService.countUserBehavior(wxStoreCodeQuery);
        }

        return this.baseMapper.getWeStoreCodeConfig(wxStoreCodeQuery.getStoreCodeType());
    }


}
