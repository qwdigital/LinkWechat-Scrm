package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.TreeUtil;
import com.linkwechat.domain.material.entity.WeCategory;
import com.linkwechat.domain.material.vo.WeCategoryVo;
import com.linkwechat.mapper.WeCategoryMapper;
import com.linkwechat.service.IWeCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leejoker
 * @date 2022/4/1 21:14
 */
@Service
public class WeCategoryServiceImpl extends ServiceImpl<WeCategoryMapper, WeCategory> implements IWeCategoryService {

    @Override
    public void insertWeCategory(WeCategory category) {
        //判断是否存在相同的名称
        WeCategory weCategory = this.getOne(
                new LambdaQueryWrapper<WeCategory>().eq(WeCategory::getMediaType, category.getMediaType())
                        .eq(WeCategory::getName, category.getName()).eq(WeCategory::getDelFlag, Constants.NORMAL_CODE)
                        .eq(WeCategory::getParentId,category.getParentId()));
        if (null != weCategory) {
            throw new WeComException("名称已存在！");
        }
        category.setId(SnowFlakeUtil.nextId());
        this.save(category);
    }

    @Override
    public void updateWeCategory(WeCategory category) {
        //判断是否存在相同的名称
        WeCategory weCategory = this.getOne(
                new LambdaQueryWrapper<WeCategory>().eq(WeCategory::getMediaType, category.getMediaType())
                        .eq(WeCategory::getName, category.getName()));

        if (null != weCategory) {
            throw new WeComException("名称已存在！");
        }
        this.updateById(category);
    }


    @Override
    public List<? extends Tree<?>> findWeCategoryByMediaType(String mediaType) {
        List<WeCategory> weCategories = baseMapper.categoryList(mediaType);
        List<WeCategoryVo> weCategoryVos = new ArrayList<>();

        WeCategoryVo weCategoryVo = new WeCategoryVo();
        weCategoryVo.setId(1L);
        weCategoryVo.setName("默认分组");
        weCategoryVo.setFlag(1);
        weCategoryVo.setParentId(0L);
        weCategoryVos.add(weCategoryVo);

        weCategories.forEach(c -> {
            WeCategoryVo weCategory = new WeCategoryVo();
            weCategory.setId(c.getId());
            weCategory.setName(c.getName());
            weCategory.setFlag(c.getFlag());
            weCategory.setParentId(c.getParentId());
            weCategoryVos.add(weCategory);
        });

        return TreeUtil.build(weCategoryVos);
    }

    @Override
    public void deleteWeCategoryById(Long[] ids) {
        new LambdaUpdateChainWrapper<>(this.baseMapper).set(WeCategory::getDelFlag, 2)
                .in(WeCategory::getId, Lists.newArrayList(ids)).update();
    }
}
