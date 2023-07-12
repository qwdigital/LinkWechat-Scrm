package com.linkwechat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerNumVO;
import com.linkwechat.mapper.WeLeadsFollowerMapper;
import com.linkwechat.service.IWeLeadsFollowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 线索跟进人 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:44
 */
@Slf4j
@Service
public class WeLeadsFollowerServiceImpl extends ServiceImpl<WeLeadsFollowerMapper, WeLeadsFollower> implements IWeLeadsFollowerService {

    @Resource
    private WeLeadsFollowerMapper weLeadsFollowerMapper;

    @Override
    public WeLeadsFollowerNumVO getLeadsFollowerNum(Long seaId) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        List<WeLeadsFollower> list = weLeadsFollowerMapper.getLeadsFollower(sysUser.getUserId());
        String dateStr = DateUtil.date().toDateStr();
        long count = list.stream().filter(i -> i.getSeaId().equals(seaId)).filter(i -> DateUtil.date(i.getFollowerStartTime()).toDateStr().equals(dateStr)).count();
        return WeLeadsFollowerNumVO.builder().maxClaim(count).stockMaxClaim(Long.valueOf(list.size())).build();
    }
}