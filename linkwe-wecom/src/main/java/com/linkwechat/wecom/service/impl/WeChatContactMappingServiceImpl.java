package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.elastic.ElasticSearchEntity;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeChatContactMapping;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.mapper.WeChatContactMappingMapper;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.service.IWeChatContactMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return weChatContactMappingMapper.insertWeChatContactMapping(weChatContactMapping);
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

    @Override
    public void saveWeChatContactMapping(List<ElasticSearchEntity> query) {
        /*List<String> toListIds = query.stream().map(chatData -> JSONObject.parseObject(JSONObject.toJSONString(chatData.getData())))
                .filter(chatData -> CollectionUtil.isNotEmpty(chatData.getJSONArray("tolist")))
                .map(chatData -> chatData.getJSONArray("tolist"))
                .map(item -> JSONArray.parseArray(item.toJSONString(), String.class))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));*/
        query.stream().forEach(elasticSearchEntity -> {
            //发送人映射数据
            WeChatContactMapping fromWeChatContactMapping = new WeChatContactMapping();
            //接收人映射数据
            WeChatContactMapping reveiceWeChatContactMapping = new WeChatContactMapping();
            JSONObject chatData = JSONObject.parseObject(JSONObject.toJSONString(elasticSearchEntity.getData()));
            int fromType = StringUtils.weCustomTypeJudgment(chatData.getString("from"));
            fromWeChatContactMapping.setFromId(chatData.getString("from"));
            getFromUserOrCustomInfo(fromWeChatContactMapping, chatData.getString("from"), fromType);
            getReveiceUserOrCustomInfo(reveiceWeChatContactMapping, chatData.getString("from"), fromType);
            JSONArray tolist = chatData.getJSONArray("tolist");
            if (CollectionUtil.isNotEmpty(tolist)) {
                //如果是单聊，tolist唯一，只有一个接收人，并判断接收人的类型
                if (StringUtils.isEmpty(chatData.getString("roomid"))) {
                    String idStr = (String) tolist.get(0);
                    int reveiceType = StringUtils.weCustomTypeJudgment(idStr);
                    getFromUserOrCustomInfo(reveiceWeChatContactMapping, idStr, reveiceType);
                    getReveiceUserOrCustomInfo(fromWeChatContactMapping, idStr, fromType);
                }else {
                    fromWeChatContactMapping.setRoomId(chatData.getString("roomid"));
                }
            }

        });
    }

    /**
     * 获取发送人成员或者客户详情
     *
     * @param id
     * @return
     */
    private WeChatContactMapping getFromUserOrCustomInfo(WeChatContactMapping weChatContactMapping, String id, int type) {
        if (WeConstans.ID_TYPE_USER.equals(type)) {
            WeUser weUser = weUserMapper.selectOne(new LambdaQueryWrapper<WeUser>().eq(WeUser::getUserId, id));
            weChatContactMapping.setFromAvatar(weUser.getAvatarMediaid());
            weChatContactMapping.setFromName(weUser.getName());
            weChatContactMapping.setFromGender(weUser.getGender());
        } else if (WeConstans.ID_TYPE_EX.equals(type)) {
            WeCustomer weCustomer = weCustomerMapper.selectWeCustomerById(id);
            weChatContactMapping.setFromAvatar(weCustomer.getAvatar());
            weChatContactMapping.setFromName(weCustomer.getName());
            weChatContactMapping.setFromGender(weCustomer.getGender());
        } else if (WeConstans.ID_TYPE_MACHINE.equals(type)) {

        }
        return weChatContactMapping;
    }

    /**
     * 获取接收人成员或者客户详情
     *
     * @param id
     * @return
     */
    private WeChatContactMapping getReveiceUserOrCustomInfo(WeChatContactMapping weChatContactMapping, String id, int type) {
        if (WeConstans.ID_TYPE_USER.equals(type)) {
            WeUser weUser = weUserMapper.selectOne(new LambdaQueryWrapper<WeUser>().eq(WeUser::getUserId, id));
            weChatContactMapping.setReviceAvatar(weUser.getAvatarMediaid());
            weChatContactMapping.setReviceName(weUser.getName());
            weChatContactMapping.setReviceGender(weUser.getGender());
        } else if (WeConstans.ID_TYPE_EX.equals(type)) {
            WeCustomer weCustomer = weCustomerMapper.selectWeCustomerById(id);
            weChatContactMapping.setReviceAvatar(weCustomer.getAvatar());
            weChatContactMapping.setReviceName(weCustomer.getName());
            weChatContactMapping.setReviceGender(weCustomer.getGender());
        } else if (WeConstans.ID_TYPE_MACHINE.equals(type)) {

        }
        return weChatContactMapping;
    }

}
