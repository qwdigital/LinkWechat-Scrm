package com.linkwechat.wecom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeSensitiveAct;
import com.linkwechat.wecom.domain.WeSensitiveActHit;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.mapper.WeCustomerMapper;
import com.linkwechat.wecom.mapper.WeSensitiveActHitMapper;
import com.linkwechat.wecom.mapper.WeUserMapper;
import com.linkwechat.wecom.service.IWeSensitiveActHitService;
import com.linkwechat.wecom.service.IWeSensitiveActService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/13 9:05
 */
@Service
public class WeSensitiveActHitServiceImpl extends ServiceImpl<WeSensitiveActHitMapper, WeSensitiveActHit> implements IWeSensitiveActHitService {
    @Autowired
    private IWeSensitiveActService weSensitiveActService;
    @Autowired
    private WeUserMapper weUserMapper;
    @Autowired
    private WeCustomerMapper weCustomerMapper;

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
    public void hitWeSensitiveAct(JSONObject chatJson) {
        if (chatJson != null) {
            String roomId = chatJson.getString("roomid");
            if (StringUtils.isEmpty(roomId)) {
                WeSensitiveActHit weSensitiveActHit = new WeSensitiveActHit();
                WeSensitiveAct weSensitiveAct = getSensitiveActType(chatJson.getString("msgtype"));
                if (weSensitiveAct != null && weSensitiveAct.getEnableFlag() == 1) {
                    weSensitiveActHit.setSensitiveAct(weSensitiveAct.getActName());
                    weSensitiveActHit.setSensitiveActId(weSensitiveAct.getId());
                    String operatorId = chatJson.getString("from");
                    String operatorTargetId = chatJson.getString("tolist");
                    weSensitiveActHit.setOperatorId(operatorId);
                    weSensitiveActHit.setOperateTargetId(operatorTargetId);
                    setUserOrCustomerInfo(weSensitiveActHit);
                    saveOrUpdate(weSensitiveActHit);
                }
            }
        }
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

    public WeSensitiveAct getSensitiveActType(String msgType) {
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

    private void setUserOrCustomerInfo(WeSensitiveActHit weSensitiveActHit) {
        int operatorType = StringUtils.weCustomTypeJudgment(weSensitiveActHit.getOperatorId());
        int operatorTargetType = StringUtils.weCustomTypeJudgment(weSensitiveActHit.getOperateTargetId());
        weSensitiveActHit.setOperator(getUserOrCustomerName(operatorType, weSensitiveActHit.getOperatorId()));
        weSensitiveActHit.setOperateTarget(getUserOrCustomerName(operatorTargetType, weSensitiveActHit.getOperateTargetId()));
    }

    private String getUserOrCustomerName(int type, String userId) {
        //获取发送人信息
        if (WeConstans.ID_TYPE_USER.equals(type)) {
            //成员信息
            WeUser weUser = weUserMapper.selectOne(new LambdaQueryWrapper<WeUser>().eq(WeUser::getUserId, userId));
            if (weUser != null) {
                return weUser.getName();
            }
        } else if (WeConstans.ID_TYPE_EX.equals(type)) {
            //获取外部联系人信息
//            WeCustomer weCustomer = weCustomerMapper.selectWeCustomerById(userId);
            List<WeCustomer> weCustomers = weCustomerMapper.selectList(new LambdaQueryWrapper<WeCustomer>()
                    .eq(WeCustomer::getExternalUserid, userId));
            if(CollectionUtils.isNotEmpty(weCustomers)){
                return weCustomers.stream().findFirst().get().getCustomerName();
            }
//            if (weCustomer != null) {
//                return weCustomer.getName();
//            }
        } else if (WeConstans.ID_TYPE_MACHINE.equals(type)) {
            //拉去机器人信息暂不处理
        }
        return null;
    }
}
