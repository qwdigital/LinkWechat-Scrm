package com.linkwechat.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.task.entity.WeTasks;
import com.linkwechat.domain.task.vo.WeTasksVO;
import com.linkwechat.mapper.WeTasksMapper;
import com.linkwechat.service.IWeTasksService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 待办任务 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-07-20
 */
@Service
public class WeTasksServiceImpl extends ServiceImpl<WeTasksMapper, WeTasks> implements IWeTasksService {

    @Override
    public List<WeTasksVO> myList() {
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getUserId, SecurityUtils.getLoginUser().getUserId());
        queryWrapper.eq(WeTasks::getStatus, 0);
        queryWrapper.orderByDesc(WeTasks::getCreateTime);
        List<WeTasks> weTasks = this.baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(weTasks, WeTasksVO.class);
    }

    @Override
    public List<WeTasksVO> history() {
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getUserId, SecurityUtils.getLoginUser().getUserId());
        queryWrapper.ne(WeTasks::getStatus, 0);
        queryWrapper.orderByDesc(WeTasks::getCreateTime);
        List<WeTasks> weTasks = this.baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(weTasks, WeTasksVO.class);
    }

    @Override
    public void save() {
        WeTasks build = WeTasks.builder()
                .id(IdUtil.getSnowflakeNextId())
                .userId()
                .weUserId()
                .type()
                .title()
                .content()
                .sendTime(LocalDateTime.now())
                .url()
                .status(0)
                .delFlag(Constants.COMMON_STATE)
                .build();
        this.baseMapper.insert(build);
    }
}
