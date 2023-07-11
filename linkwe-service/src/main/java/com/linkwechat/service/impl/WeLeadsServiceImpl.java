package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.mapper.WeLeadsMapper;
import com.linkwechat.service.IWeLeadsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 线索 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-04-04
 */
@Slf4j
@Service
public class WeLeadsServiceImpl extends ServiceImpl<WeLeadsMapper, WeLeads> implements IWeLeadsService {

}