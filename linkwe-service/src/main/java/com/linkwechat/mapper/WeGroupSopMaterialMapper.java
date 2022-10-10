package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.community.WeGroupSopMaterial;
import java.util.List;

/**
 * 群SOP - 素材 关联 Mapper
 */
public interface WeGroupSopMaterialMapper extends BaseMapper<WeGroupSopMaterial> {

    /**
     * 批量保存 sop规则与素材的绑定
     * @param sopMaterialList 待绑定列表
     * @return 结果
     */
    int batchBindsSopMaterial(List<WeGroupSopMaterial> sopMaterialList);
}
