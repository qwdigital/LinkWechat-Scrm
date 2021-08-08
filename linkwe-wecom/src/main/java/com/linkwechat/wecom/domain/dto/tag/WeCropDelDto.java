package com.linkwechat.wecom.domain.dto.tag;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 标签删除参数实体
 * @author: HaoN
 * @create: 2020-10-18 00:22
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeCropDelDto {

    private String[] tag_id;

    private String[] group_id;

}
