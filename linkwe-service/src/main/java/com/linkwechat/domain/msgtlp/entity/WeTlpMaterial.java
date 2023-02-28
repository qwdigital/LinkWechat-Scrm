package com.linkwechat.domain.msgtlp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("we_tlp_material")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "模板素材关联表")
public class WeTlpMaterial {

    private Long tlpId;

    private Long materialId;
}
