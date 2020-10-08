package com.linkwechat.wecom.mapper;

import com.linkwechat.wecom.domain.WeCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeCategoryMapper {

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
    int deleteWeCategoryById(@Param("id") Long id);

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
    List<WeCategory> findWeCategoryByMediaType(@Param("mediaType") String mediaType);

    /**
     * 通过id查询分类信息
     *
     * @param id 主键id
     * @return {@link WeCategory}
     */
    WeCategory findWeCategoryById(@Param("id") Long id);

    /**
     * 通过类型和名称查询类目信息
     *
     * @param mediaType 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本
     * @param name      分类名称
     * @return {@link WeCategory}
     */
    WeCategory findWeCategoryByMediaTypeAndName(@Param("mediaType") String mediaType, @Param("name") String name);
}
