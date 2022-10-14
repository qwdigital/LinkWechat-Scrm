package com.linkwechat.domain.wecom.query.living;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @description 直播接口入参
 * @date 2022/10/10 10:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeActivityDetailQuery extends WeBaseQuery {

    /**
     * 活动直播特定参数，活动直播简介
     */
    @ApiModelProperty("活动直播特定参数，活动直播简介")
    private String description;

    /**
     * 活动直播特定参数，活动直播附图的mediaId列表，最多支持传5张，超过五张取前五张
     */
    @ApiModelProperty("活动直播特定参数，活动直播附图的mediaId列表，最多支持传5张，超过五张取前五张")
    private List<String> image_list;
}
