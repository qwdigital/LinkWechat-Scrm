package com.linkwechat.wecom.domain.dto.kf;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author danmo
 * @description 客服企微实体
 * @date 2021/10/9 14:36
 **/
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeKfDto extends WeResultDto{

    @ApiModelProperty("客服帐号ID")
    private String open_kfid;

    @ApiModelProperty("客服名称")
    private String name;

    @ApiModelProperty("客服头像")
    private String avatar;

    @ApiModelProperty("客服头像临时素材id")
    private String media_id;

    @ApiModelProperty("客服链接")
    private String url;

    @ApiModelProperty("场景值")
    private String scene;

    @ApiModelProperty("接待人员userid列表")
    private List<String> userid_list;

    @ApiModelProperty("微信客户的external_userid")
    private String external_userid;

    @ApiModelProperty("当前的会话状态")
    private Integer service_state;

    @ApiModelProperty("接待人员的userid，仅当state=3时有效")
    private String servicer_userid;

    @ApiModelProperty("用于发送响应事件消息的code，将会话初次变更为service_state为2和3时，返回回复语code，service_state为4时，返回结束语code。")
    private String msg_code;

    @ApiModelProperty("external_userid列表 超过100个需分批调用")
    private List<String> external_userid_list;
}
