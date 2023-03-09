package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeLxQrScope;
import com.linkwechat.domain.qr.WeQrScope;
import com.linkwechat.domain.qr.query.WeLxQrUserInfoQuery;
import com.linkwechat.mapper.WeLxQrScopeMapper;
import com.linkwechat.service.IWeLxQrScopeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 拉新活码使用范围表(WeLxQrScope)
 *
 * @author danmo
 * @since 2023-03-07 15:06:04
 */
@Service
public class WeLxQrScopeServiceImpl extends ServiceImpl<WeLxQrScopeMapper, WeLxQrScope> implements IWeLxQrScopeService {

    @Override
    public void saveBatchByQrId(Long qrId, List<WeLxQrUserInfoQuery> qrUserInfos) {
        if (CollectionUtil.isNotEmpty(qrUserInfos)) {
            List<WeLxQrScope> scopeList = new ArrayList<>();
            for (WeLxQrUserInfoQuery userInfo : qrUserInfos) {
                List<WeLxQrScope> weQrScopeUserList = Optional.ofNullable(userInfo.getUserIds()).orElseGet(ArrayList::new).stream().map(userId -> {
                    WeLxQrScope weQrScope = new WeLxQrScope();
                    weQrScope.setQrId(qrId);
                    weQrScope.setUserId(userId);
                    weQrScope.setScopeType(1);
                    return weQrScope;
                }).collect(Collectors.toList());
                scopeList.addAll(weQrScopeUserList);

                List<WeLxQrScope> weQrScopePartyList = Optional.ofNullable(userInfo.getPartys()).orElseGet(ArrayList::new).stream().map(party -> {
                    WeLxQrScope weQrScope = new WeLxQrScope();
                    weQrScope.setQrId(qrId);
                    weQrScope.setParty(String.valueOf(party));
                    weQrScope.setScopeType(2);
                    return weQrScope;
                }).collect(Collectors.toList());
                scopeList.addAll(weQrScopePartyList);
            }
            saveBatch(scopeList);
        }
    }

    @Override
    public void updateBatchByQrId(Long qrId, List<WeLxQrUserInfoQuery> qrUserInfos) {
        if(CollectionUtil.isNotEmpty(qrUserInfos)){
            delBatchByQrIds(ListUtil.toList(qrId));
            this.saveBatchByQrId(qrId,qrUserInfos);
        }
    }

    @Override
    public void delBatchByQrIds(List<Long> qrIds) {
        this.update(new LambdaUpdateWrapper<WeLxQrScope>().set(WeLxQrScope::getDelFlag,1).in(WeLxQrScope::getQrId,qrIds));
    }
}
