package com.linkwechat.domain.moments.dto;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 朋友圈创建结果
 */
@Data
public class MomentsCreateResultDto extends WeResultVo {

    /**
     *  任务状态，整型，1表示开始创建任务，2表示正在创建任务中，3表示创建任务已完成
     */
    private Integer status;

    /**
     * 操作类型，字节串，此处固定为add_moment_task
     */
    private String type;

    private Result result;


    @Data
    public static class Result extends WeResultVo {

        /**
         * 朋友圈id
         */
        private String momentId;

        @ApiModelProperty("指定的发表范围")
        private WeMomentSendVo invalidSenderList;

        @ApiModelProperty("可见到该朋友圈的客户列表")
        private WeMomentCustomerVo invalidExternalContactList;
    }

    @ApiModel
    @Data
    public static class WeMomentSendVo {

        @ApiModelProperty("发表任务的执行者用户列表，最多支持10万个")
        private List<String> userList;


        @ApiModelProperty("发表任务的执行者部门列表")
        private List<String> departmentList;
    }

    @ApiModel
    @Data
    public static class WeMomentCustomerVo {

        @ApiModelProperty("可见到该朋友圈的客户标签列表")
        private List<String> tagList;

    }

}
