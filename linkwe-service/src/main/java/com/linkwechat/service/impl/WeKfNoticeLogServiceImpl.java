package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeKfNoticeLog;
import com.linkwechat.mapper.WeKfNoticeLogMapper;
import com.linkwechat.service.IWeKfNoticeLogService;
import org.springframework.stereotype.Service;

/**
 * 客服员工通知日志表(WeKfNoticeLog)
 *
 * @author danmo
 * @since 2022-12-05 11:06:26
 */
@Service
public class WeKfNoticeLogServiceImpl extends ServiceImpl<WeKfNoticeLogMapper, WeKfNoticeLog> implements IWeKfNoticeLogService {


    @Override
    public void updateStatusById(Integer status, Long id) {
        update(new LambdaUpdateWrapper<WeKfNoticeLog>().set(WeKfNoticeLog::getSendStatus, status).eq(WeKfNoticeLog::getId, id));
    }
}
