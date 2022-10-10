package com.linkwechat.domain.taskfission.vo;

import com.linkwechat.domain.WeTaskFission;
import com.linkwechat.domain.WeTaskFissionRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class WeTaskFissionRecordVo extends WeTaskFission {
    /**
     * 任务记录
     */
    @ApiModelProperty(value = "任务记录")
    private List<WeTaskFissionRecord> taskFissionRecordList;

}
