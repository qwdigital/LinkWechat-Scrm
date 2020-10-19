package com.linkwechat.wecom.domain.dto.tag;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

/**
 * @description: 单个标签组
 * @author: HaoN
 * @create: 2020-10-17 23:51
 **/
@Data
public class WeCropGropTagDtlDto extends WeResultDto {

    private WeCropGroupTagDto tag_group;
}
