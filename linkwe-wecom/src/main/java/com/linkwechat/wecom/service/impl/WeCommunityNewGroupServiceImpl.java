package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.wecom.client.WeExternalContactClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.domain.vo.WeCommunityNewGroupVo;
import com.linkwechat.wecom.mapper.WeCommunityNewGroupMapper;
import com.linkwechat.wecom.mapper.WeGroupCodeMapper;
import com.linkwechat.wecom.service.IWeCommunityNewGroupService;
import com.linkwechat.wecom.service.IWeEmpleCodeTagService;
import com.linkwechat.wecom.service.IWeEmpleCodeUseScopService;
import com.linkwechat.wecom.service.IWeGroupCodeActualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@Service
@Slf4j
public class WeCommunityNewGroupServiceImpl extends ServiceImpl<WeCommunityNewGroupMapper, WeCommunityNewGroup> implements IWeCommunityNewGroupService {

    @Autowired
    private WeGroupCodeMapper weGroupCodeMapper;

    @Autowired
    private WeEmpleCodeServiceImpl weEmpleCodeService;

    @Autowired
    private WeCommunityNewGroupMapper weCommunityNewGroupMapper;

    @Autowired
    private IWeEmpleCodeTagService weEmpleCodeTagService;


    @Autowired
    private IWeEmpleCodeUseScopService iWeEmpleCodeUseScopService;

    @Autowired
    private WeExternalContactClient weExternalContactClient;

    @Autowired
    private IWeGroupCodeActualService weGroupCodeActualService;

