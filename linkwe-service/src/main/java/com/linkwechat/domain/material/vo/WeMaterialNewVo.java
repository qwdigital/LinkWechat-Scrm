package com.linkwechat.domain.material.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.domain.material.entity.WeMaterial;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeMaterialNewVo  extends WeMaterial {
    private String categoryName;
    private Integer sendNum;
    private Integer viewNum;
    private Integer viewByNum;
}
