package com.linkwechat.wecom.domain.dto.message;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 群发记录列表返回值
 * @date 2021/10/3 16:30
 **/
@ApiModel
@Data
public class WeGroupMsgListDto extends WeResultDto {

    @ApiModelProperty("分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空")
    private String nextCursor;

    @ApiModelProperty("群发记录列表")
    private List<WeGroupMsgDto> groupMsgList;

    @ApiModelProperty("群发成员发送任务列表")
    private List<WeGroupMsgTaskDto>  taskList;

    @ApiModelProperty("群成员发送结果列表")
    private List<WeGroupMsgSendDto>  sendList;
}
