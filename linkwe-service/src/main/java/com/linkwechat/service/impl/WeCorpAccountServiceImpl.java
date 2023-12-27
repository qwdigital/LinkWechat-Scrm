package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.fegin.QwCorpClient;
import com.linkwechat.mapper.WeCorpAccountMapper;
import com.linkwechat.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 企业id相关配置(WeCorpAccount)
 *
 * @author danmo
 * @since 2022-03-08 19:01:14
 */
@Service
public class WeCorpAccountServiceImpl extends ServiceImpl<WeCorpAccountMapper, WeCorpAccount> implements IWeCorpAccountService {

    @Autowired
    private RedisService redisService;

    @Resource
    private QwCorpClient qwCorpClient;

    @Override
    public List<WeCorpAccount> getAllCorpAccountInfo() {
        return list(new LambdaQueryWrapper<WeCorpAccount>().eq(WeCorpAccount::getDelFlag, 0));
    }

    @Override
    public WeCorpAccount getCorpAccountByCorpId(String corpId) {
        //下面注释代码开发过程中由于spring-boot-devtools有自己的类加载器，进行更新，由于类加载器的不同导致类型转换失败。
        //WeCorpAccount account = (WeCorpAccount)redisService.getCacheObject(StringUtils.format(Constants.CORP_ACCOUNT_KEY, corpId));

        //从缓存中取数据
        Object object = redisService.getCacheObject(Constants.CORP_ACCOUNT_KEY);
        WeCorpAccount account = JSONObject.parseObject(JSONObject.toJSONString(object), WeCorpAccount.class);
        if (ObjectUtil.isNull(account)) {
            //从数据库获取企微配置信息
            LambdaQueryWrapper<WeCorpAccount> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StringUtils.isNotEmpty(corpId), WeCorpAccount::getCorpId, corpId);
            queryWrapper.eq(WeCorpAccount::getDelFlag, Constants.COMMON_STATE).last("limit 1");
            account = getOne(queryWrapper);
            //添加缓存到redis
            redisService.setCacheObject(Constants.CORP_ACCOUNT_KEY, account, 1, TimeUnit.HOURS);
        }
        return account;
    }

    @Override
    public void startCustomerChurnNoticeSwitch(String status) {
        WeCorpAccount validWeCorpAccount = getCorpAccountByCorpId(SecurityUtils.getCorpId());
        validWeCorpAccount.setCustomerChurnNoticeSwitch(status);
        this.updateById(validWeCorpAccount);
    }

    @Override
    public String getCustomerChurnNoticeSwitch() {

        if(StringUtils.isNotEmpty(SecurityUtils.getCorpId())){
            List<WeCorpAccount> weCorpAccounts = this.list(new LambdaQueryWrapper<WeCorpAccount>()
                    .eq(WeCorpAccount::getCorpId, SecurityUtils.getCorpId()));
            if(CollectionUtil.isNotEmpty(weCorpAccounts)){
                WeCorpAccount validWeCorpAccount =weCorpAccounts.stream().findFirst().get();
                return validWeCorpAccount.getCustomerChurnNoticeSwitch();
            }

        }else{
            List<WeCorpAccount> weCorpAccounts = this.list();

            if(CollectionUtil.isNotEmpty(weCorpAccounts)){
                WeCorpAccount validWeCorpAccount =weCorpAccounts.stream().findFirst().get();
                return validWeCorpAccount.getCustomerChurnNoticeSwitch();
            }

        }


        return WeConstans.DEL_FOLLOW_USER_SWITCH_CLOSE;
    }

    @Override
    public void addOrUpdate(WeCorpAccount weCorpAccount) {

        if (saveOrUpdate(weCorpAccount)) {
            redisService.deleteObject(Constants.CORP_ACCOUNT_KEY);
        }
    }

}
