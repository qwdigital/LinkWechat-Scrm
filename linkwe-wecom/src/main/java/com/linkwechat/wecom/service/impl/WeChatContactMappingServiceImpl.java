package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.elastic.ElasticSearchEntity;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.client.WeUserClient;
import com.linkwechat.wecom.domain.WeChatContactMapping;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.domain.dto.WeUserDto;
import com.linkwechat.wecom.domain.dto.customer.ExternalUserDetail;
import com.linkwechat.wecom.mapper.WeChatContactMappingMapper;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.service.IWeChatContactMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 聊天关系映射Service业务层处理
 *
 * @author ruoyi
 * @date 2020-12-27
 */
@Slf4j
@Service
public class WeChatContactMappingServiceImpl implements IWeChatContactMappingService {
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
        return weChatContactMappingMapper.selectWeChatContactMappingList(weChatContactMapping);
    }

    /**
     * 新增聊天关系映射
     *
     * @param weChatContactMapping 聊天关系映射
     * @return 结果
     */
    @Override
    public int insertWeChatContactMapping(WeChatContactMapping weChatContactMapping) {
        List<WeChatContactMapping> list = selectWeChatContactMappingList(weChatContactMapping);
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
    public void saveWeChatContactMapping(List<ElasticSearchEntity> query) {
        query.stream().forEach(elasticSearchEntity -> {
            //发送人映射数据
            WeChatContactMapping fromWeChatContactMapping = new WeChatContactMapping();
            JSONObject chatData = JSONObject.parseObject(JSONObject.toJSONString(elasticSearchEntity.getData()));
            String fromId = chatData.getString("from");
            int fromType = StringUtils.weCustomTypeJudgment(fromId);

            JSONObject fromData = getUserOrCustomInfo(fromId, fromType);

            fromWeChatContactMapping.setFromId(fromId);
            fromWeChatContactMapping.setFromAvatar(fromData.getString("avatar"));
            fromWeChatContactMapping.setFromName(fromData.getString("name"));
            fromWeChatContactMapping.setFromGender(fromData.getInteger("gender"));

            JSONArray tolist = chatData.getJSONArray("tolist");
            if (CollectionUtil.isNotEmpty(tolist)) {
                //如果是单聊，tolist唯一，只有一个接收人，并判断接收人的类型
                if (StringUtils.isEmpty(chatData.getString("roomid"))) {
                    String idStr = String.valueOf(tolist.get(0));
                    int reveiceType = StringUtils.weCustomTypeJudgment(idStr);
                    JSONObject reveiceData = getUserOrCustomInfo(idStr, reveiceType);
                    fromWeChatContactMapping.setReviceId(idStr);
                    fromWeChatContactMapping.setReviceAvatar(reveiceData.getString("avatar"));
                    fromWeChatContactMapping.setReviceName(reveiceData.getString("name"));
                    fromWeChatContactMapping.setReviceGender(reveiceData.getInteger("gender"));
                    fromWeChatContactMapping.setIsCustom(reveiceType);
                } else {
                    fromWeChatContactMapping.setRoomId(chatData.getString("roomid"));
                }
            }
            //接收人映射数据
            WeChatContactMapping reveiceWeChatContactMapping = from2ReveiceData(fromType, fromWeChatContactMapping);
            insertWeChatContactMapping(fromWeChatContactMapping);
            insertWeChatContactMapping(reveiceWeChatContactMapping);
        });
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
            reveiceWeChatContactMapping.setFromId(fromWeChatContactMapping.getReviceId());
            reveiceWeChatContactMapping.setFromAvatar(fromWeChatContactMapping.getReviceAvatar());
            reveiceWeChatContactMapping.setFromName(fromWeChatContactMapping.getReviceName());
            reveiceWeChatContactMapping.setFromGender(fromWeChatContactMapping.getReviceGender());
        }
        reveiceWeChatContactMapping.setReviceId(fromWeChatContactMapping.getFromId());
        reveiceWeChatContactMapping.setReviceAvatar(fromWeChatContactMapping.getFromAvatar());
        reveiceWeChatContactMapping.setReviceName(fromWeChatContactMapping.getFromName());
        reveiceWeChatContactMapping.setReviceGender(fromWeChatContactMapping.getFromGender());
        reveiceWeChatContactMapping.setIsCustom(fromType);
        return reveiceWeChatContactMapping;
    }

    /**
     * 获取成员或者客户详情
     *
     * @param id   用户id
     * @param type
     * @return
     */
    private JSONObject getUserOrCustomInfo(String id, int type) {
        JSONObject tempData = new JSONObject();
        if (WeConstans.ID_TYPE_USER.equals(type)) {
            WeUser weUser = weUserMapper.selectOne(new LambdaQueryWrapper<WeUser>().eq(WeUser::getUserId, id));
            if (weUser == null) {
                WeUserDto userDto = weUserClient.getUserByUserId(id);
                tempData.put("avatar", userDto.getAvatar());
                tempData.put("name", userDto.getName());
                tempData.put("gender", userDto.getGender());
            } else {
                tempData.put("avatar", weUser.getAvatarMediaid());
                tempData.put("name", weUser.getName());
                tempData.put("gender", weUser.getGender());
            }
        } else if (WeConstans.ID_TYPE_EX.equals(type)) {
            WeCustomer weCustomer = weCustomerMapper.selectWeCustomerById(id);
            if (weCustomer == null) {
                ExternalUserDetail externalUserDetail = weCustomerClient.get(id);
                tempData.put("avatar", externalUserDetail.getExternal_contact().getAvatar());
                tempData.put("name", externalUserDetail.getExternal_contact().getName());
                tempData.put("gender", externalUserDetail.getExternal_contact().getGender());
            } else {
                tempData.put("avatar", weCustomer.getAvatar());
                tempData.put("name", weCustomer.getName());
                tempData.put("gender", weCustomer.getGender());
            }
        } else if (WeConstans.ID_TYPE_MACHINE.equals(type)) {

        }
        return tempData;
    }
}
