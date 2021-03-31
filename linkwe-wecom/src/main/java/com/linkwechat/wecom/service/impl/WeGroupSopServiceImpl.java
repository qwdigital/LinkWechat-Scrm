package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.enums.CommunityTaskType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeMessagePushClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.TextMessageDto;
import com.linkwechat.wecom.domain.vo.WeGroupSopVo;
import com.linkwechat.wecom.domain.vo.WeCommunityTaskEmplVo;
import com.linkwechat.wecom.domain.vo.WeMaterialVo;
import com.linkwechat.wecom.mapper.*;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IWeGroupSopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.ArrayList;
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

    @Autowired
    private IWeCorpAccountService corpAccountService;

    @Autowired
    private WeMessagePushClient messagePushClient;

    @Value("${wecome.authorizeUrl")
    private String authorizeUrl;

    @Value("${wecome.authorizeRedirectUrl")
    private String authorizeRedirectUrl;

    @Value("${wecome.callBack.appIdOrCorpId}")
    private String corpId;

    /**
     * 通过规则id获取sop规则
     *
     * @param ruleId 规则id
     * @return 结果
     */
    @Override
    public WeGroupSopVo getGroupSopById(Long ruleId) {
        WeGroupSopVo groupSopVo = groupSopMapper.getGroupSopById(ruleId);
        this.setChatAndMaterialAndPicList(groupSopVo);
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
        groupSopVoList.forEach(this::setChatAndMaterialAndPicList);
        return groupSopVoList;
    }

    /**
     * 新增群sop
     *
     * @param weGroupSop     新增所用数据
     * @param groupIdList    选中的群聊id
     * @param materialIdList 素材
     * @param picUrlList     手动上传的图片URL
     * @return 结果
     */
    @Override
    @Transactional
    public int addGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picUrlList) {
        if (this.save(weGroupSop)) {
            Long ruleId = weGroupSop.getRuleId();
            // 保存群聊及素材关联
            this.saveChatAndMaterialBinds(ruleId, groupIdList, materialIdList);
            // 保存手动上传的图片素材
            List<WeGroupSopPic> sopPicList = picUrlList.stream().map(picUrl -> new WeGroupSopPic(ruleId, picUrl)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(sopPicList)) {
                sopPicMapper.batchSopPic(sopPicList);
            }
            this.sendMessage(ruleId);
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
     * @param picUrlList     手动上传的图片URL
     * @return 结果
     */
    @Override
    @Transactional
    public int updateGroupSop(WeGroupSop weGroupSop, List<String> groupIdList, List<Long> materialIdList, List<String> picUrlList) {
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
            List<WeGroupSopPic> sopPicList = picUrlList.stream().map(picUrl -> new WeGroupSopPic(ruleId, picUrl)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(sopPicList)) {
                sopPicMapper.batchSopPic(sopPicList);
            }
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
        List<Long> materialIdList = groupSopMapper.getMaterialIdListByRuleId(ruleId);
        if (StringUtils.isNotEmpty(materialIdList)) {
            List<WeMaterialVo> materialList = materialMapper.findMaterialVoListByIds(materialIdList.toArray(new Long[0]));
            groupSopVo.setMaterialList(materialList);
        }

        // 设置图片列表
        QueryWrapper<WeGroupSopPic> picQueryWrapper = new QueryWrapper<>();
        picQueryWrapper.eq("rule_id", ruleId);
        List<WeGroupSopPic> sopPicList = sopPicMapper.selectList(picQueryWrapper);
        if (StringUtils.isNotEmpty(sopPicList)) {
            List<String> picUrlList = sopPicList.stream().map(WeGroupSopPic::getPicUrl).collect(Collectors.toList());
            groupSopVo.setPicList(picUrlList);
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
                    .map(id -> new WeGroupSopChat(ruleId, id, false))
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
     *  根据员工id获取对应的sop任务列表
     * @param emplId 员工id
     * @param isDone 已完成还是待处理
     * @return 结果
     */
    @Override
    public List<WeGroupSopVo> getEmplTaskList(String emplId, boolean isDone) {
        List<WeGroupSopVo> sopVoList = groupSopMapper.getEmplTaskList(emplId, isDone);
        sopVoList.forEach(this::setChatAndMaterialAndPicList);
        return sopVoList;
    }

    /**
     * 变更某员工sop规则发送任务的状态
     *
     * @param ruleId 规则名称
     * @param emplId 群聊的群主id
     * @return 结果
     */
    @Override
    public int updateChatSopStatus(Long ruleId, String emplId) {
        return sopChatMapper.updateChatSopStatus(ruleId, emplId);
    }

    /**
     * 根据SOP 规则id获取所有使用人员信息
     *
     * @param ruleId sop id
     * @return 结果
     */
    @Override
    public List<WeCommunityTaskEmplVo> getScopeListByRuleId(Long ruleId) {
        return sopChatMapper.getScopeListByRuleId(ruleId);
    }

    /**
     * 根据规则id获取对应的群聊信息
     *
     * @param ruleId 规则id
     * @return 群聊信息列表
     */
    private List<WeGroup> getGroupListByRuleId(Long ruleId) {
        QueryWrapper<WeGroup> groupQueryWrapper = new QueryWrapper<>();
        List<String> chatIdList = groupSopMapper.getChatIdListByRuleId(ruleId);
        List<WeGroup> groupList = new ArrayList<>();
        if (StringUtils.isNotEmpty(chatIdList)) {
            groupQueryWrapper.in("chat_id", chatIdList);
            groupList = groupMapper.selectList(groupQueryWrapper);
        }
        return groupList;
    }

    /**
     * 消息推送(企微API 消息推送 - 发送应用消息)
     */
    private void sendMessage(Long ruleId) {
        // 构造请求参数
        WeMessagePushDto pushDto = new WeMessagePushDto();
        // 根据群聊获取员工(群主)列表
        List<WeGroup> groupList = this.getGroupListByRuleId(ruleId);
        String toUser = groupList.stream().map(WeGroup::getOwner).collect(Collectors.joining("|"));
        pushDto.setTouser(toUser);

        // 获取agentId
        WeCorpAccount validWeCorpAccount = corpAccountService.findValidWeCorpAccount();
        String agentId = validWeCorpAccount.getAgentId();
        if (StringUtils.isEmpty(agentId)) {
            throw new WeComException("当前agentId不可用或不存在");
        }
        pushDto.setAgentid(Integer.valueOf(agentId));

        // 设置消息内容
        pushDto.setMsgtype("text");

        TextMessageDto text = new TextMessageDto();
        String REDIRECT_URI = URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s&type=%s", authorizeRedirectUrl, corpId, agentId, CommunityTaskType.SOP.getType()));
        String context = String.format(
                "你有一个新任务，<a href='%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect'>请点击此链接查看</a>",
                authorizeUrl, corpId, REDIRECT_URI);
        text.setContent(context);
        pushDto.setText(text);

        // 请求消息推送接口，获取结果 [消息推送 - 发送应用消息]
        messagePushClient.sendMessageToUser(pushDto, agentId);

    }
}
