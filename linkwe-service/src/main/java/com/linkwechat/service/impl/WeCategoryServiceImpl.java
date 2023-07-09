package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.common.enums.CategoryMediaType;
import com.linkwechat.common.enums.CategoryModuleTypeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.TreeUtil;
import com.linkwechat.domain.WeMsgTlp;
import com.linkwechat.domain.material.entity.WeCategory;
import com.linkwechat.domain.material.entity.WeContentTalk;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.vo.WeCategoryNewVo;
import com.linkwechat.domain.material.vo.WeCategoryVo;
import com.linkwechat.mapper.WeCategoryMapper;
import com.linkwechat.mapper.WeContentTalkMapper;
import com.linkwechat.mapper.WeMaterialMapper;
import com.linkwechat.mapper.WeMsgTlpMapper;
import com.linkwechat.service.IWeCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author leejoker
 * @date 2022/4/1 21:14
 */
@Service
public class WeCategoryServiceImpl extends ServiceImpl<WeCategoryMapper, WeCategory> implements IWeCategoryService {

    @Resource
    private WeMaterialMapper weMaterialMapper;

    @Resource
    private WeContentTalkMapper weContentTalkMapper;

    @Resource
    private WeMsgTlpMapper weMsgTlpMapper;

    @Override
    public void insertWeCategory(WeCategory category) {
        //判断是否存在相同的名称
        WeCategory weCategory = this.getOne(
                new LambdaQueryWrapper<WeCategory>().eq(WeCategory::getMediaType, category.getMediaType())
                        .eq(WeCategory::getName, category.getName()).eq(WeCategory::getDelFlag, Constants.NORMAL_CODE)
                        .eq(WeCategory::getParentId, category.getParentId()));
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
    public List<? extends Tree<?>> findWeCategoryByMediaType(String mediaType, Integer scene) {
        List<WeCategory> weCategories = baseMapper.categoryList(mediaType);
        List<WeCategoryVo> weCategoryVos = new ArrayList<>();

        WeCategory weCategoryVo = new WeCategory();
        weCategoryVo.setId(1L);
        weCategoryVo.setName("默认分组");
        weCategoryVo.setFlag(1);
        weCategoryVo.setParentId(0L);
        weCategories.add(0, weCategoryVo);

        Integer moduleType = null;
        //素材中心
        List<String> materialType = new ArrayList<>();
        materialType.add("0");
        materialType.add("1");
        materialType.add("2");
        materialType.add("3");
        materialType.add("4");
        materialType.add("5");
        materialType.add("6");
        materialType.add("7");
        materialType.add("8");
        materialType.add("9");
        materialType.add("10");
        materialType.add("11");
        materialType.add("12");
        //话术中心
        List<String> talkType = new ArrayList<>();
        talkType.add("13");
        talkType.add("14");
        //模板中心
        List<String> templateType = new ArrayList<>();
        templateType.add("17");

        if (materialType.contains(mediaType)) {
            moduleType = 1;
        }
        if (talkType.contains(mediaType)) {
            moduleType = 2;
        }
        if (templateType.contains(mediaType)) {
            moduleType = 3;
        }

        Map<Long, Integer> countMap = new HashMap<>();
        if (moduleType != null) {
            if (moduleType.equals(1)) {
                //统计素材中心数量
                LambdaQueryWrapper<WeMaterial> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(WeMaterial::getCategoryId);
                queryWrapper.eq(WeMaterial::getMediaType, mediaType);
                queryWrapper.eq(WeMaterial::getModuleType, moduleType);
                queryWrapper.eq(WeMaterial::getDelFlag, 0);

                if (scene.equals(1)) {
                    if (mediaType.equals(CategoryMediaType.IMAGE.getType().toString())) {
                        queryWrapper.le(WeMaterial::getPixelSize, 172800L);
                        queryWrapper.le(WeMaterial::getMemorySize, 10 * 1024 * 1024L);
                    } else {
                        queryWrapper.le(WeMaterial::getPixelSize, 1555200L);
                        queryWrapper.le(WeMaterial::getMemorySize, 10 * 1024 * 1024L);
                    }
                }
                List<WeMaterial> weMaterials = weMaterialMapper.selectList(queryWrapper);
                if (CollectionUtil.isNotEmpty(weMaterials)) {
                    weMaterials.removeIf(Objects::isNull);
                    Map<Long, List<WeMaterial>> collect = weMaterials.stream().collect(Collectors.groupingBy(WeMaterial::getCategoryId));
                    for (Map.Entry<Long, List<WeMaterial>> entry : collect.entrySet()) {
                        countMap.put(entry.getKey(), entry.getValue().size());
                    }
                }

            } else if (moduleType.equals(2)) {
                //0企业话术，1客服话术
                int type = mediaType.equals("13") ? 0 : 1;
                //统计话术中心数量
                LambdaQueryWrapper<WeContentTalk> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(WeContentTalk::getCategoryId);
                queryWrapper.eq(WeContentTalk::getTalkType, type);
                queryWrapper.eq(WeContentTalk::getDelFlag, 0);
                List<WeContentTalk> weContentTalks = weContentTalkMapper.selectList(queryWrapper);
                Map<Long, List<WeContentTalk>> collect = weContentTalks.stream().collect(Collectors.groupingBy(WeContentTalk::getCategoryId));
                for (Map.Entry<Long, List<WeContentTalk>> entry : collect.entrySet()) {
                    countMap.put(entry.getKey(), entry.getValue().size());
                }
            } else if (moduleType.equals(3)) {
                //统计模板中心数量
                LambdaQueryWrapper<WeMsgTlp> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(WeMsgTlp::getCategoryId);
                //1欢迎语模板2群发模板3sop模板
                queryWrapper.eq(WeMsgTlp::getTemplateType, 2);
                queryWrapper.eq(WeMsgTlp::getDelFlag, 0);
                List<WeMsgTlp> weMsgTlps = weMsgTlpMapper.selectList(queryWrapper);
                Map<Long, List<WeMsgTlp>> collect = weMsgTlps.stream().collect(Collectors.groupingBy(WeMsgTlp::getCategoryId));
                for (Map.Entry<Long, List<WeMsgTlp>> entry : collect.entrySet()) {
                    countMap.put(entry.getKey(), entry.getValue().size());
                }
            }

        }
        weCategories.forEach(c -> {
            WeCategoryVo weCategory = new WeCategoryVo();
            weCategory.setId(c.getId());
            weCategory.setName(c.getName());
            weCategory.setFlag(c.getFlag());
            weCategory.setParentId(c.getParentId());
            Integer count = countMap.get(c.getId());
            if (count != null) {
                weCategory.setNumber(count);
            }
            weCategoryVos.add(weCategory);
        });

        return TreeUtil.build(weCategoryVos);
    }

    @Override
    public void deleteWeCategoryById(Long[] ids) {
        new LambdaUpdateChainWrapper<>(this.baseMapper).set(WeCategory::getDelFlag, 2)
                .in(WeCategory::getId, Lists.newArrayList(ids)).update();
    }

    @Override
    @Transactional
    public void deleteWeCategoryByIds(Long[] ids) {
        List<Long> list = Lists.newArrayList(ids);
        new LambdaUpdateChainWrapper<>(this.baseMapper).set(WeCategory::getDelFlag, 2)
                .in(WeCategory::getId, list).update();
        for (Long id : list) {
            new LambdaUpdateChainWrapper<>(weMaterialMapper).set(WeMaterial::getCategoryId, id)
                    .eq(WeMaterial::getCategoryId, id).update();
        }
    }

    @Override
    @Transactional
    public void delOrMuchMove(WeCategoryNewVo weCategoryNewVo) {
        List<Long> ids = weCategoryNewVo.getIds();
        if (ObjectUtil.isEmpty(ids)) {
            throw new WeComException("id不能为空");
        }
        Integer updateOrDel = weCategoryNewVo.getUpdateOrDel();
        if (updateOrDel == 0) {
            updateObject(weCategoryNewVo.getModuleType(), weCategoryNewVo.getCateGoreId(), weCategoryNewVo.getIds(), "id");
        } else {
            new LambdaUpdateChainWrapper<>(this.baseMapper).set(WeCategory::getDelFlag, 1)
                    .in(WeCategory::getId, ids).update();
            updateObject(weCategoryNewVo.getModuleType(), weCategoryNewVo.getCateGoreId(), weCategoryNewVo.getIds(), "categoryId");
        }
    }

    public void updateObject(Integer moduleType, Long cateGoreId, List<Long> ids, String propertyName) {
        switch (CategoryModuleTypeEnum.getCategoryModuleTypeEnumByValue(moduleType)) {
            case MATERIAL:
                new LambdaUpdateChainWrapper<>(weMaterialMapper)
                        .set(WeMaterial::getCategoryId, cateGoreId)
                        .set(WeMaterial::getUpdateTime, new Date())
                        .in(propertyName.equals("id") ? WeMaterial::getId : WeMaterial::getCategoryId, ids).update();
                break;
            case TALK:
                new LambdaUpdateChainWrapper<>(weContentTalkMapper)
                        .set(WeContentTalk::getCategoryId, cateGoreId)
                        .set(WeContentTalk::getUpdateTime, new Date())
                        .in(propertyName.equals("id") ? WeContentTalk::getId : WeContentTalk::getCategoryId, ids).update();
                break;
            case TEMPLATE:
                new LambdaUpdateChainWrapper<>(weMsgTlpMapper)
                        .set(WeMsgTlp::getCategoryId, cateGoreId)
                        .set(WeMsgTlp::getUpdateTime, new Date())
                        .in(propertyName.equals("id") ? WeMsgTlp::getId : WeMsgTlp::getCategoryId, ids).update();
                break;
            default:
                throw new WeComException("该模块分组树未被定义");
        }
    }
}
