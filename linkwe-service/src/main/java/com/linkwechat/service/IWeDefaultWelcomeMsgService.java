package com.linkwechat.service;

import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.welcomemsg.WeDefaultWelcomeMsg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author robin
* @description 针对表【we_default_welcome_msg(默认欢迎语附件)】的数据库操作Service
* @createDate 2023-10-23 11:15:10
*/
public interface IWeDefaultWelcomeMsgService extends IService<WeDefaultWelcomeMsg> {

    /**
     * 通过活码id批量保存活码欢迎语素材
     * @param attachments 素材列表
     */
    void saveOrUpdateBatchWeComeMsg(List<WeMessageTemplate> attachments);


    /**
     * 获取欢迎语WeMessageTemplate
     * @return
     */
    List<WeMessageTemplate> findWeMessageTemplates();



}
