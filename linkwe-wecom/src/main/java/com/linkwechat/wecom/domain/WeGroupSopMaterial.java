package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群SOP图片素材
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("we_group_sop_material")
public class WeGroupSopMaterial {

    /**
     * sop规则id
     */
    private Long ruleId;

    /**
     * 素材id
     */
    private Long materialId;
}
