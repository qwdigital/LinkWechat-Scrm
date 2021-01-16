package com.linkwechat.wecom.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.elastic.ElasticSearchEntity;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.client.WeCustomerGroupClient;
import com.linkwechat.wecom.client.WeMsgAuditClient;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.domain.WeChatContactMapping;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeGroupMemberDto;
import com.linkwechat.wecom.domain.dto.customer.CustomerGroupDetail;
import com.linkwechat.wecom.mapper.WeChatContactMappingMapper;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.service.IWeChatContactMappingService;
import com.linkwechat.wecom.service.IWeConversationArchiveService;
import com.linkwechat.wecom.service.IWeGroupMemberService;
import com.linkwechat.wecom.service.IWeGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 聊天关系映射Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-27
 */
@Slf4j
@Service
public class WeChatContactMappingServiceImpl extends ServiceImpl<WeChatContactMappingMapper, WeChatContactMapping> implements IWeChatContactMappingService {
    @Autowired
    private WeChatContactMappingMapper weChatContactMappingMapper;
    @Autowired
    private WeUserMapper weUserMapper;
    @Autowired
    private WeCustomerMapper weCustomerMapper;
    @Autowired
    private WeUserClient weUserClient;
    @Autowired
    private WeCustomerClient weCustomerClient;
    @Autowired
    private WeCustomerGroupClient weCustomerGroupClient;
    @Autowired
    private WeMsgAuditClient weMsgAuditClient;
    @Autowired
    private IWeConversationArchiveService weConversationArchiveService;
    @Autowired
    private IWeGroupService weGroupService;
    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    /**
     * 查询聊天关系映射
     *
     * @param id 聊天关系映射ID
     * @return 聊天关系映射
     */
    @Override
    public WeChatContactMapping selectWeChatContactMappingById(Long id) {
        return weChatContactMappingMapper.selectWeChatContactMappingById(id);
    }

