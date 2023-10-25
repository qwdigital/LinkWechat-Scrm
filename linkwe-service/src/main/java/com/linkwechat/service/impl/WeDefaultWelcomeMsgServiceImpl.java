package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.know.WeKnowCustomerAttachments;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.welcomemsg.WeDefaultWelcomeMsg;
import com.linkwechat.service.IWeDefaultWelcomeMsgService;
import com.linkwechat.mapper.WeDefaultWelcomeMsgMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author robin
* @description 针对表【we_default_welcome_msg(默认欢迎语附件)】的数据库操作Service实现
* @createDate 2023-10-23 11:15:10
*/
@Service
public class WeDefaultWelcomeMsgServiceImpl extends ServiceImpl<WeDefaultWelcomeMsgMapper, WeDefaultWelcomeMsg>
    implements IWeDefaultWelcomeMsgService {

    @Override
    public void saveOrUpdateBatchWeComeMsg(List<WeMessageTemplate> attachments) {
        this.remove(new LambdaQueryWrapper<>());
        List<WeDefaultWelcomeMsg> attachmentsList = Optional.ofNullable(attachments)
                .orElseGet(ArrayList::new).stream().map(weQrAttachmentsQuery -> {
                    WeDefaultWelcomeMsg weDefaultWelcomeMsg = new WeDefaultWelcomeMsg();
                    BeanUtil.copyProperties(weQrAttachmentsQuery, weDefaultWelcomeMsg);
                    return weDefaultWelcomeMsg;
                }).collect(Collectors.toList());
        saveOrUpdateBatch(attachmentsList);
    }

    @Override
    public List<WeMessageTemplate> findWeMessageTemplates() {
        List<WeMessageTemplate> messageTemplates=new ArrayList<>();
        List<WeDefaultWelcomeMsg> weDefaultWelcomeMsgs = this.list();
        if(CollectionUtil.isNotEmpty(weDefaultWelcomeMsgs)){
            weDefaultWelcomeMsgs.stream().forEach(weDefaultWelcomeMsg->{

                WeMessageTemplate weMessageTemplate = new WeMessageTemplate();

                BeanUtil.copyProperties(weDefaultWelcomeMsg, weMessageTemplate);
                messageTemplates.add(weMessageTemplate);
            });

        }
        return messageTemplates;
    }


}




