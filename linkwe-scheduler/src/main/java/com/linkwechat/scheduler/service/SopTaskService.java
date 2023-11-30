package com.linkwechat.scheduler.service;

import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeSopChange;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.domain.sop.dto.WeSopBaseDto;

public interface SopTaskService {


    /**
     * 更新或新建sop执行计划
     * @param weSopBaseDto
     */
    void createOrUpdateSop(WeSopBaseDto weSopBaseDto);




    /**
     * 构建新客SOP
     */
    void builderNewWeCustomer(WeCustomer weCustomer);


    /**
     * 新群加入
     * @param linkGroupChatListVo
     */
    void builderNewGroup(LinkGroupChatListVo linkGroupChatListVo);


    /**
     * 转入sop处理
     */
    void handleChangeSop( WeSopChange weSopChange);
}