    @Override
    public int add(WeCommunityNewGroupDto communityNewGroupDto) {

        //检查群活码是否存在
        WeGroupCode weGroupCode = weGroupCodeMapper.selectWeGroupCodeById(communityNewGroupDto.getGroupCodeId());
        if (null == weGroupCode) {
            throw new WeComException("群活码不存在！");
        }
        WeEmpleCode weEmpleCode = getWeEmpleCode(communityNewGroupDto);

        //生成员工活码信息
        WeExternalContactDto.WeContactWay weContactWay = weEmpleCodeService.getWeContactWay(weEmpleCode);
        WeExternalContactDto qrCode = weEmpleCodeService.getQrCode(weContactWay);

        //保存新客自动拉群信息
        WeCommunityNewGroup communityNewGroup = new WeCommunityNewGroup();
        communityNewGroup.setNewGroupId(weGroupCode.getId());
        communityNewGroup.setEmpleCodeName(communityNewGroupDto.getActivityScene());
        communityNewGroup.setDelFlag(0);
        communityNewGroup.setCreateTime(new Date());
        communityNewGroup.setCreateBy(SecurityUtils.getUsername());
        communityNewGroup.setActivityScene(communityNewGroupDto.getActivityScene());
        communityNewGroup.setWelcomeMsg(communityNewGroupDto.getWelcomeMsg());
        communityNewGroup.setIsJoinConfirmFriends(communityNewGroupDto.getIsJoinConfirmFriends());
        communityNewGroup.setMediaId(communityNewGroup.getMediaId());

        if (qrCode != null) {
            communityNewGroup.setQrCode(qrCode.getQr_code());
        }

        if (this.save(communityNewGroup)) {
            if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeUseScops())) {
                weEmpleCode.getWeEmpleCodeUseScops().forEach(item -> item.setEmpleCodeId(communityNewGroup.getNewGroupId()));
                iWeEmpleCodeUseScopService.saveBatch(weEmpleCode.getWeEmpleCodeUseScops());
            }
            if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeTags())) {
                weEmpleCode.getWeEmpleCodeTags().forEach(item -> item.setEmpleCodeId(communityNewGroup.getNewGroupId()));
                weEmpleCodeTagService.saveBatch(weEmpleCode.getWeEmpleCodeTags());
            }
        }

        return 1;

    }

    private WeEmpleCode getWeEmpleCode(WeCommunityNewGroupDto communityNewGroupDto) {
        WeEmpleCode weEmpleCode = new WeEmpleCode();
        weEmpleCode.setCodeType(2);
        weEmpleCode.setActivityScene(communityNewGroupDto.getActivityScene());
        weEmpleCode.setIsJoinConfirmFriends(communityNewGroupDto.getIsJoinConfirmFriends());
        weEmpleCode.setWelcomeMsg(communityNewGroupDto.getWelcomeMsg());
        weEmpleCode.setWeEmpleCodeUseScops(communityNewGroupDto.getWeEmpleCodeUseScops());
        weEmpleCode.setWeEmpleCodeTags(communityNewGroupDto.getWeEmpleCodeTags());
        weEmpleCode.setQrCode(communityNewGroupDto.getQrCode());
        weEmpleCode.setMediaId(communityNewGroupDto.getMediaId());
        return weEmpleCode;
    }

    @Override
    public List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(String empleCodeName, String createBy, String beginTime, String endTime) {
        List<WeCommunityNewGroupVo> weCommunityNewGroupVos = weCommunityNewGroupMapper.selectWeCommunityNewGroupList(empleCodeName, createBy, beginTime, endTime);
        if (CollectionUtil.isNotEmpty(weCommunityNewGroupVos)) {
            List<Long> newGroupIdList = weCommunityNewGroupVos.stream().map(WeCommunityNewGroupVo::getNewGroupId).collect(Collectors.toList());
            List<WeEmpleCodeUseScop> useScopList = iWeEmpleCodeUseScopService.selectWeEmpleCodeUseScopListByIds(newGroupIdList);
            List<WeEmpleCodeTag> tagList = weEmpleCodeTagService.selectWeEmpleCodeTagListByIds(newGroupIdList);
            weCommunityNewGroupVos.forEach(newGroup -> {
                List<WeGroupCodeActual> weGroupCodeActuals = getWeGroupCodeActuals(newGroup);
                newGroup.setWeGroupUserScops(weGroupCodeActuals);
                //活码使用人对象
                List<WeEmpleCodeUseScop> weEmpleCodeUseScopList = useScopList.stream()
                        .filter(useScop -> useScop.getEmpleCodeId().equals(newGroup.getNewGroupId())).collect(Collectors.toList());
                newGroup.setWeEmpleCodeUseScops(weEmpleCodeUseScopList);
                //员工活码标签对象
                newGroup.setWeEmpleCodeTags(tagList.stream()
                        .filter(tag -> tag.getEmpleCodeId().equals(newGroup.getNewGroupId())).collect(Collectors.toList()));
            });
        }
        return weCommunityNewGroupVos;
    }

    @Override
    public WeCommunityNewGroupVo selectWeCommunityNewGroupById(Long newGroupId) {
        WeCommunityNewGroupVo weCommunityNewGroupVo = weCommunityNewGroupMapper.selectWeCommunityNewGroupById(newGroupId);
        if (null != weCommunityNewGroupVo) {
            List<WeGroupCodeActual> weGroupCodeActuals = getWeGroupCodeActuals(weCommunityNewGroupVo);
            weCommunityNewGroupVo.setWeGroupUserScops(weGroupCodeActuals);
            List<WeEmpleCodeUseScop> useScopList = iWeEmpleCodeUseScopService.selectWeEmpleCodeUseScopListByIds(Lists.newArrayList(weCommunityNewGroupVo.getNewGroupId()));
            List<WeEmpleCodeTag> tagList = weEmpleCodeTagService.selectWeEmpleCodeTagListByIds(Lists.newArrayList(weCommunityNewGroupVo.getNewGroupId()));
            //活码使用人对象
            List<WeEmpleCodeUseScop> weEmpleCodeUseScopList = useScopList.stream()
                    .filter(useScop -> useScop.getEmpleCodeId().equals(weCommunityNewGroupVo.getNewGroupId())).collect(Collectors.toList());
            weCommunityNewGroupVo.setWeEmpleCodeUseScops(weEmpleCodeUseScopList);
            //员工活码标签对象
            weCommunityNewGroupVo.setWeEmpleCodeTags(tagList.stream()
                    .filter(tag -> tag.getEmpleCodeId().equals(weCommunityNewGroupVo.getNewGroupId())).collect(Collectors.toList()));
        }
        return weCommunityNewGroupVo;
    }

    private List<WeGroupCodeActual> getWeGroupCodeActuals(WeCommunityNewGroupVo weCommunityNewGroupVo) {
        WeGroupCodeActual weGroupCodeActual = new WeGroupCodeActual();
        weGroupCodeActual.setGroupCodeId(weCommunityNewGroupVo.getNewGroupId());
        return weGroupCodeActualService.selectWeGroupCodeActualList(weGroupCodeActual);
    }

    @Override
    public int updateWeCommunityNewGroup(WeCommunityNewGroupDto communityNewGroupDto) {

        //检查群活码是否存在
        WeGroupCode weGroupCode = weGroupCodeMapper.selectWeGroupCodeById(communityNewGroupDto.getGroupCodeId());
        if (null != weGroupCode) {
            throw new WeComException("群活码不存在！");
        }

        //查询新客自动拉群信息
        WeCommunityNewGroup communityNewGroup = weCommunityNewGroupMapper.selectById(communityNewGroupDto.getGroupCodeId());
        if (null != communityNewGroup) {
            throw new WeComException("信息不存在！");
        }

        //更新员工活码信息
        WeEmpleCode weEmpleCode = getWeEmpleCode(communityNewGroupDto);

        WeExternalContactDto.WeContactWay weContactWay = weEmpleCodeService.getWeContactWay(weEmpleCode);
        try {
            weExternalContactClient.updateContactWay(weContactWay);
        } catch (Exception e) {
            e.printStackTrace();
        }

        communityNewGroup.setEmpleCodeName(communityNewGroupDto.getActivityScene());
        communityNewGroup.setDelFlag(0);
        communityNewGroup.setCreateTime(new Date());
        communityNewGroup.setCreateBy(SecurityUtils.getUsername());
        communityNewGroup.setActivityScene(communityNewGroupDto.getActivityScene());
        communityNewGroup.setWelcomeMsg(communityNewGroupDto.getWelcomeMsg());
        communityNewGroup.setIsJoinConfirmFriends(communityNewGroupDto.getIsJoinConfirmFriends());
        communityNewGroup.setMediaId(communityNewGroup.getMediaId());
        communityNewGroup.setGroupCodeId(communityNewGroup.getGroupCodeId());

        //更新新客自动拉群信息
        if (this.updateById(communityNewGroup)) {
            if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeUseScops())) {
                weEmpleCode.getWeEmpleCodeUseScops().forEach(item -> item.setEmpleCodeId(communityNewGroup.getNewGroupId()));
                iWeEmpleCodeUseScopService.updateBatchById(weEmpleCode.getWeEmpleCodeUseScops());
            }
            if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeTags())) {
                weEmpleCode.getWeEmpleCodeTags().forEach(item -> item.setEmpleCodeId(communityNewGroup.getNewGroupId()));
                weEmpleCodeTagService.updateBatchById(weEmpleCode.getWeEmpleCodeTags());
            }
        }

        return 1;
    }

    @Override
    public int batchRemoveWeCommunityNewGroupIds(List<String> idList) {
        return weCommunityNewGroupMapper.batchRemoveWeCommunityNewGroupIds(idList);
    }

    @Override
    public WeCommunityNewGroupVo selectWeCommunityNewGroupById(long id) {
        return weCommunityNewGroupMapper.selectWeCommunityNewGroupById(id);
    }

    @Override
    public List<WeCommunityNewGroupVo> selectWeCommunityNewGroupByIds(List<String> ids) {
        return weCommunityNewGroupMapper.selectWeCommunityNewGroupByIds(ids);
    }
}
