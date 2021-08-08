package com.linkwechat.wecom.factory;

import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 事件类型策略接口
 * @date 2021/1/20 22:00
 **/
public abstract class WeEventStrategy {
    protected final String tag = "tag";
    protected final String tagGroup = "tag_group";

    public abstract void eventHandle(WxCpXmlMessageVO message);

    //生成成员数据
    public WeUser setWeUserData(WxCpXmlMessageVO message) {
        WeUser weUser = WeUser.builder().userId(message.getUserId())
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
            weUser.setIsActivate(Integer.valueOf(message.getStatus()));
        }
        if (message.getIsLeaderInDept() != null) {
            String isLeaderInDeptStr = Arrays.stream(message.getIsLeaderInDept())
                    .map(String::valueOf).collect(Collectors.joining(","));
            weUser.setIsLeaderInDept(isLeaderInDeptStr);
        }
        if (message.getDepartments() != null) {
            String departmentsStr = Arrays.stream(message.getDepartments())
                    .map(String::valueOf).collect(Collectors.joining(","));
            weUser.setDepartment(departmentsStr);
        }
        return weUser;
    }

    //部门信息
    public WeDepartment setWeDepartMent(WxCpXmlMessageVO message){
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
