package com.linkwechat.wecom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.wecom.domain.WeSensitiveAct;
import com.linkwechat.wecom.domain.WeSensitiveActHit;
import com.linkwechat.wecom.mapper.WeSensitiveActHitMapper;
import com.linkwechat.wecom.service.IWeSensitiveActHitService;
import com.linkwechat.wecom.service.IWeSensitiveActService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/13 9:05
 */
@Service
public class WeSensitiveActHitServiceImpl extends ServiceImpl<WeSensitiveActHitMapper, WeSensitiveActHit> implements IWeSensitiveActHitService {
    @Autowired
    private IWeSensitiveActService weSensitiveActService;

    @Override
    public WeSensitiveActHit selectWeSensitiveActHitById(Long id) {
        return getById(id);
    }

    @Override
    public List<WeSensitiveActHit> selectWeSensitiveActHitList(WeSensitiveActHit weSensitiveActHit) {
        LambdaQueryWrapper<WeSensitiveActHit> lambda = Wrappers.lambdaQuery();
        lambda.orderByDesc(WeSensitiveActHit::getCreateTime);
        return list(lambda);
    }

    @Override
    public boolean insertWeSensitiveActHit(WeSensitiveActHit weSensitiveActHit) {
        weSensitiveActHit.setCreateBy(SecurityUtils.getUsername());
        weSensitiveActHit.setCreateTime(DateUtils.getNowDate());
        return saveOrUpdate(weSensitiveActHit);
    }

    @Override
    public void hitWeSensitiveAct(List<JSONObject> chatDataList) {
        //TODO 未完成
        List<WeSensitiveActHit> saveList = Lists.newArrayList();
        chatDataList.stream().filter(chatData -> {
            String type = chatData.getString("msgtype");
            if ("card".equals(type) || "redpacket".equals(type)) {
                return true;
            }
            return false;
        }).map(chatData -> {
            WeSensitiveActHit weSensitiveActHit = new WeSensitiveActHit();
            WeSensitiveAct weSensitiveAct = getSensitiveActType(chatData.getString("msgtype"));
            if (weSensitiveAct != null) {
                weSensitiveActHit.setSensitiveAct(weSensitiveAct.getActName());
                weSensitiveActHit.setSensitiveActId(weSensitiveAct.getId());
            }
            return weSensitiveActHit;
        }).collect(Collectors.toList());
    }

    private WeSensitiveAct getSensitiveAct(String type) {
        WeSensitiveAct weSensitiveAct = new WeSensitiveAct();
        weSensitiveAct.setActName(type);
        List<WeSensitiveAct> list = weSensitiveActService.selectWeSensitiveActList(weSensitiveAct);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    private WeSensitiveAct getSensitiveActType(String msgType) {
        String type;
        if ("card".equals(msgType)) {
            type = "发名片";
        } else if ("redpacket".equals(msgType)) {
            type = "发红包";
        } else {
            type = "拉黑/删除好友";
        }
        return getSensitiveAct(type);
    }
}
