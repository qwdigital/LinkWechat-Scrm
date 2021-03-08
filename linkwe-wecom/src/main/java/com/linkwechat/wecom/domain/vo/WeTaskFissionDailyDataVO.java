package com.linkwechat.wecom.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/3/8 10:24
 */
@Data
public class WeTaskFissionDailyDataVO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date day;
    private Integer increase;
    private Integer attend;
    private Integer complete;
}
