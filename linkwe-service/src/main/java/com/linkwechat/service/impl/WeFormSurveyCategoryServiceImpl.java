package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.domain.material.entity.WeCategory;
import com.linkwechat.mapper.WeCategoryMapper;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import com.linkwechat.service.IWeFormSurveyCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 智能表单分组
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/13 9:39
 */
@Service
public class WeFormSurveyCategoryServiceImpl extends ServiceImpl<WeCategoryMapper, WeCategory> implements IWeFormSurveyCategoryService {


    @Resource
    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;

    @Override
    public void insertWeCategory(WeCategory category) {
        //判断是否存在相同的名称
        WeCategory weCategory = getWeCategory(category);
        if (null != weCategory) {
            throw new WeComException("名称已存在！");
        }
        category.setId(SnowFlakeUtil.nextId());
        this.save(category);
    }


    @Override
    public void updateWeCategory(WeCategory category) {
        //判断是否存在相同的名称
        WeCategory weCategory = getWeCategory(category);
        if (null != weCategory) {
            throw new WeComException("名称已存在！");
        }
        this.updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWeCategoryById(Long[] ids) {
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                weFormSurveyCatalogueService.deleteFormSurveyGroup(id);
            }
        }
        new LambdaUpdateChainWrapper<>(this.baseMapper).set(WeCategory::getDelFlag, 2).in(WeCategory::getId, Lists.newArrayList(ids)).update();
    }

    /**
     * 获取分组
     *
     * @param category
     * @return {@link WeCategory}
     * @author WangYX
     * @date 2022/10/13 10:35
     */
    private WeCategory getWeCategory(WeCategory category) {
        QueryWrapper<WeCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WeCategory::getMediaType, category.getMediaType());
        queryWrapper.lambda().eq(WeCategory::getName, category.getName());
        queryWrapper.lambda().eq(WeCategory::getDelFlag, Constants.NORMAL_CODE);
        queryWrapper.lambda().eq(WeCategory::getParentId, 0L);
        return this.getOne(queryWrapper);
    }

}
