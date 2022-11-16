package com.linkwechat.domain.material.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeCategoryNewVo {


    private List<Long> ids;

    /**
     * 分组中心，默认1，删除不需要传
     */
    private Long cateGoreId = 1L;

    /**
     * 操作模块 默认-1必须要传; 1素材中心2话术中心3模板中心
     */
    private Integer moduleType = -1;

    /**
     * 0移动1删除。默认值是0，修改可以不传
     */
    private Integer updateOrDel = 0;
}
