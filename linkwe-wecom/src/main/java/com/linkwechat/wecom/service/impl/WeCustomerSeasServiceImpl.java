package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeCustomerSeas;
import com.linkwechat.wecom.domain.vo.CustomerSeasCountVo;
import com.linkwechat.wecom.domain.vo.CustomerSeasRecordVo;
import com.linkwechat.wecom.mapper.WeCustomerSeasMapper;
import com.linkwechat.wecom.service.IWeCustomerSeasService;
import org.springframework.stereotype.Service;
import java.util.*;



@Service
public class WeCustomerSeasServiceImpl extends ServiceImpl<WeCustomerSeasMapper, WeCustomerSeas> implements IWeCustomerSeasService {



    @Override
    public CustomerSeasCountVo countCustomerSeas() {
        return this.baseMapper.countCustomerSeas();
    }

    @Override
    public List<CustomerSeasRecordVo> findSeasRecord(Integer groupByType) {
        return this.baseMapper.findSeasRecord(groupByType);
    }

}
