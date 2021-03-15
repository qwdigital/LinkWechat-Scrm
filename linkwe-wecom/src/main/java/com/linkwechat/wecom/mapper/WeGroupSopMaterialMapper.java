package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroupSopMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 群SOP - 素材 关联 Mapper
 */
@Mapper
@Repository
public interface WeGroupSopMaterialMapper extends BaseMapper<WeGroupSopMaterial> {

    /**
     * 批量保存 sop规则与素材的绑定
     * @param sopMaterialList 待绑定列表
     * @return 结果
     */
    int batchBindsSopMaterial(List<WeGroupSopMaterial> sopMaterialList);
}
