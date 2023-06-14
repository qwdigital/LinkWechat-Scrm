package com.linkwechat.domain.material.vo;

import com.linkwechat.domain.material.entity.WeMaterial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeMaterialNewVo extends WeMaterial {
    private String categoryName;
    private Integer sendNum;
    private Integer viewNum;
    private Integer viewByNum;
}
