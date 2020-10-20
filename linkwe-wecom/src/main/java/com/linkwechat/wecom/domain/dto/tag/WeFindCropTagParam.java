package com.linkwechat.wecom.domain.dto.tag;

import lombok.Builder;
import lombok.Data;

/**
 * @description: 获取标签接口参数实体
 * @author: HaoN
 * @create: 2020-10-20 13:03
 **/
@Data
@Builder
public class WeFindCropTagParam {
    private String[] tag_id;
}
