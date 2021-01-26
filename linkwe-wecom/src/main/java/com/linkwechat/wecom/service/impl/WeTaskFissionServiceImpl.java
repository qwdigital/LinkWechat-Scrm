package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.LinkMessageDto;
import com.linkwechat.wecom.mapper.WeTaskFissionMapper;
import com.linkwechat.wecom.service.*;
import com.linkwechat.wecom.strategy.MessageContext;
import com.linkwechat.wecom.strategy.SendMessageToUserStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 任务宝Service业务层处理
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Slf4j
@Service
public class WeTaskFissionServiceImpl implements IWeTaskFissionService {
    @Autowired
    private WeTaskFissionMapper weTaskFissionMapper;
    @Autowired
    private IWeTaskFissionStaffService weTaskFissionStaffService;
    @Autowired
    private IWeEmpleCodeService weEmpleCodeService;
    @Autowired
    private IWeCustomerMessagePushService weCustomerMessagePushService;

    /**
     * 查询任务宝
     *
     * @param id 任务宝ID
     * @return 任务宝
     */
    @Override
    public WeTaskFission selectWeTaskFissionById(Long id) {
        return weTaskFissionMapper.selectWeTaskFissionById(id);
    }

    /**
     * 查询任务宝列表
     *
     * @param weTaskFission 任务宝
     * @return 任务宝
     */
    @Override
    public List<WeTaskFission> selectWeTaskFissionList(WeTaskFission weTaskFission) {
        return weTaskFissionMapper.selectWeTaskFissionList(weTaskFission);
    }

    /**
     * 新增任务宝
     *
     * @param weTaskFission 任务宝
     * @return 结果
     */
    @Override
    public int insertWeTaskFission(WeTaskFission weTaskFission) {
        weTaskFission.setCreateTime(DateUtils.getNowDate());
        return weTaskFissionMapper.insertWeTaskFission(weTaskFission);
    }

    /**
     * 修改任务宝
     *
     * @param weTaskFission 任务宝
     * @return 结果
     */
    @Override
    public int updateWeTaskFission(WeTaskFission weTaskFission) {
        weTaskFission.setUpdateTime(DateUtils.getNowDate());
        return weTaskFissionMapper.updateWeTaskFission(weTaskFission);
    }

    /**
     * 批量删除任务宝
     *
     * @param ids 需要删除的任务宝ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionByIds(Long[] ids) {
        return weTaskFissionMapper.deleteWeTaskFissionByIds(ids);
    }

    /**
     * 删除任务宝信息
     *
     * @param id 任务宝ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionById(Long id) {
        return weTaskFissionMapper.deleteWeTaskFissionById(id);
    }


    @Override
    public void sendWeTaskFission(Long id) {
        WeTaskFission weTaskFission = selectWeTaskFissionById(id);
        //海报路径
        String postersPath = weTaskFission.getPostersPath();
        //目标员工id
        String fissStaffId = weTaskFission.getFissStaffId();
        //目标员工活码
        String fissStaffQrcode = weTaskFission.getFissStaffQrcode();
        //todo 生成海报

        LinkMessageDto linkMessageDto = new LinkMessageDto();
        linkMessageDto.setPicurl(postersPath);
        linkMessageDto.setDesc(weTaskFission.getFissInfo());
        linkMessageDto.setTitle(weTaskFission.getTaskName());
        linkMessageDto.setUrl("www.baidu.com");

        CustomerMessagePushDto customerMessagePushDto = new CustomerMessagePushDto();
        customerMessagePushDto.setLinkMessage(linkMessageDto);
        customerMessagePushDto.setPushType("0");
        customerMessagePushDto.setPushRange("1");
        customerMessagePushDto.setMessageType("2");
        customerMessagePushDto.setTag(weTaskFission.getCustomerTagId());
        //查询发起成员
        List<WeTaskFissionStaff> weTaskFissionStaffList = weTaskFissionStaffService.selectWeTaskFissionStaffByTaskId(id);
        if (CollectionUtil.isNotEmpty(weTaskFissionStaffList)){
            //获取部门id
            String departmentIds = weTaskFissionStaffList.stream().filter(weTaskFissionStaff ->
                    WeConstans.USE_SCOP_BUSINESSID_TYPE_ORG.equals(weTaskFissionStaff.getStaffType()))
                    .map(WeTaskFissionStaff::getStaffId).collect(Collectors.joining(","));
            //获取成员id
            String userIds = weTaskFissionStaffList.stream().filter(weTaskFissionStaff ->
                    WeConstans.USE_SCOP_BUSINESSID_TYPE_USER.equals(weTaskFissionStaff.getStaffType()))
                    .map(WeTaskFissionStaff::getStaffId).collect(Collectors.joining(","));
            customerMessagePushDto.setStaffId(userIds);
            customerMessagePushDto.setDepartment(departmentIds);
        }
        try {
            weCustomerMessagePushService.addWeCustomerMessagePush(customerMessagePushDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("发送任务失败》》》》》》》》》》》params:{},ex:{}", JSONObject.toJSONString(customerMessagePushDto),e);
        }


    }
}
