package com.linkwechat.domain.wecom.vo.living;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @description 创建直播返回对象
 * @date 2022年10月11日 16:09
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeLivingIdListVo extends WeResultVo {

    /**
     * 当前数据最后一个key值
     */
    @ApiModelProperty("当前数据最后一个key值")
    private String nextCursor;
    /**
     * 直播ID列表
     */
    @ApiModelProperty("直播ID列表")
    private List<String> livingIdList;
}
