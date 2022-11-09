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
import com.linkwechat.domain.sop.WeSopExecuteTargetAttachments;
import com.linkwechat.domain.wecom.query.customer.msg.WeAddCustomerMsgQuery;
import com.linkwechat.domain.wecom.vo.customer.msg.WeAddCustomerMsgVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.service.AbstractGroupMsgSendTaskService;
import com.linkwechat.service.IWeSopExecuteTargetAttachmentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 *
 * sop企业微信api群发消息
 */
@Service("sopGroupMsgService")
@Slf4j
public class SopGroupMsgServiceImpl extends AbstractGroupMsgSendTaskService {

    @Autowired
    QwCustomerClient qwCustomerClient;

    @Autowired
    IWeSopExecuteTargetAttachmentsService sopExecuteTargetAttachmentsService;


    @Autowired
    LinkWeChatConfig linkWeChatConfig;



    @Override
    public void sendGroupMsg(WeAddGroupMessageQuery query) {


        LoginUser loginUser = query.getLoginUser();
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setCorpId(loginUser.getCorpId());

        String businessIds = query.getBusinessIds();

        if(StringUtils.isNotEmpty(businessIds)){

          List<WeSopExecuteTargetAttachments> targetAttachments
              =sopExecuteTargetAttachmentsService.list(new LambdaQueryWrapper<WeSopExecuteTargetAttachments>()
                    .in(WeSopExecuteTargetAttachments::getId,ListUtil.toList(businessIds.split(",")))
                    .eq(WeSopExecuteTargetAttachments::getDelFlag, Constants.COMMON_STATE));

            if(CollectionUtil.isNotEmpty(targetAttachments)){
                Optional.of(query).map(WeAddGroupMessageQuery::getSenderList).orElseGet(ArrayList::new).forEach(sender -> {
                    WeAddCustomerMsgQuery templateQuery = new WeAddCustomerMsgQuery();
                    templateQuery.setChat_type(query.getChatType());
                    templateQuery.setSender(sender.getUserId());
                    if (ObjectUtil.equal(1, query.getChatType())) {
                        templateQuery.setExternal_userid(sender.getCustomerList());
                    }

                    getMediaId(query.getAttachmentsList());
                    templateQuery.setAttachmentsList(linkWeChatConfig.getDomain(),query.getAttachmentsList());
                    WeAddCustomerMsgVo weAddCustomerMsgVo = qwCustomerClient.addMsgTemplate(templateQuery).getData();
                    if (weAddCustomerMsgVo != null && ObjectUtil.equal(WeConstans.WE_SUCCESS_CODE, weAddCustomerMsgVo.getErrCode())) {
                        targetAttachments.stream().forEach(targetAttachment->{
                            targetAttachment.setMsgId(weAddCustomerMsgVo.getMsgId());
                            targetAttachment.setIsTip(1);

                        });

                    }
                });
                sopExecuteTargetAttachmentsService.updateBatchById(targetAttachments);
            }
        }



    }

}
