package com.linkwechat.wecom.domain.dto.moments;

import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

/**
 * 朋友圈创建结果
 */
@Data
public class MomentsCreateResultDto extends WeResultDto {
    //任务状态，整型，1表示开始创建任务，2表示正在创建任务中，3表示创建任务已完成
    private  Integer status;
    //操作类型，字节串，此处固定为add_moment_task
    private String type;

    private Result result;


    @Data
    public static class Result extends WeResultDto{
        //朋友圈id
       private String moment_id;

    }

}
