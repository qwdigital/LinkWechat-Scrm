package com.linkwechat.wecom.service.impl;

import cn.hutool.core.date.DateUtil;
import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.TreeUtil;
import com.linkwechat.wecom.domain.WeCategory;
import com.linkwechat.wecom.domain.vo.WeCategoryVo;
import com.linkwechat.wecom.mapper.WeCategoryMapper;
import com.linkwechat.wecom.service.IWeCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WeCategoryServiceImpl implements IWeCategoryService {

    @Autowired
    private WeCategoryMapper weCategoryMapper;

    @Override
    public int insertWeCategory(WeCategory category) {
        //判断是否存在相同的名称
        WeCategory weCategory = weCategoryMapper.findWeCategoryByMediaTypeAndName(category.getMediaType(), category.getName());
        if (null != weCategory) {
            throw new WeComException("名称已存在！");
        }
        category.setCreateTime(DateUtil.date());
        category.setId(SnowFlakeUtil.nextId());
        return weCategoryMapper.insertWeCategory(category);
    }

    @Override
    public int updateWeCategory(WeCategory category) {
        //判断是否存在相同的名称
        WeCategory weCategory = weCategoryMapper.findWeCategoryByMediaTypeAndName(category.getMediaType(), category.getName());
        if (null != weCategory) {
            throw new WeComException("名称已存在！");
        }
        category.setUpdateTime(DateUtil.date());
        return weCategoryMapper.updateWeCategory(category);
    }

    @Override
    public int deleteWeCategoryById(Long id) {
        return weCategoryMapper.deleteWeCategoryById(id);
    }

    @Override
    public int deleteWeCategoryByIds(Long[] ids) {
        return weCategoryMapper.deleteWeCategoryByIds(ids);
    }

    @Override
    public List<? extends Tree<?>> findWeCategoryByMediaType(String mediaType) {
        List<WeCategory> weCategories = weCategoryMapper.findWeCategoryByMediaType(mediaType);
        List<WeCategoryVo> weCategoryVos = new ArrayList<>();
        weCategories.forEach(c -> {
            WeCategoryVo weCategoryVo = new WeCategoryVo();
            weCategoryVo.setId(c.getId());
            weCategoryVo.setName(c.getName());
            weCategoryVo.setParentId(c.getParentId());
            weCategoryVos.add(weCategoryVo);
        });
        return TreeUtil.build(weCategoryVos);
    }

    @Override
    public WeCategory findWeCategoryById(Long id) {
        return weCategoryMapper.findWeCategoryById(id);
    }


}
