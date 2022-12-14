package com.linkwechat.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeKfNoticeLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 客服员工通知日志表(WeKfNoticeLog)
 *
 * @author danmo
 * @since 2022-12-05 11:06:25
 */
@InterceptorIgnore(tenantLine = "true")
@Repository()
@Mapper
public interface WeKfNoticeLogMapper extends BaseMapper<WeKfNoticeLog> {


}

