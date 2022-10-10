package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.domain.WeSensitiveAct;
import com.linkwechat.mapper.WeSensitiveActMapper;
import com.linkwechat.service.IWeSensitiveActService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 敏感行为表(WeSensitiveAct)
 *
 * @author danmo
 * @since 2022-06-10 10:38:46
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
        return save(weSensitiveAct);
    }

    @Override
    public boolean updateWeSensitiveAct(WeSensitiveAct weSensitiveAct) {
        return saveOrUpdate(weSensitiveAct);
    }

    @Override
    public boolean deleteWeSensitiveActByIds(Long[] ids) {
        return removeByIds(Lists.newArrayList(ids));
    }
}
