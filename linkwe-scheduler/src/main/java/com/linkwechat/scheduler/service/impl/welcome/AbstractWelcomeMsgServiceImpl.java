package com.linkwechat.scheduler.service.impl.welcome;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.customer.WeMakeCustomerTag;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.tag.vo.WeTagVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.domain.wecom.query.customer.WeCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.msg.WeWelcomeMsgQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.WeCustomerDetailVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.scheduler.service.IWelcomeMsgService;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @date 2023年03月10日 11:43
 */
@Service
@Slf4j
public class AbstractWelcomeMsgServiceImpl implements IWelcomeMsgService {


    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    protected IWeMaterialService weMaterialService;

    @Resource
    private QwCustomerClient qwCustomerClient;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


//    @Value("${wecom.welcome-msg-default}")
//    protected String welcomeMsgDefault;

    @Autowired
    private IWeDefaultWelcomeMsgService iWeDefaultWelcomeMsgService;

    @Override
    public void sendWelcomeMsg(WeBackCustomerVo query, List<WeMessageTemplate> attachments) {
        WeWelcomeMsgQuery welcomeMsg = new WeWelcomeMsgQuery();
        welcomeMsg.setWelcome_code(query.getWelcomeCode());
        welcomeMsg.setCorpid(query.getToUserName());

        if(CollectionUtil.isEmpty(attachments)){ //为空则设置默认欢迎语
            attachments.addAll(iWeDefaultWelcomeMsgService.findWeMessageTemplates());
        }

        //图片转化企业微信media_id
        weMaterialService.msgTplToMediaId(attachments);

        WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getAddUserId, query.getUserID())
                .eq(WeCustomer::getExternalUserid, query.getExternalUserID()).eq(WeCustomer::getDelFlag, Constants.COMMON_STATE).last("limit 1"));

        StringBuilder customerName = new StringBuilder("");

        if (weCustomer != null) {
            customerName.append(weCustomer.getCustomerName());
        }else {
            WeCustomerQuery weCustomerQuery = new WeCustomerQuery();
            weCustomerQuery.setExternal_userid(query.getExternalUserID());
            weCustomerQuery.setCorpid(query.getToUserName());
            WeCustomerDetailVo customerDetail = qwCustomerClient.getCustomerDetail(weCustomerQuery).getData();
            if(Objects.nonNull(customerDetail) && Objects.nonNull(customerDetail.getExternalContact())){
                customerName.append(customerDetail.getExternalContact().getName());
            }
        }
        attachments.forEach(attachment -> {
            if (ObjectUtil.equal(MessageType.TEXT.getMessageType(), attachment.getMsgType())) {
                attachment.setContent(attachment.getContent().replaceAll("#客户昵称#", customerName.toString()));
            }
        });

        welcomeMsg.setAttachmentsList(linkWeChatConfig.getH5Domain(), attachments);

        WeResultVo resultVo = qwCustomerClient.sendWelcomeMsg(welcomeMsg).getData();
        log.info("结束发送欢迎语：result:{}", JSONObject.toJSONString(resultVo));
    }

    @Override
    public void msgHandle(WeBackCustomerVo query) {
        this.msgHandle(query);
    }


    /**
     * 客户打标签
     *
     * @param externaUserId 客户id
     * @param userId        员工id
     * @param qrTags        标签id
     */
    protected void makeCustomerTag(String externaUserId, String userId, List<WeTagVo> qrTags) {
        ThreadUtil.execAsync(() ->{
            if (CollectionUtil.isNotEmpty(qrTags)) {
                List<WeTag> weTagList = qrTags.stream().map(tag -> {
                    WeTag weTag = new WeTag();
                    weTag.setName(tag.getTagName());
                    weTag.setTagId(tag.getTagId());
                    return weTag;
                }).collect(Collectors.toList());
                WeMakeCustomerTag makeCustomerTag = new WeMakeCustomerTag();
                makeCustomerTag.setExternalUserid(externaUserId);
                makeCustomerTag.setUserId(userId);
                makeCustomerTag.setAddTag(weTagList);
                try {
                    weCustomerService.makeLabel(makeCustomerTag);
                } catch (Exception e) {
                    log.error("发送欢迎语客户打标签失败 externaUserId:{},userId:{},qrTags:{}", externaUserId, userId, JSONObject.toJSONString(qrTags), e);
                }
            }
        });
    }
}
