package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.vo.WeGroupSopVo;
import com.linkwechat.wecom.mapper.*;
import com.linkwechat.wecom.service.IWeGroupSopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WeGroupSopServiceImpl extends ServiceImpl<WeGroupSopMapper, WeGroupSop> implements IWeGroupSopService {

    @Autowired
    private WeGroupSopMapper groupSopMapper;

    @Autowired
    private WeGroupSopChatMapper sopChatMapper;

    @Autowired
    private WeMaterialMapper materialMapper;

    @Autowired
    private WeGroupSopMaterialMapper sopMaterialMapper;

    @Autowired
    private WeGroupMapper groupMapper;

    @Autowired
    private WeGroupSopPicMapper sopPicMapper;

    /**
     * 通过规则id获取sop规则
     *
     * @param ruleId 规则id
     * @return 结果
     */
    @Override
    public WeGroupSopVo getGroupSopById(Long ruleId) {
        WeGroupSopVo groupSopVo = groupSopMapper.getGroupSopById(ruleId);
        this.setChatAndMaterial(groupSopVo);
        // 设置手动上传的图片url
        this.setPicList(groupSopVo);
        return groupSopVo;
    }

    /**
     * 通过过滤条件获取群sop列表
     *
     * @param ruleName  规则名称
     * @param createBy  创建者
     * @param beginTime 创建区间 - 开始时间
     * @param endTime   创建区间 - 结束时间
     * @return 群sop规则列表
     */
    @Override
    public List<WeGroupSopVo> getGroupSopList(String ruleName, String createBy, String beginTime, String endTime) {
        List<WeGroupSopVo> groupSopVoList = groupSopMapper.getGroupSopList(ruleName, createBy, beginTime, endTime);
        for (WeGroupSopVo groupSopVo : groupSopVoList) {
            this.setChatAndMaterial(groupSopVo);
            // 设置手动上传的图片url
            this.setPicList(groupSopVo);

        }
        return groupSopVoList;
    }

    /**
     * 新增群sop
     *
     * @param weGroupSop     新增所用数据
     * @param groupIdList    选中的群聊id
     * @param materialIdList 素材
     * @param picList        手动上传的图片URL
     * @return 结果
     */
    @Override
    @Transactional
    public int addGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picList) {
        if (this.save(weGroupSop)) {
            Long ruleId = weGroupSop.getRuleId();
            // 保存群聊及素材关联
            this.saveChatAndMaterialBinds(ruleId, groupIdList, materialIdList);
            // 保存手动上传的图片素材
            List<WeGroupSopPic> sopPicList = picList.stream().map(e -> new WeGroupSopPic(ruleId, e)).collect(Collectors.toList());
            sopPicMapper.batchSopPic(sopPicList);
            return 1;
        }
        return 0;
    }

    /**
     * 更新群sop
     *
     * @param weGroupSop     更新所用数据
     * @param groupIdList    选中的群聊id
     * @param materialIdList 素材
     * @param picList        手动上传的图片URL
     * @return 结果
     */
    @Override
    @Transactional
    public int updateGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picList) {
        if (this.updateById(weGroupSop)) {
            Long ruleId = weGroupSop.getRuleId();
            // 先删除旧数据
            this.deleteChatAndMaterialBinds(ruleId);
            // 再插入新数据
            this.saveChatAndMaterialBinds(ruleId, groupIdList, materialIdList);
            // 删除旧图片
            QueryWrapper<WeGroupSopPic> queryWrapper = new QueryWrapper<>();
            sopPicMapper.delete(queryWrapper.eq("rule_id", ruleId));

            // 保留新上传的图片
            List<WeGroupSopPic> sopPicList = picList.stream().map(e -> new WeGroupSopPic(ruleId, e)).collect(Collectors.toList());
            sopPicMapper.batchSopPic(sopPicList);
            return 1;
        }
        return 0;
    }

    /**
     * 批量删除群sop
     *
     * @param ids sop规则id列表
     * @return 结果
     */
    @Override
    @Transactional
    public int batchRemoveGroupSopByIds(Long[] ids) {
        int affectedRows = groupSopMapper.deleteBatchIds(Arrays.asList(ids));
        if (affectedRows > 0) {
            // 解除群聊和素材关联
            QueryWrapper<WeGroupSopChat> sopGroupQueryWrapper = new QueryWrapper<>();
            sopGroupQueryWrapper.in("rule_id", Arrays.asList(ids));
            sopChatMapper.delete(sopGroupQueryWrapper);
            QueryWrapper<WeGroupSopMaterial> sopMaterialQueryWrapper = new QueryWrapper<>();
            sopMaterialQueryWrapper.in("rule_id", Arrays.asList(ids));
            sopMaterialMapper.delete(sopMaterialQueryWrapper);

            // 删除手动上传的图片
            QueryWrapper<WeGroupSopPic> picQueryWrapper = new QueryWrapper<>();
            picQueryWrapper.in("rule_id", Arrays.asList(ids));
            sopPicMapper.delete(picQueryWrapper);
        }
        return affectedRows;
    }

    /**
     * 校验规则名是否唯一
     *
     * @param ruleName 规则名
     * @return 是否唯一
     */
    @Override
    public boolean isRuleNameUnique(String ruleName) {
        return groupSopMapper.isRuleNameUnique(ruleName) == 0;
    }


    /**
     * 根据关联条件查询该sop所关联的群聊及素材对象，并将其放入WeGroupSopVo相应属性中用于前端使用
     *
     * @param groupSopVo 目标sop规则
     */
    private void setChatAndMaterial(WeGroupSopVo groupSopVo) {
        Long ruleId = groupSopVo.getRuleId();

        // 获取对应群聊信息
        QueryWrapper<WeGroup> groupQueryWrapper = new QueryWrapper<>();
        List<String> chatIdList = groupSopMapper.getChatIdListByRuleId(ruleId);
        if (StringUtils.isNotEmpty(chatIdList)) {
            groupQueryWrapper.in("chat_id", chatIdList);
            List<WeGroup> groupList = groupMapper.selectList(groupQueryWrapper);
            groupSopVo.setGroupList(groupList);
        }
        // 获取对应素材信息
        QueryWrapper<WeMaterial> materialQueryWrapper = new QueryWrapper<>();
        List<Long> materialIdList = groupSopMapper.getMaterialIdListByRuleId(ruleId);
        if (StringUtils.isNotEmpty(materialIdList)) {
            materialQueryWrapper.in("id", materialIdList);
            List<WeMaterial> materialList = materialMapper.selectList(materialQueryWrapper);
            groupSopVo.setMaterialList(materialList);
        }
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
                    .map(id -> new WeGroupSopChat(ruleId, id))
                    .collect(Collectors.toList());
            sopChatMapper.batchBindsSopChat(sopChatList);
        }
        if (StringUtils.isNotEmpty(materialIdList)) {
            List<WeGroupSopMaterial> materialList = materialIdList
                    .stream()
                    .map(id -> new WeGroupSopMaterial(ruleId, id))
                    .collect(Collectors.toList());
            sopMaterialMapper.batchBindsSopMaterial(materialList);
        }
    }

    /**
     * 解除该sop规则对应的群聊及素材关联
     *
     * @param ruleId 规则id
     */
    private void deleteChatAndMaterialBinds(Long ruleId) {
        QueryWrapper<WeGroupSopChat> sopChatQueryWrapper = new QueryWrapper<>();
        sopChatQueryWrapper.eq("rule_id", ruleId);
        sopChatMapper.delete(sopChatQueryWrapper);
        QueryWrapper<WeGroupSopMaterial> sopMaterialQueryWrapper = new QueryWrapper<>();
        sopMaterialQueryWrapper.eq("rule_id", ruleId);
        sopMaterialMapper.delete(sopMaterialQueryWrapper);
    }

    /**
     * 为WeGroupSopVo查询手动上传的图片列表
     *
     * @param sopVo 代操作对象
     */
    private void setPicList(WeGroupSopVo sopVo) {
        Long ruleId = sopVo.getRuleId();
        QueryWrapper<WeGroupSopPic> picQueryWrapper = new QueryWrapper<>();
        picQueryWrapper.eq("rule_id", ruleId);
        List<WeGroupSopPic> sopPicList = sopPicMapper.selectList(picQueryWrapper);
        List<String> picList = sopPicList.stream().map(WeGroupSopPic::getPicUrl).collect(Collectors.toList());
        sopVo.setPicList(picList);
    }
}
