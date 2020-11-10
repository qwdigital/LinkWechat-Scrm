package com.linkwechat.wecom.factory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import com.linkwechat.wecom.domain.WeFlowerCustomerTagRel;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeCallBackEventFactory;
import com.linkwechat.wecom.service.IWeDepartmentService;
import com.linkwechat.wecom.service.IWeFlowerCustomerRelService;
import com.linkwechat.wecom.service.IWeFlowerCustomerTagRelService;
import com.linkwechat.wecom.service.IWeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sxw
 * @description 企微变更事件通知处理类
 * @date 2020/11/9 17:17
 **/
@Service
@Slf4j
public class WeEventChangeContactImpl implements WeCallBackEventFactory {
    @Autowired
    private IWeUserService weUserService;

    @Autowired
    private IWeDepartmentService weDepartmentService;

    @Autowired
    private IWeFlowerCustomerRelService weFlowerCustomerRelService;

    @Autowired
    private IWeFlowerCustomerTagRelService weFlowerCustomerTagRelService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        //新增: create_user 更新: update_user 删除:delete_user
        String changeType = message.getChangeType();
        switch (changeType) {
            case "create_user":
                this.addUser(message);
                break;
            case "update_user":
                this.updateUser(message);
                break;
            case "delete_user":
                this.delUser(message);
                break;
            case "create_party":
                this.createParty(message);
                break;
            case "update_party":
                this.updateParty(message);
                break;
            case "delete_party":
                this.deleteParty(message);
                break;
            case "update_tag":
                this.updateTag(message);
                break;
            default:
                break;
        }
    }


    @Transactional(rollbackFor = Exception.class)
    protected void createParty(WxCpXmlMessageVO message) {
        try {
            WeDepartment weDepartment = new WeDepartment();
            if (message.getId() !=null){
                weDepartment.setId(message.getId());
            }
            if (message.getName() !=null){
                weDepartment.setName(message.getName());
            }
            if (message.getParentId() !=null){
                weDepartment.setParentId(Long.valueOf(message.getParentId()));
            }
            weDepartmentService.save(weDepartment);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected void deleteParty(WxCpXmlMessageVO message) {
        try {
            if (message.getId() != null) {
                weDepartmentService.removeById(message.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected void updateParty(WxCpXmlMessageVO message) {
        try {
            WeDepartment weDepartment = new WeDepartment();
            if (message.getId() !=null){
                weDepartment.setId(message.getId());
            }
            if (message.getName() !=null){
                weDepartment.setName(message.getName());
            }
            if (message.getParentId() !=null){
                weDepartment.setParentId(Long.valueOf(message.getParentId()));
            }
            weDepartmentService.saveOrUpdate(weDepartment);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected void updateTag(WxCpXmlMessageVO message) {
        try {
            String tagId = message.getTagId();
            //标签中新增的成员userid列表，用逗号分隔
            List<String> addUserItemsList = Arrays.stream(Optional.ofNullable(message.getAddUserItems())
                    .orElse("").split(",")).collect(Collectors.toList());
            //标签中删除的成员userid列表，用逗号分隔
            List<String> delUserItemsList = Arrays.stream(Optional.ofNullable(message.getDelUserItems())
                    .orElse("").split(",")).collect(Collectors.toList());

            //标签中新增的成员userid列表，建立关联
            List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
            LambdaQueryWrapper<WeFlowerCustomerRel> relLambdaQueryWrapper = new LambdaQueryWrapper<>();
            relLambdaQueryWrapper.in(WeFlowerCustomerRel::getUserId,addUserItemsList);
            List<WeFlowerCustomerRel> flowerCustomerRelList = weFlowerCustomerRelService.list(relLambdaQueryWrapper);
            List<Long> idList = Optional.ofNullable(flowerCustomerRelList).orElseGet(ArrayList::new)
                    .stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList());
            idList.forEach(id ->{
                weFlowerCustomerTagRels.add(WeFlowerCustomerTagRel.builder().flowerCustomerRelId(id)
                        .tagId(tagId)
                        .build());
            });
            weFlowerCustomerTagRelService.batchInsetWeFlowerCustomerTagRel(weFlowerCustomerTagRels);

            //当前标签对应成员列表
            LambdaQueryWrapper<WeFlowerCustomerTagRel> tagRelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tagRelLambdaQueryWrapper.eq(WeFlowerCustomerTagRel::getTagId,tagId);
            List<WeFlowerCustomerTagRel> tagRelList = weFlowerCustomerTagRelService.list(tagRelLambdaQueryWrapper);

            List<Long> flowerCustomerRelIdList = Optional.ofNullable(tagRelList).orElseGet(ArrayList::new)
                    .stream().map(WeFlowerCustomerTagRel::getFlowerCustomerRelId).collect(Collectors.toList());

            if (!flowerCustomerRelIdList.isEmpty()){
                LambdaQueryWrapper<WeFlowerCustomerRel> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(WeFlowerCustomerRel::getId,flowerCustomerRelIdList)
                        .in(WeFlowerCustomerRel::getUserId,delUserItemsList);
                List<WeFlowerCustomerRel> relList = weFlowerCustomerRelService.list(queryWrapper);
                List<Long>  relIdList = Optional.ofNullable(relList).orElseGet(ArrayList::new)
                        .stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList());
                //标签中删除的成员userid列表
                LambdaQueryWrapper<WeFlowerCustomerTagRel> tagRelQueryWrapper = new LambdaQueryWrapper<>();
                tagRelQueryWrapper.in(WeFlowerCustomerTagRel::getFlowerCustomerRelId,relIdList);
                weFlowerCustomerTagRelService.remove(tagRelQueryWrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     * 新增企业成员
     *
     * @param message
     */
    @Transactional(rollbackFor = Exception.class)
    protected void addUser(WxCpXmlMessageVO message) {
        try {
            WeUser weUser = WeUser.builder().userId(message.getUserId())
                    .email(message.getEmail())
                    .name(message.getName())
                    .alias(message.getAlias())
                    .gender(message.getGender())
                    .address(message.getAddress())
                    .telephone(message.getTelephone())
                    .mobile(message.getMobile())
                    .avatarMediaid(message.getAvatar())
                    .position(message.getPosition())
                    .joinTime(new Date())
                    .build();
            if (message.getStatus()!=null){
                weUser.setIsActivate(Integer.valueOf(message.getStatus()));
            }
            if (message.getIsLeaderInDept()!=null){
                String[] isLeaderInDeptArr = Arrays.stream(message.getIsLeaderInDept())
                        .collect(Collectors.toList()).toArray(new String[message.getIsLeaderInDept().length]);
                weUser.setIsLeaderInDept(isLeaderInDeptArr);
            }

            if (message.getDepartments()!=null){
                String[] departmentsArr = Arrays.stream(message.getDepartments())
                        .collect(Collectors.toList()).toArray(new String[message.getDepartments().length]);
                weUser.setDepartment(departmentsArr);
            }
            weUserService.insertWeUser(weUser);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     * 删除企业成员
     *
     * @param message
     */
    @Transactional(rollbackFor = Exception.class)
    protected void delUser(WxCpXmlMessageVO message) {
        try {
            if (message.getUserId() != null) {
                String[] userIdArr = message.getUserId().split("");
                weUserService.deleteUser(userIdArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     * 修改成员信息
     *
     * @param message
     */
    @Transactional(rollbackFor = Exception.class)
    protected void updateUser(WxCpXmlMessageVO message) {
        try {

            WeUser weUser = WeUser.builder().userId(message.getUserId())
                    .email(message.getEmail())
                    .name(message.getName())
                    .alias(message.getAlias())
                    .gender(message.getGender())
                    .address(message.getAddress())
                    .telephone(message.getTelephone())
                    .mobile(message.getMobile())
                    .avatarMediaid(message.getAvatar())
                    .position(message.getPosition())
                    .build();
            if (message.getStatus()!=null){
                weUser.setIsActivate(Integer.valueOf(message.getStatus()));
            }
            if (message.getIsLeaderInDept()!=null){
                String[] isLeaderInDeptArr = Arrays.stream(message.getIsLeaderInDept())
                        .collect(Collectors.toList()).toArray(new String[message.getIsLeaderInDept().length]);
                weUser.setIsLeaderInDept(isLeaderInDeptArr);
            }

            if (message.getDepartments()!=null){
                String[] departmentsArr = Arrays.stream(message.getDepartments())
                        .collect(Collectors.toList()).toArray(new String[message.getDepartments().length]);
                weUser.setDepartment(departmentsArr);
            }
            weUserService.updateWeUser(weUser);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

}
