package com.linkwechat.wecom.domain.dto.tag;

import lombok.experimental.SuperBuilder;
import lombok.Data;

import java.util.List;

/**
 * @description: 获取标签接口参数实体
 * @author: HaoN
 * @create: 2020-10-20 13:03
 **/
@Data
@SuperBuilder
public class WeFindCropTagParam {
    private String[] tag_id;
    private List<String> group_id;
}
