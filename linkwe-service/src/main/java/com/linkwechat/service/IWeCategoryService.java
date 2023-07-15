package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.domain.material.entity.WeCategory;
import com.linkwechat.domain.material.vo.WeCategoryNewVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/4/1 21:14
 */
public interface IWeCategoryService extends IService<WeCategory> {

    /**
     * 保存素材分类
     *
     * @param category 素材分类 信息
     */
    void insertWeCategory(WeCategory category);

    /**
     * 保存素材分类
     *
     * @param category 素材分类 信息
     */
    void updateWeCategory(WeCategory category);


    /**
     * 通过类型查询对应类目的分类信息列表
     *
     * @param mediaType 类型
     * @return {@link WeCategory}
     */
    List<? extends Tree<?>> findWeCategoryByMediaType(@Param("mediaType") String mediaType, @RequestParam(value = "scene", required = false) Integer scene);

    void deleteWeCategoryById(Long[] ids);

    void deleteWeCategoryByIds(Long[] ids);

    /**
     * 批量删除或批量移动
     *
     * @param weCategoryNewVo
     */
    void delOrMuchMove(WeCategoryNewVo weCategoryNewVo);
}
