package com.linkwechat.common.core.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @description:
 * @author: HaoN
 * @create: 2021-07-13 15:25
 **/
@Data
@Builder
public class FileVo {
    private  String fileName;
    private String url;
}
