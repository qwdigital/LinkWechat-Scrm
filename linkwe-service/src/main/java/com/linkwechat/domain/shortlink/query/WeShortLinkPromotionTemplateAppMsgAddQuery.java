package com.linkwechat.domain.shortlink.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 短链推广模板-应用消息
 * </p>
 *
 * @author WangYX
 * @since 2023-03-14
 */
@ApiModel
@Data
public class WeShortLinkPromotionTemplateAppMsgAddQuery {

    /**
     * 执行员工
     */
    @ApiModelProperty(value = "执行员工")
    public WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit;

    /**
     * 执行部门与岗位
     */
    @ApiModelProperty(value = "执行部门与岗位")
    public WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit;

    /**
     * 发送类型：0立即发送 1定时发送
     */
    @ApiModelProperty(value = "发送类型：0立即发送 1定时发送")
    private Integer sendType;

    /**
     * 定时发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "定时发送时间")
    private LocalDateTime taskSendTime;

    /**
     * 任务结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "任务结束时间")
    private LocalDateTime taskEndTime;


    @Data
    public static class ExecuteUserCondit {

        /**
         * 是否选择,默认不选择
         */
        private boolean change = false;
        /**
         * 员工id
         */
        private List<String> weUserIds;
    }

    @Data
    public static class ExecuteDeptCondit {
        /**
         * 是否选择,默认不选择
         */
        private boolean change = false;

        /**
         * 部门范围id
         */
        private List<String> deptIds;

        /**
         * 岗位名称集合
         */
        private List<String> posts;
    }


}
