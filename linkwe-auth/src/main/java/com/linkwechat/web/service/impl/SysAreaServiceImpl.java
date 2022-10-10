package com.linkwechat.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.web.domain.SysArea;
import com.linkwechat.web.mapper.SysAreaMapper;
import com.linkwechat.web.service.ISysAreaService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行政区划(SysArea)
 *
 * @author danmo
 * @since 2022-06-27 11:01:07
 */
@Service
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {
    @Override
    public List<SysArea> getList(SysArea sysArea) {
        LambdaQueryWrapper<SysArea> wrapper = new LambdaQueryWrapper<>();
        if(sysArea.getId() != null){
            wrapper.eq(SysArea::getId,sysArea.getId());
        }
        if(sysArea.getParentId() != null){
            wrapper.eq(SysArea::getParentId,sysArea.getParentId());
        }
        if(sysArea.getName() != null){
            wrapper.like(SysArea::getName,sysArea.getName());
        }
        if(sysArea.getEPrefix() != null){
            wrapper.eq(SysArea::getEPrefix,sysArea.getEPrefix());
        }
        wrapper.eq(SysArea::getDelFlag,0);
        return list(wrapper);
    }
}
