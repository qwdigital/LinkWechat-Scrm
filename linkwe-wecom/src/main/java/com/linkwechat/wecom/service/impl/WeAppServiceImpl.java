package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeApp;
import com.linkwechat.wecom.mapper.WeAppMapper;
import com.linkwechat.wecom.service.IWeAppService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: HaoN
 * @create: 2021-01-26 18:44
 **/
@Service
public class WeAppServiceImpl extends ServiceImpl<WeAppMapper, WeApp> implements IWeAppService {
}
