package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.query.qr.WeQrUserInfoQuery;
import com.linkwechat.wecom.domain.vo.qr.WeQrScopeVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.mapper.WeQrScopeMapper;
import com.linkwechat.wecom.domain.WeQrScope;
import com.linkwechat.wecom.service.IWeQrScopeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 活码使用范围表(WeQrScope)表服务实现类
 *
 * @author makejava
 * @since 2021-11-07 01:29:14
 */
//@DS("db1")
@Service
public class IWeQrScopeServiceImpl extends ServiceImpl<WeQrScopeMapper, WeQrScope> implements IWeQrScopeService {

    public IWeQrScopeServiceImpl() {
    }

    @Autowired
    private WeQrScopeMapper weQrScopeMapper;

    @Override
    public void saveBatchByQrId(Long qrId, List<WeQrUserInfoQuery> qrUserInfos) {
        if (CollectionUtil.isNotEmpty(qrUserInfos)) {
            List<WeQrScope> scopeList = new ArrayList<>();
            for (WeQrUserInfoQuery userInfo : qrUserInfos) {
                String uuId = RandomUtil.randomString(18);
                List<WeQrScope> weQrScopeUserList = Optional.ofNullable(userInfo.getUserIds()).orElseGet(ArrayList::new).stream().map(userId -> {
                    WeQrScope weQrScope = new WeQrScope();
                    weQrScope.setQrId(qrId);
                    weQrScope.setScopeId(uuId);
                    weQrScope.setUserId(userId);
                    weQrScope.setScopeType(1);
                    weQrScope.setType(userInfo.getType());
                    if(CollectionUtil.isNotEmpty(userInfo.getWorkCycle())){
                        weQrScope.setWorkCycle(userInfo.getWorkCycle().stream().map(String::valueOf).collect(Collectors.joining(",")));
                    }
                    weQrScope.setBeginTime(userInfo.getBeginTime());
                    weQrScope.setEndTime(userInfo.getEndTime());
                    return weQrScope;
                }).collect(Collectors.toList());
                scopeList.addAll(weQrScopeUserList);

                List<WeQrScope> weQrScopePartyList = Optional.ofNullable(userInfo.getPartys()).orElseGet(ArrayList::new).stream().map(party -> {
                    WeQrScope weQrScope = new WeQrScope();
                    weQrScope.setQrId(qrId);
                    weQrScope.setScopeId(uuId);
                    weQrScope.setParty(String.valueOf(party));
                    weQrScope.setScopeType(2);
                    if(CollectionUtil.isNotEmpty(userInfo.getWorkCycle())){
                        weQrScope.setWorkCycle(userInfo.getWorkCycle().stream().map(String::valueOf).collect(Collectors.joining(",")));
                    }
                    weQrScope.setType(userInfo.getType());
                    weQrScope.setBeginTime(userInfo.getBeginTime());
                    weQrScope.setEndTime(userInfo.getEndTime());
                    return weQrScope;
                }).collect(Collectors.toList());
                scopeList.addAll(weQrScopePartyList);
            }
            saveBatch(scopeList);
        }
    }

    @Override
    public Boolean delBatchByQrIds(List<Long> qrIds) {
        WeQrScope weQrScope = new WeQrScope();
        weQrScope.setDelFlag(1);
        return this.update(weQrScope,new LambdaQueryWrapper<WeQrScope>().in(WeQrScope::getQrId,qrIds));
    }

    @Override
    public void updateBatchByQrId(Long qrId, List<WeQrUserInfoQuery> qrUserInfos) {
        if(CollectionUtil.isNotEmpty(qrUserInfos)){
            delBatchByQrIds(ListUtil.toList(qrId));
            this.saveBatchByQrId(qrId,qrUserInfos);
        }
    }

    @Override
    public List<WeQrScopeVo> getWeQrScopeByQrIds(List<Long> qrIds) {
        return this.baseMapper.getWeQrScopeByQrIds(qrIds);
    }

    @Override
    public List<WeQrScopeVo> getWeQrScopeByTime(String formatTime, String qrId) {
       return this.baseMapper.getWeQrScopeByTime(formatTime,qrId);
    }
}
