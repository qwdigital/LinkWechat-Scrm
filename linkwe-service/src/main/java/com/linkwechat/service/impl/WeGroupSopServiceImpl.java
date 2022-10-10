package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.community.WeGroupSop;
import com.linkwechat.domain.community.WeGroupSopChat;
import com.linkwechat.domain.community.WeGroupSopMaterial;
import com.linkwechat.domain.community.WeGroupSopPic;
import com.linkwechat.domain.community.vo.WeCommunityTaskEmplVo;
import com.linkwechat.domain.community.vo.WeGroupSopVo;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import com.linkwechat.mapper.*;
import com.linkwechat.service.IWeGroupSopService;
import com.linkwechat.service.IWeMessagePushService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeGroupSopServiceImpl extends ServiceImpl<WeGroupSopMapper, WeGroupSop> implements IWeGroupSopService {

    @Autowired
    private WeGroupSopChatMapper sopChatMapper;

    @Autowired
    private WeGroupSopPicMapper sopPicMapper;

    @Autowired
    private WeGroupSopMaterialMapper sopMaterialMapper;

    @Autowired
    private WeGroupMapper groupMapper;

    @Autowired
    private WeMaterialMapper materialMapper;

    @Autowired
    private IWeMessagePushService weMessagePushService;


    @Override
    public boolean isNameOccupied(String ruleName) {
        List<WeGroupSop> weGroupSops = list(new LambdaQueryWrapper<WeGroupSop>()
                .eq(WeGroupSop::getRuleName, ruleName));

        if(CollectionUtil.isNotEmpty(weGroupSops)){
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public void addGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picList) {

        if (this.save(weGroupSop)) {
            Long ruleId = weGroupSop.getRuleId();
            // 保存群聊及素材关联
            this.saveChatAndMaterialBinds(ruleId, groupIdList, materialIdList);
            // 保存手动上传的图片素材
            List<WeGroupSopPic> sopPicList = picList.stream().map(picUrl -> new WeGroupSopPic(ruleId, picUrl)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(sopPicList)) {
                sopPicMapper.batchSopPic(sopPicList);
            }
        }
        // 添加成功后进行异步消息推送
        this.sendMessage(groupIdList);

    }

    @Override
    @Transactional
    public void batchRemoveGroupSopByIds(Long[] ids) {

        if(removeByIds(Arrays.asList(ids))){

            sopChatMapper.delete(new LambdaQueryWrapper<WeGroupSopChat>()
                    .in(WeGroupSopChat::getRuleId, Arrays.asList(ids)));

            sopMaterialMapper.delete(new LambdaQueryWrapper<WeGroupSopMaterial>()
                    .in(WeGroupSopMaterial::getRuleId, Arrays.asList(ids)));

            sopPicMapper.delete(new LambdaQueryWrapper<WeGroupSopPic>()
                    .in(WeGroupSopPic::getRuleId, Arrays.asList(ids)));

        }


    }

    @Override
    @Transactional
    public void updateGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picList) {
        if (this.updateById(weGroupSop)) {
            Long ruleId = weGroupSop.getRuleId();
            // 先删除旧数据
            this.deleteChatAndMaterialBinds(ruleId);
            // 再插入新数据
            this.saveChatAndMaterialBinds(ruleId, groupIdList, materialIdList);
            // 删除旧图片
            LambdaQueryWrapper<WeGroupSopPic> queryWrapper = new LambdaQueryWrapper<>();
            sopPicMapper.delete(queryWrapper.eq(WeGroupSopPic::getRuleId, ruleId));

            // 保留新上传的图片
            List<WeGroupSopPic> sopPicList = picList.stream().map(picUrl -> new WeGroupSopPic(ruleId, picUrl)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(sopPicList)) {
                sopPicMapper.batchSopPic(sopPicList);
            }
        }

    }

    @Override
    public WeGroupSopVo getGroupSopById(Long ruleId) {
        WeGroupSopVo weGroupSopVo=new WeGroupSopVo();
        WeGroupSop weGroupSop = getById(ruleId);
        if(null != weGroupSop){
            BeanUtils.copyProperties(weGroupSop,weGroupSopVo);
            setChatAndMaterialAndPicList(weGroupSopVo);
        }
        return weGroupSopVo;
    }

    @Override
    public List<WeGroupSopVo> getGroupSopList(String ruleName, String createBy, String beginTime, String endTime) {
        List<WeGroupSopVo> groupSopVoList = this.baseMapper.getGroupSopList(ruleName, createBy, beginTime, endTime);
        groupSopVoList.forEach(this::setChatAndMaterialAndPicList);
        return groupSopVoList;
    }

    @Override
    public void sendMessage(List<String> groupIdList) {
        // 查询群聊列表，获取群主列表
        LambdaQueryWrapper<WeGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(WeGroup::getChatId, groupIdList);
        List<WeGroup> groupList = groupMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(groupList)){

            weMessagePushService.pushMessageSelfH5(
                    groupList.stream().map(WeGroup::getOwner).collect(Collectors.toList()),
                    "【任务动态】<br/> 您有一项「群SOP」任务待完成，请尽快处理",
                    MessageNoticeType.SOP.getType(),true
            );

        }


    }

    @Override
    public List<WeGroupSopVo> getEmplTaskList(String emplId, boolean isDone) {
        List<WeGroupSopVo> sopVoList = this.baseMapper.getEmplTaskList(emplId, isDone);
        sopVoList.forEach(this::setChatAndMaterialAndPicList);
        return sopVoList;
    }

    @Override
    public int updateChatSopStatus(Long ruleId, String emplId) {
        return this.baseMapper.updateChatSopStatus(ruleId,emplId);
    }

    @Override
    public List<WeCommunityTaskEmplVo> getScopeListByRuleId(Long ruleId) {
        return this.baseMapper.getScopeListByRuleId(ruleId);
    }

    //根据关联条件查询该sop所关联的群聊及素材对象，并将其放入WeGroupSopVo相应属性中用于前端使用
    private void setChatAndMaterialAndPicList(WeGroupSopVo groupSopVo) {

        Long ruleId = groupSopVo.getRuleId();

        // 获取对应群聊信息
        List<WeGroup> groupList = this.getGroupListByRuleId(ruleId);
        if (StringUtils.isNotEmpty(groupList)) {
            groupSopVo.setGroupList(groupList);
        }

        // 设置执行人信息
        List<WeCommunityTaskEmplVo> scopeList = sopChatMapper.getScopeListByRuleId(ruleId);
        if (StringUtils.isNotEmpty(scopeList)) {
            groupSopVo.setScopeList(scopeList);
        }

        // 获取对应素材信息
        List<Long> materialIdList = this.baseMapper.getMaterialIdListByRuleId(ruleId);
        if (StringUtils.isNotEmpty(materialIdList)) {
            List<WeMaterialVo> materialList = materialMapper.findMaterialVoListByIds(materialIdList.toArray(new Long[0]));
            groupSopVo.setMaterialList(materialList);
        }

        // 设置图片列表
        List<WeGroupSopPic> sopPicList = sopPicMapper.selectList(
                new LambdaQueryWrapper<WeGroupSopPic>().eq(WeGroupSopPic::getRuleId, ruleId)
        );
        if (StringUtils.isNotEmpty(sopPicList)) {
            List<String> picUrlList = sopPicList.stream().map(WeGroupSopPic::getPicUrl).collect(Collectors.toList());
            groupSopVo.setPicList(picUrlList);
        }

    }

    /**
     * 根据规则id获取对应的群聊信息
     *
     * @param ruleId 规则id
     * @return 群聊信息列表
     */
    private List<WeGroup> getGroupListByRuleId(Long ruleId) {
        LambdaQueryWrapper<WeGroup> groupQueryWrapper = new LambdaQueryWrapper<>();
        List<String> chatIdList = this.baseMapper.getChatIdListByRuleId(ruleId);
        List<WeGroup> groupList = new ArrayList<>();
        if (StringUtils.isNotEmpty(chatIdList)) {
            groupQueryWrapper.in(WeGroup::getChatId, chatIdList);
            groupList = groupMapper.selectList(groupQueryWrapper);
        }
        return groupList;
    }

    /**
     * 解除该sop规则对应的群聊及素材关联
     *
     * @param ruleId 规则id
     */
    private void deleteChatAndMaterialBinds(Long ruleId) {
        // 删除群聊绑定
        LambdaQueryWrapper<WeGroupSopChat> chatQueryWrapper = new LambdaQueryWrapper<>();
        chatQueryWrapper.eq(WeGroupSopChat::getRuleId, ruleId);
        sopChatMapper.delete(chatQueryWrapper);
        // 删除素材绑定
        LambdaQueryWrapper<WeGroupSopMaterial> materialQueryWrapper = new LambdaQueryWrapper<>();
        materialQueryWrapper.eq(WeGroupSopMaterial::getRuleId, ruleId);
        sopMaterialMapper.delete(materialQueryWrapper);
    }


    /**
     * 保存该sop规则对应的群聊及素材关联对象
     *
     * @param ruleId         规则id
     * @param chatIdList     群聊id列表
     * @param materialIdList 素材id列表
     */
    private void saveChatAndMaterialBinds(Long ruleId, List<String> chatIdList, List<Long> materialIdList) {
        if (StringUtils.isNotEmpty(chatIdList)) {
            List<WeGroupSopChat> sopChatList = chatIdList
                    .stream()
                    .map(id ->{
                        WeGroupSopChat weGroupSopChat = new WeGroupSopChat();
                        weGroupSopChat.setId(SnowFlakeUtil.nextId());
                        weGroupSopChat.setRuleId(ruleId);
                        weGroupSopChat.setChatId(id);
                        weGroupSopChat.setDone(false);
                        weGroupSopChat.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
                        weGroupSopChat.setCreateTime(new Date());
                        weGroupSopChat.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
                        weGroupSopChat.setUpdateTime(new Date());
                        return weGroupSopChat;
                    })
                    .collect(Collectors.toList());
            sopChatMapper.batchBindsSopChat(sopChatList);
        }
        if (StringUtils.isNotEmpty(materialIdList)) {

            List<WeGroupSopMaterial> weGroupSopMaterials = materialIdList
                    .stream()
                    .map(id -> WeGroupSopMaterial.builder().id(SnowFlakeUtil.nextId()).ruleId(ruleId).materialId(id).build())
                    .collect(Collectors.toList());
            sopMaterialMapper.batchBindsSopMaterial(weGroupSopMaterials);
        }
    }
}
