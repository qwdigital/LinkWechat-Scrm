package com.linkwechat.service.impl;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.leads.leads.entity.WeLeadsManualAddRecord;
import com.linkwechat.domain.leads.leads.query.WeLeadsAddRequest;
import com.linkwechat.mapper.WeLeadsManualAddRecordMapper;
import com.linkwechat.service.IWeLeadsManualAddRecordService;
import com.linkwechat.service.IWeLeadsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 线索手动入线记录 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-07-25
 */
@Service
public class WeLeadsManualAddRecordServiceImpl extends ServiceImpl<WeLeadsManualAddRecordMapper, WeLeadsManualAddRecord> implements IWeLeadsManualAddRecordService {
    @Resource
    private IWeLeadsService weLeadsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long manualAdd(WeLeadsAddRequest request) {
        Long leadsId = weLeadsService.manualAdd(request);
        WeLeadsManualAddRecord record = new WeLeadsManualAddRecord();
        record.setId(IdUtil.getSnowflakeNextId());
        record.setLeadsId(leadsId);
        record.setWeUserId(SecurityUtils.getLoginUser().getSysUser().getWeUserId());
        this.save(record);
        return record.getLeadsId();
    }
}
