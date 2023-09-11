package com.linkwechat.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeKfCustomer;
import com.linkwechat.domain.wecom.query.kf.WeKfCustomerQuery;
import com.linkwechat.domain.wecom.vo.kf.WeCustomerInfoVo;
import com.linkwechat.fegin.QwKfClient;
import com.linkwechat.mapper.WeKfCustomerMapper;
import com.linkwechat.service.IWeKfCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 客服客户表(WeKfCustomer)
 *
 * @author danmo
 * @since 2022-04-15 15:53:34
 */
@Service
public class WeKfCustomerServiceImpl extends ServiceImpl<WeKfCustomerMapper, WeKfCustomer> implements IWeKfCustomerService {

    @Autowired
    private QwKfClient qwKfClient;

    @Override
    public void saveCustomerInfo(String corpId, String externalUserId) {
        WeKfCustomer weKfCustomer = getCustomerInfo(corpId,externalUserId);
        if (weKfCustomer == null) {
            WeKfCustomerQuery query = new WeKfCustomerQuery();
            query.setExternal_userid_list(ListUtil.toList(externalUserId));
            query.setCorpid(corpId);
            WeCustomerInfoVo weCustomerInfo = qwKfClient.getCustomerInfos(query).getData();
            List<WeKfCustomer> weKfCustomers = Optional.ofNullable(weCustomerInfo)
                    .map(WeCustomerInfoVo::getCustomerList)
                    .orElseGet(ArrayList::new).stream().map(customerInfo -> {
                        WeKfCustomer customer = new WeKfCustomer();
                        customer.setCorpId(corpId);
                        customer.setExternalUserid(externalUserId);
                        customer.setNickName(customerInfo.getNickName());
                        customer.setAvatar(customerInfo.getAvatar());
                        customer.setUnionId(customerInfo.getUnionId());
                        customer.setGender(customerInfo.getGender());
                        return customer;
                    }).collect(Collectors.toList());
            saveBatch(weKfCustomers);
        }
    }

    @Override
    public WeKfCustomer getCustomerInfo(String corpId, String externalUserId) {
        return getOne(new LambdaQueryWrapper<WeKfCustomer>()
                .eq(WeKfCustomer::getCorpId,corpId)
                .eq(WeKfCustomer::getExternalUserid, externalUserId).last("limit 1"));
    }
}
