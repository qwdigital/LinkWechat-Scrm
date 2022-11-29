package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.live.WeLiveTip;
import com.linkwechat.domain.wecom.vo.customer.msg.WeAddCustomerMsgVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.service.AbstractGroupMsgSendTaskService;
import com.linkwechat.service.IWeLiveTipService;
import com.linkwechat.service.IWeMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * 直播通知群发
 */
@Service("liveGroupMsgService")
@Slf4j
public class LiveGroupMsgServiceImpl extends AbstractGroupMsgSendTaskService {


    @Autowired
    IWeLiveTipService iWeLiveTipService;


    @Autowired
    LinkWeChatConfig linkWeChatConfig;

    @Autowired
    IWeMaterialService weMaterialService;

    @Autowired
    QwCustomerClient qwCustomerClient;

    @Override
    public void sendGroupMsg(WeAddGroupMessageQuery query) {
        LoginUser loginUser = query.getLoginUser();
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setCorpId(loginUser.getCorpId());

        //直播id
        String businessIds = query.getBusinessIds();
        if(StringUtils.isNotEmpty(businessIds)){
            List<WeLiveTip> weLiveTips = iWeLiveTipService.list(new LambdaQueryWrapper<WeLiveTip>()
                    .in(WeLiveTip::getLiveId, ListUtil.toList(businessIds.split(",")))
                    .eq(WeLiveTip::getDelFlag, Constants.COMMON_STATE));


            if(CollectionUtil.isNotEmpty(weLiveTips)){

                WeAddCustomerMsgVo weAddCustomerMsgVo = sendSpecGroupMsgTemplate(query, query.getSenderList().stream().findFirst().get());

                //发送完成以后调用逻辑
                if (weAddCustomerMsgVo != null && ObjectUtil.equal(WeConstans.WE_SUCCESS_CODE, weAddCustomerMsgVo.getErrCode())) {
                    weLiveTips.stream().forEach(targetAttachment->{
                        targetAttachment.setMsgId(weAddCustomerMsgVo.getMsgId());
                    });

                    iWeLiveTipService.updateBatchById(weLiveTips);

                }

            }
        }

    }
}
