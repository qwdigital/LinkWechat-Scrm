package com.linkwechat.domain.wecom.entity.customer.moment;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 指定的发表范围
 * @date 2021/12/6 13:48
 **/
@Data
public class WeVisibleRangeEntity {

    private WeMomentSendsEntity sender_list;

    private WeMomentCustomersEntity external_contact_list;

    public void setSenderList(List<String> userList, List<Integer> departmentList) {
        WeMomentSendsEntity weMomentSends = new WeMomentSendsEntity();
        weMomentSends.setUser_list(userList);
        weMomentSends.setDepartment_list(departmentList);
        this.sender_list = weMomentSends;
    }

    public void setExternalContactList(List<String> tagList) {
        WeMomentCustomersEntity weMomentCustomer = new WeMomentCustomersEntity();
        weMomentCustomer.setTag_list(tagList);
        this.external_contact_list = weMomentCustomer;
    }
}
