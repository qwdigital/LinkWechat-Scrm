package com.linkwechat.wecom.factory;

import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackDeptVo;
import com.linkwechat.wecom.domain.callback.WeBackUserVo;
import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.domain.WeUser;

/**
 * @author danmo
 * @description 事件类型策略接口
 * @date 2021/1/20 22:00
 **/
public abstract class WeEventStrategy {
    protected final String tag = "tag";
    protected final String tagGroup = "tag_group";

    public abstract void eventHandle(WeBackBaseVo message);

    //生成成员数据
    public WeUser setWeUserData(WeBackUserVo message) {
        WeUser weUser = WeUser.builder().userId(message.getUserID())
                .email(message.getEmail())
                .name(message.getName())
                .alias(message.getAlias())
                .gender(message.getGender())
                .address(message.getAddress())
                .telephone(message.getTelephone())
                .mobile(message.getMobile())
                .headImageUrl(message.getAvatar())
                .position(message.getPosition())
                .build();
        if (message.getStatus() != null) {
            weUser.setIsActivate(message.getStatus());
        }
        if (message.getIsLeaderInDept() != null) {
            weUser.setIsLeaderInDept(message.getIsLeaderInDept());
        }
        if (message.getDepartment() != null) {
            weUser.setDepartment(message.getDepartment());
        }
        return weUser;
    }

    //部门信息
    public WeDepartment setWeDepartMent(WeBackDeptVo message){
        WeDepartment weDepartment = new WeDepartment();
        if (message.getId() != null) {
            weDepartment.setId(Long.parseLong(message.getId()));
        }
        if (message.getName() != null) {
            weDepartment.setName(message.getName());
        }
        if (message.getParentId() != null) {
            weDepartment.setParentId(Long.valueOf(message.getParentId()));
        }
        return weDepartment;
    }
}
