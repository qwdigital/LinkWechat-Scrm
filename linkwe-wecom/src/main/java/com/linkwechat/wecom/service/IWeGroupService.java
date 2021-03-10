package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomerAddGroup;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.vo.WeLeaveUserInfoAllocateVo;

import java.util.List;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/22 0022 0:01
 */
public interface IWeGroupService extends IService<WeGroup> {

    List<WeGroup> selectWeGroupList(WeGroup paramWeGroup);


    void allocateWeGroup(WeLeaveUserInfoAllocateVo weLeaveUserInfoAllocateVo);


    void synchWeGroup();

    void createWeGroup(String chatId);

    void updateWeGroup(String chatId);

    void deleteWeGroup(String chatId);

    List<WeCustomerAddGroup> findWeGroupByCustomer(String userId,String externalUserid);
}
