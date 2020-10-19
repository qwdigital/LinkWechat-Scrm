package com.linkwechat.wecom.domain.dto.tag;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.List;

/**
 * @description: 标签列表实体
 * @author: HaoN
 * @create: 2020-10-17 20:36
 **/
@Data
public class WeCropGroupTagListDto extends WeResultDto {

    private List<WeCropGroupTagDto> tag_group;
}
