package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.mapper.WeLeadsMapper;
import com.linkwechat.service.IWeLeadsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 线索 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:44
 */
@Slf4j
@Service
public class WeLeadsServiceImpl extends ServiceImpl<WeLeadsMapper, WeLeads> implements IWeLeadsService {

}