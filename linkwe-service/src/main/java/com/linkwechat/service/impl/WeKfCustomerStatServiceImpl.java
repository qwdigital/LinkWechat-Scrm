package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeKfCustomerStat;
import com.linkwechat.mapper.WeKfCustomerStatMapper;
import com.linkwechat.service.IWeKfCustomerStatService;
import org.springframework.stereotype.Service;

/**
 * 客服客户统计表(WeKfCustomerStat)
 *
 * @author danmo
 * @since 2022-11-28 16:48:24
 */
@Service
public class WeKfCustomerStatServiceImpl extends ServiceImpl<WeKfCustomerStatMapper, WeKfCustomerStat> implements IWeKfCustomerStatService {

}
