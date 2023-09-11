package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfNoticeLog;

/**
 * 客服员工通知日志表(WeKfNoticeLog)
 *
 * @author danmo
 * @since 2022-12-05 11:06:26
 */
public interface IWeKfNoticeLogService extends IService<WeKfNoticeLog> {
    public void updateStatusById(Integer status, Long id);
} 
