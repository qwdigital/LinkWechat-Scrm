package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.material.entity.WeCategory;

/**
 * 智能表单分组
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/13 9:39
 */
public interface IWeFormSurveyCategoryService extends IService<WeCategory> {


    /**
     * 保存分组
     *
     * @param category
     * @return
     * @author WangYX
     * @date 2022/10/13 9:43
     */
    void insertWeCategory(WeCategory category);

    /**
     * 更新分组
     *
     * @param category
     * @return
     * @author WangYX
     * @date 2022/10/13 9:43
     */
    void updateWeCategory(WeCategory category);

    /**
     * 删除分组
     *
     * @param ids
     * @return
     * @author WangYX
     * @date 2022/10/13 9:43
     */
    void deleteWeCategoryById(Long[] ids);


}
