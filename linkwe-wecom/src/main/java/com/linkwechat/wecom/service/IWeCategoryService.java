package com.linkwechat.wecom.service;

import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.wecom.domain.WeCategory;
import com.linkwechat.wecom.domain.vo.WeCategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWeCategoryService {

    /**
     * 保存素材分类
     *
     * @param category 素材分类 信息
     * @return {@link int}
     */
    int insertWeCategory(WeCategory category);

    /**
     * 保存素材分类
     *
     * @param category 素材分类 信息
     * @return {@link int}
     */
    int updateWeCategory(WeCategory category);

    /**
     * 删除分类信息
     *
     * @param id 主键id
     * @return {@link int}
     */
    int deleteWeCategoryById(Long id);

    /**
     * 批量删除
     *
     * @param ids id列表
     * @return {@link int}
     */
    int deleteWeCategoryByIds(Long[] ids);

    /**
     * 通过类型查询对应类目的分类信息列表
     *
     * @param mediaType 类型
     * @return {@link WeCategory}s
     */
    List<? extends Tree<?>> findWeCategoryByMediaType(@Param("mediaType") String mediaType);

    /**
     * 通过id查询分类信息
     *
     * @param id 主键id
     * @return {@link WeCategory}
     */
    WeCategory findWeCategoryById(@Param("id") Long id);

}
