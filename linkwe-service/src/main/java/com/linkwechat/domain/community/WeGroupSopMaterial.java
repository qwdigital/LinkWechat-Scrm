package com.linkwechat.domain.community;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群SOP图片素材
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("we_group_sop_material")
public class WeGroupSopMaterial {

    @TableId
    private Long id;
    /**
     * sop规则id
     */
    private Long ruleId;

    /**
     * 素材id
     */
    private Long materialId;
}
