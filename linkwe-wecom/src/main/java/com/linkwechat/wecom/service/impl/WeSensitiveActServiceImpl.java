package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.wecom.domain.WeSensitiveAct;
import com.linkwechat.wecom.mapper.WeSensitiveActMapper;
import com.linkwechat.wecom.service.IWeSensitiveActService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/12 17:41
 */
@Service
public class WeSensitiveActServiceImpl extends ServiceImpl<WeSensitiveActMapper, WeSensitiveAct> implements IWeSensitiveActService {

    @Override
    public WeSensitiveAct selectWeSensitiveActById(Long id) {
        return getById(id);
    }

    @Override
    public List<WeSensitiveAct> selectWeSensitiveActList(WeSensitiveAct weSensitiveAct) {
        LambdaQueryWrapper<WeSensitiveAct> lambda = Wrappers.lambdaQuery();
        if (weSensitiveAct != null && StringUtils.isNotBlank(weSensitiveAct.getActName())) {
            lambda.eq(WeSensitiveAct::getActName, weSensitiveAct);
        }
        lambda.orderByAsc(WeSensitiveAct::getOrderNum)
                .orderByDesc(WeSensitiveAct::getUpdateTime)
                .orderByDesc(WeSensitiveAct::getCreateTime);
        return list(lambda);
    }

    @Override
    public boolean insertWeSensitiveAct(WeSensitiveAct weSensitiveAct) {
        weSensitiveAct.setCreateBy(SecurityUtils.getUsername());
        weSensitiveAct.setCreateTime(DateUtils.getNowDate());
        return saveOrUpdate(weSensitiveAct);
    }

    @Override
    public boolean updateWeSensitiveAct(WeSensitiveAct weSensitiveAct) {
        weSensitiveAct.setUpdateBy(SecurityUtils.getUsername());
        weSensitiveAct.setUpdateTime(DateUtils.getNowDate());
        return saveOrUpdate(weSensitiveAct);
    }

    @Override
    public boolean deleteWeSensitiveActByIds(Long[] ids) {
        return removeByIds(Lists.newArrayList(ids));
    }
}
