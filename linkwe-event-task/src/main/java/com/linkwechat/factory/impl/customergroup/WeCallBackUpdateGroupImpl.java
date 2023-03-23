package com.linkwechat.factory.impl.customergroup;

import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.enums.GroupUpdateDetailEnum;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerGroupVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.service.IWeGroupService;
//import com.linkwechat.service.IWeTaskFissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 客户群变更事件
 * @date 2021/1/20 0:39
 **/
@Slf4j
@Component("update")
public class WeCallBackUpdateGroupImpl extends WeEventStrategy {

    @Autowired
    private IWeGroupService weGroupService;

//    @Autowired
//    private IWeTaskFissionService taskFissionService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerGroupVo customerGroupInfo = (WeBackCustomerGroupVo) message;
        try {
            /**
             *  * add_member : 成员入群
             *  * del_member : 成员退群
             *  * change_owner : 群主变更
             *  * change_name : 群名变更
             *  * change_notice : 群公告变更
             */
            String updateDetail = customerGroupInfo.getUpdateDetail();
            if (updateDetail.equals(GroupUpdateDetailEnum.ADD_MEMBER.getType())) {
                //成员入群
                weGroupService.addMember(customerGroupInfo.getChatId(),customerGroupInfo.getJoinScene(),customerGroupInfo.getMemChangeCnt());
                //任务宝入群校验
//                taskFissionService.groupFissionEnterCheck(customerGroupInfo.getChatId(),customerGroupInfo.getJoinScene(), customerGroupInfo.getCreateTime(), customerGroupInfo.getMemChangeCnt());
                //入群轨迹
            }else if(updateDetail.equals(GroupUpdateDetailEnum.DEL_MEMBER.getType())){
                //成员入退群
                weGroupService.delMember(customerGroupInfo.getChatId(),customerGroupInfo.getQuitScene(),customerGroupInfo.getMemChangeCnt());
            }else{
                //群信息变更
                weGroupService.changeGroup(customerGroupInfo.getChatId(),updateDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("update>>>>>>>>>param:{},ex:{}",customerGroupInfo.getChatId(),e);
        }finally {
            SecurityContextHolder.remove();
        }
    }

}
