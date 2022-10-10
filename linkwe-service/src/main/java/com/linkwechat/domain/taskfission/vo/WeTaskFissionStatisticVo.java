package com.linkwechat.domain.taskfission.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @version 1.0
 * @date 2022/7/1 10:20
 */
@Data
public class WeTaskFissionStatisticVo {
    private Long taskFissionId;

    private String taskName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    private List<WeTaskFissionDailyDataVo> data;
}