    /**
     * 查询聊天关系映射列表
     *
     * @param weChatContactMapping 聊天关系映射
     * @return 聊天关系映射
     */
    @Override
    public List<WeChatContactMapping> selectWeChatContactMappingList(WeChatContactMapping weChatContactMapping) {
        List<WeChatContactMapping> weChatMappingList = weChatContactMappingMapper.selectWeChatContactMappingList(weChatContactMapping);
        Optional.ofNullable(weChatMappingList).ifPresent(weChatMappingListVo -> {
            weChatMappingListVo.stream().forEach(item -> {
                if (StringUtils.isNotEmpty(item.getReceiveId())) {
                    if (WeConstans.ID_TYPE_USER.equals(item.getIsCustom())) {
                        //成员信息
                        item.setReceiveWeUser(weUserMapper.selectOne(new LambdaQueryWrapper<WeUser>().eq(WeUser::getUserId, item.getReceiveId())));
                    } else if (WeConstans.ID_TYPE_EX.equals(item.getIsCustom())) {
                        //获取外部联系人信息
                        item.setReceiveWeCustomer(weCustomerMapper.selectWeCustomerById(item.getReceiveId()));
                    } else if (WeConstans.ID_TYPE_MACHINE.equals(item.getIsCustom())) {
                        //拉去机器人信息暂不处理
                    }
                    item.setFinalChatContext(weConversationArchiveService.getFinalChatContactInfo(item.getFromId(), item.getReceiveId()));
                } else if (StringUtils.isNotEmpty(item.getRoomId())) {
                    //获取群信息
                    WeGroup weGroup = weGroupService.getOne(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getChatId, item.getRoomId()));
                    //查询群成员头像
                    if (weGroup!=null){
                        List<WeGroupMemberDto> weGroupMemberDtos = weGroupMemberService.selectWeGroupMemberListByChatId(item.getRoomId());
                        String roomAvatar = weGroupMemberDtos.stream()
                                .map(WeGroupMemberDto::getMemberAvatar)
                                .filter(StringUtils::isNotEmpty)
                                .limit(9)
                                .collect(Collectors.joining(","));
                        weGroup.setAvatar(roomAvatar);
                        item.setRoomInfo(weGroup);
                    }
                    item.setFinalChatContext(weConversationArchiveService.getFinalChatRoomContactInfo(item.getFromId(), item.getRoomId()));
                }
            });
        });
        return weChatMappingList;
    }

    /**
     * 新增聊天关系映射
     *
     * @param weChatContactMapping 聊天关系映射
     * @return 结果
     */
    @Override
    public int insertWeChatContactMapping(WeChatContactMapping weChatContactMapping) {
        List<WeChatContactMapping> list = weChatContactMappingMapper.selectWeChatContactMappingList(weChatContactMapping);
        if (CollectionUtil.isEmpty(list)) {
            return weChatContactMappingMapper.insertWeChatContactMapping(weChatContactMapping);
        }
        return 0;
    }

    /**
     * 修改聊天关系映射
     *
     * @param weChatContactMapping 聊天关系映射
     * @return 结果
     */
    @Override
    public int updateWeChatContactMapping(WeChatContactMapping weChatContactMapping) {
        return weChatContactMappingMapper.updateWeChatContactMapping(weChatContactMapping);
    }

    /**
     * 批量删除聊天关系映射
     *
     * @param ids 需要删除的聊天关系映射ID
     * @return 结果
     */
    @Override
    public int deleteWeChatContactMappingByIds(Long[] ids) {
        return weChatContactMappingMapper.deleteWeChatContactMappingByIds(ids);
    }

    /**
     * 删除聊天关系映射信息
     *
     * @param id 聊天关系映射ID
     * @return 结果
     */
    @Override
    public int deleteWeChatContactMappingById(Long id) {
        return weChatContactMappingMapper.deleteWeChatContactMappingById(id);
    }

    /**
     * 需后期优化
     *
     * @param query
     */
    @Override
    public List<JSONObject> saveWeChatContactMapping(List<JSONObject> query) {
        List<JSONObject> resultList = new ArrayList<>();
        query.stream().filter(chatData -> StringUtils.isNotEmpty(chatData.getString("from"))).forEach(chatData -> {
            //发送人映射数据
            WeChatContactMapping fromWeChatContactMapping = new WeChatContactMapping();
            String fromId = chatData.getString("from");
            //发送人类型
            int fromType = StringUtils.weCustomTypeJudgment(fromId);
            fromWeChatContactMapping.setFromId(fromId);
            getUserOrCustomerInfo(chatData, fromId, fromType, "fromInfo");

            JSONArray tolist = chatData.getJSONArray("tolist");
            if (CollectionUtil.isNotEmpty(tolist)) {
                //如果是单聊，tolist唯一，只有一个接收人，并判断接收人的类型
                if (StringUtils.isEmpty(chatData.getString("roomid"))) {
                    String idStr = String.valueOf(tolist.get(0));
                    //接收人类型
                    int reveiceType = StringUtils.weCustomTypeJudgment(idStr);
                    fromWeChatContactMapping.setReceiveId(idStr);
                    //获取接收人信息
                    getUserOrCustomerInfo(chatData, idStr, reveiceType, "toListInfo");
                    fromWeChatContactMapping.setIsCustom(reveiceType);
                } else {
                    fromWeChatContactMapping.setRoomId(chatData.getString("roomid"));
                    WeGroup weGroup = weGroupService.getOne(new LambdaQueryWrapper<WeGroup>().eq(WeGroup::getChatId, chatData.getString("roomid")));
                    chatData.put("roomInfo",JSONObject.parse(JSONObject.toJSONString(weGroup)));
                }
            }
            //接收人映射数据
            WeChatContactMapping reveiceWeChatContactMapping = from2ReveiceData(fromType, fromWeChatContactMapping);
            insertWeChatContactMapping(fromWeChatContactMapping);
            insertWeChatContactMapping(reveiceWeChatContactMapping);
            resultList.add(chatData);
        });
        return resultList;
    }

    private void getUserOrCustomerInfo(JSONObject chatData, String fromId, int fromType, String key) {
        //获取发送人信息
        if (WeConstans.ID_TYPE_USER.equals(fromType)) {
            //成员信息
            WeUser weUser = weUserMapper.selectOne(new LambdaQueryWrapper<WeUser>().eq(WeUser::getUserId, fromId));
            chatData.put(key, JSONObject.parse(JSONObject.toJSONString(weUser)));
        } else if (WeConstans.ID_TYPE_EX.equals(fromType)) {
            //获取外部联系人信息
            WeCustomer weCustomer = weCustomerMapper.selectWeCustomerById(fromId);
            chatData.put(key,JSONObject.parse(JSONObject.toJSONString(weCustomer)));
        } else if (WeConstans.ID_TYPE_MACHINE.equals(fromType)) {
            //拉去机器人信息暂不处理
        }
    }

    @Override
    public PageInfo<WeCustomer> listByCustomer() {

        LambdaQueryWrapper<WeChatContactMapping> lambdaQueryWrapper = new LambdaQueryWrapper<WeChatContactMapping>()
                .eq(WeChatContactMapping::getIsCustom, WeConstans.ID_TYPE_EX);
        List<WeChatContactMapping> weChatContactMappingList = this.baseMapper.selectList(lambdaQueryWrapper);
        List<WeCustomer> resultList = Optional.ofNullable(weChatContactMappingList).orElseGet(ArrayList::new)
                .stream().map(item -> weCustomerMapper.selectWeCustomerById(item.getReceiveId())).collect(Collectors.toList());
        PageInfo<WeCustomer> weCustomerPageInfo = new PageInfo<>(resultList);
        weCustomerPageInfo.setTotal(new PageInfo<>(weChatContactMappingList).getTotal());
        return weCustomerPageInfo;
    }


    /**
     * 发送人转接收人
     *
     * @param fromType
     * @param fromWeChatContactMapping
     * @return
     */
    private WeChatContactMapping from2ReveiceData(int fromType, WeChatContactMapping fromWeChatContactMapping) {
        //接收人映射数据
        WeChatContactMapping reveiceWeChatContactMapping = new WeChatContactMapping();
        if (StringUtils.isNotEmpty(fromWeChatContactMapping.getRoomId())) {
            reveiceWeChatContactMapping.setFromId(fromWeChatContactMapping.getRoomId());
        } else {
            reveiceWeChatContactMapping.setFromId(fromWeChatContactMapping.getReceiveId());
        }
        reveiceWeChatContactMapping.setReceiveId(fromWeChatContactMapping.getFromId());
        reveiceWeChatContactMapping.setIsCustom(fromType);
        return reveiceWeChatContactMapping;
    }
}
