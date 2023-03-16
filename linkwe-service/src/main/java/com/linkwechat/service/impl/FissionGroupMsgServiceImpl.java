package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.domain.fission.WeFissionDetail;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.wecom.vo.customer.msg.WeAddCustomerMsgVo;
import com.linkwechat.service.AbstractGroupMsgSendTaskService;
import com.linkwechat.service.IWeFissionDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 裂变群发消息
 */
@Service("fissionGroupMsgService")
@Slf4j
public class FissionGroupMsgServiceImpl  extends AbstractGroupMsgSendTaskService {

    @Autowired
    private IWeFissionDetailService iWeFissionDetailService;


    @Override
    public void sendGroupMsg(WeAddGroupMessageQuery query) {
        LoginUser loginUser = query.getLoginUser();
        SecurityContextHolder.setUserName(loginUser.getUserName());
        SecurityContextHolder.setCorpId(loginUser.getCorpId());

        List<WeAddGroupMessageQuery.SenderInfo> senderList = query.getSenderList();
        if(CollectionUtil.isNotEmpty(senderList)){
            senderList.stream().forEach(senderInfo -> {
                WeAddCustomerMsgVo weAddCustomerMsgVo = sendSpecGroupMsgTemplate(query, senderInfo);

                 if (weAddCustomerMsgVo != null && ObjectUtil.equal(WeConstans.WE_SUCCESS_CODE, weAddCustomerMsgVo.getErrCode())) {
                     iWeFissionDetailService.update(WeFissionDetail.builder().msgId(weAddCustomerMsgVo.getMsgId()).build(),

                             new LambdaQueryWrapper<WeFissionDetail>()
                                     .eq(WeFissionDetail::getFissionId,query.getBusinessIds())
                                     .in(WeFissionDetail::getSendWeUserid, ListUtil.toList(senderInfo.getUserId().split(","))));

                 }



            });

        }





    }
}
