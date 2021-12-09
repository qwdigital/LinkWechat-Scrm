package com.linkwechat.wecom.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.TreeUtil;
import com.linkwechat.wecom.domain.WeCategory;
import com.linkwechat.wecom.domain.vo.WeCategoryVo;
import com.linkwechat.wecom.mapper.WeCategoryMapper;
import com.linkwechat.wecom.service.IWeCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WeCategoryServiceImpl extends ServiceImpl<WeCategoryMapper,WeCategory> implements IWeCategoryService {



    @Override
    public void insertWeCategory(WeCategory category) {
        //判断是否存在相同的名称
        WeCategory weCategory =this.getOne(new LambdaQueryWrapper<WeCategory>()
        .eq(WeCategory::getMediaType,category.getMediaType())
        .eq(WeCategory::getName,category.getName())
        .eq(WeCategory::getDelFlag, Constants.NORMAL_CODE));
        if (null != weCategory) {
            throw new WeComException("名称已存在！");
        }
//        category.setCreateTime(DateUtil.date());
        category.setId(SnowFlakeUtil.nextId());
         this.save(category);
    }

    @Override
    public void updateWeCategory(WeCategory category) {
        //判断是否存在相同的名称
        WeCategory weCategory =this.getOne(new LambdaQueryWrapper<WeCategory>()
                .eq(WeCategory::getMediaType,category.getMediaType())
                .eq(WeCategory::getName,category.getName())
                .eq(WeCategory::getDelFlag, Constants.NORMAL_CODE));

        if (null != weCategory) {
            throw new WeComException("名称已存在！");
        }
//        category.setUpdateTime(DateUtil.date());
        this.updateById(category);
    }





    @Override
    public List<? extends Tree<?>> findWeCategoryByMediaType(String mediaType) {
        List<WeCategory> weCategories = this.list(new LambdaQueryWrapper<WeCategory>()
        .eq(WeCategory::getMediaType,mediaType)
        .eq(WeCategory::getDelFlag,Constants.NORMAL_CODE).orderByDesc(BaseEntity::getCreateTime));
        List<WeCategoryVo> weCategoryVos = new ArrayList<>();
        weCategories.forEach(c -> {
            WeCategoryVo weCategoryVo = new WeCategoryVo();
            weCategoryVo.setId(c.getId());
            weCategoryVo.setName(c.getName());
            weCategoryVo.setFlag(c.getFlag());
            weCategoryVo.setParentId(c.getParentId());
            weCategoryVos.add(weCategoryVo);
        });
        return TreeUtil.build(weCategoryVos);
    }

    @Override
    public void deleteWeCategoryById(Long[] ids) {
         this.baseMapper.deleteWeCategoryById(ids);
    }


}
