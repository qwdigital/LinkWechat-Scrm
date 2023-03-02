package com.linkwechat.domain.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("we_talk_material")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "话术素材关联表")
public class WeTalkMaterial {

    private Long talkId;

    private Long materialId;

    private Integer sort;

}
