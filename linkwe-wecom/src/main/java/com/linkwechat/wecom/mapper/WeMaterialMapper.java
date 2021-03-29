package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.vo.WeMaterialVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 素材mapper
 *
 * @author KEWEN
 * @date 2020-10-09
 */
public interface WeMaterialMapper extends BaseMapper<WeMaterial> {


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
     * @param mediaType  0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本
     * @return {@link WeMaterial}s
     */
    List<WeMaterial> findWeMaterials(@Param("categoryId") String categoryId, @Param("search") String search, @Param("mediaType") String mediaType);

    /**
     * 更换分组
     *
     * @param categoryId 类目id
     * @param material   素材id
     * @return int
     */
    int resetCategory(@Param("categoryId") String categoryId, @Param("material") String material);

    /**
     * 根据id列表获取素材Vo列表
     * @param ids 素材id列表
     * @return 结果
     */
    List<WeMaterialVo> findMaterialVoListByIds(Long[] ids);
}
