package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSeaBaseSettings;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaBaseSettingsRequest;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaBaseSettingsVO;
import com.linkwechat.mapper.WeLeadsSeaBaseSettingsMapper;
import com.linkwechat.service.IWeLeadsSeaBaseSettingsService;
import ma.glasnost.orika.MapperFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 公海基础配置 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 16:41
 */
@Service
public class WeLeadsSeaBaseSettingsServiceImpl extends ServiceImpl<WeLeadsSeaBaseSettingsMapper, WeLeadsSeaBaseSettings> implements IWeLeadsSeaBaseSettingsService {

    @Resource
    private MapperFactory mapperFactory;

    @Override
    public WeLeadsSeaBaseSettingsVO queryBaseSetting() {
        WeLeadsSeaBaseSettings weSeaBaseSettings = getOne(Wrappers.<WeLeadsSeaBaseSettings>lambdaQuery());
        return mapperFactory.getMapperFacade().map(weSeaBaseSettings, WeLeadsSeaBaseSettingsVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBaseSetting(WeLeadsSeaBaseSettingsRequest request) {
        if (request.getMaxClaim() > request.getStockMaxClaim()) {
            throw new ServiceException("每日领取上限不可大于客户存量上线");
        }
        //保证这张表只有一条记录
        WeLeadsSeaBaseSettings weSeaBaseSettings = getOne(Wrappers.<WeLeadsSeaBaseSettings>lambdaQuery());
        Long id = weSeaBaseSettings.getId();
        request.setId(id);
        WeLeadsSeaBaseSettings baseSettings = mapperFactory.getMapperFacade().map(request, WeLeadsSeaBaseSettings.class);
        return updateById(baseSettings);
    }
}

