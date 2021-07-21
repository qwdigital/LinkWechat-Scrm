package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.wecom.domain.WeCategory;
import com.linkwechat.wecom.domain.vo.WeCategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * @return {@link WeCategory}s
     */
    List<? extends Tree<?>> findWeCategoryByMediaType(@Param("mediaType") String mediaType);

    void deleteWeCategoryById(Long[] ids);



}
