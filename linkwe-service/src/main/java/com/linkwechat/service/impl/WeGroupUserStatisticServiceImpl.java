package com.linkwechat.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeGroupUserStatisticMapper;
import com.linkwechat.domain.WeGroupUserStatistic;
import com.linkwechat.service.IWeGroupUserStatisticService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 群聊群主数据统计数据(WeGroupUserStatistic)
 *
 * @author danmo
 * @since 2022-06-13 16:59:10
 */
@Service
public class WeGroupUserStatisticServiceImpl extends ServiceImpl<WeGroupUserStatisticMapper, WeGroupUserStatistic> implements IWeGroupUserStatisticService {

public WeGroupUserStatisticServiceImpl() {}

@Autowired
private WeGroupUserStatisticMapper weGroupUserStatisticMapper; 
}
