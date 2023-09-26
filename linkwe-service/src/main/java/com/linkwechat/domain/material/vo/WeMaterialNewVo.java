package com.linkwechat.domain.material.vo;

import com.linkwechat.domain.material.entity.WeMaterial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeMaterialNewVo extends WeMaterial {
    private String categoryName;
    private Integer sendNum;
    private Integer viewNum;
    private Integer viewByNum;

    private String tagIds;


    private String tagNames;
    //如果素材为表单类型则有该字段
//    private Long formSurveyId;
//    //为表单类型当前字段为表单名称
//    private String formSurveyName;
//   //为表单类型当前字段为表单名称
//    private String fromChannelsName;
}
