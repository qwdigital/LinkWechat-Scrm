package com.linkwechat.domain.wecom.vo.customer.moment;

import com.linkwechat.domain.wecom.entity.customer.moment.WeMomentEntity;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 获取企业全部的发表列表
 * @date 2021/12/2 16:11
 **/
@ApiModel
@Data
public class WeMomentListVo extends WeResultVo {
    @ApiModelProperty("朋友圈列表")
    private List<WeMomentEntity> momentList;
}
