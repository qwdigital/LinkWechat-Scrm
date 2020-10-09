package com.linkwechat.wecom.mapper;

import com.linkwechat.wecom.domain.WeMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 素材mapper
 *
 * @author KEWEN
 * @date 2020-10-09
 */
public interface WeMaterialMapper {


    /**
     * 添加素材信息
     *
     * @param WeMaterial 素材信息
     * @return
     */
    int insertWeMaterial(WeMaterial WeMaterial);

    /**
     * 删除素材信息
     *
     * @param id 主键id
     * @return {@link int}
     */
    int deleteWeMaterialById(Long id);

    /**
     * 批量删除
     *
     * @param ids id列表
     * @return {@link int}
     */
    int deleteWeMaterialByIds(Long[] ids);

    /**
     * 更新素材信息
     *
     * @param WeMaterial
     * @return
     */
    int updateWeMaterial(WeMaterial WeMaterial);

    /**
     * 查询素材详细信息
     *
     * @param id id
     * @return {@link WeMaterial}
     */
    WeMaterial findWeMaterialById(Long id);

    /**
     * 查询素材列表
     *
     * @param categoryId 类目id
     * @param search     搜索值
     * @return {@link WeMaterial}s
     */
    List<WeMaterial> findWeMaterials(@Param("categoryId") String categoryId, @Param("search") String search);

}
