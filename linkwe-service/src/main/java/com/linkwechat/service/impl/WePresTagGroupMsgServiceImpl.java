package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.taggroup.WePresTagGroupTaskStat;
import com.linkwechat.domain.wecom.vo.customer.msg.WeAddCustomerMsgVo;
import com.linkwechat.service.AbstractGroupMsgSendTaskService;
import com.linkwechat.service.IWePresTagGroupTaskStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * 老客标签建群发送逻辑
 */
@Service("wePresTagGroupMsgService")
@Slf4j
public class WePresTagGroupMsgServiceImpl extends AbstractGroupMsgSendTaskService {



    @Autowired
    IWePresTagGroupTaskStatService iWePresTagGroupTaskStatService;



    @Override
    public void sendGroupMsg(WeAddGroupMessageQuery query) {


       List<WePresTagGroupTaskStat> wePresTagGroupTaskStats=new ArrayList<>();
        Optional.of(query).map(WeAddGroupMessageQuery::getSenderList).orElseGet(ArrayList::new).forEach(sender -> {
            WeAddCustomerMsgVo weAddCustomerMsgVo = sendSpecGroupMsgTemplate(query, sender);
            if(null != weAddCustomerMsgVo && StringUtils.isNotEmpty(weAddCustomerMsgVo.getMsgId())){

                iWePresTagGroupTaskStatService.remove(new LambdaQueryWrapper<WePresTagGroupTaskStat>()
                        .eq(WePresTagGroupTaskStat::getTaskId,query.getBusinessId()));

                sender.getCustomerList().stream().forEach(extId->{

                    wePresTagGroupTaskStats.add(WePresTagGroupTaskStat.builder()
                            .msgId(weAddCustomerMsgVo.getMsgId())
                            .externalUserid(extId)
                            .userId(sender.getUserId())
                            .taskId(query.getBusinessId())
                            .build());

                });

            }
        });

        if(CollectionUtil.isNotEmpty(wePresTagGroupTaskStats)){
            iWePresTagGroupTaskStatService.saveBatch(wePresTagGroupTaskStats);
        }



    }
}
