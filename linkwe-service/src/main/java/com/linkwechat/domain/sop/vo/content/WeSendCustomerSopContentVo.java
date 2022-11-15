package com.linkwechat.domain.sop.vo.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.sop.WeSopAttachments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * 待发送的客户sop
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeSendCustomerSopContentVo {


    //客户名称
    private String customerName;
    //客户类型
    private Integer customerType;
    //客户id
    private String externalUserid;
    //客户性别
    private Integer gender;
    //客户头像
    private String avatar;
    //1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    private Integer trackState;

    //所有需要执行的sop
    private List<WeCustomerSop> weCustomerSops;





    @Data
    @Builder
    public static class WeCustomerSop{

        //sop主键
        private String sopBaseId;

        //sop名称
        private String sopName;

        //sop执行内容
        private List<WeCustomerSopContent> weCustomerSopContents;

    }

    //客户sop内容
    @Data
    @Builder
    public static class WeCustomerSopContent{
        //内容明细
        private WeSopAttachments weQrAttachments;
        //推送具体开始时间
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date pushStartTime;
        //推送具体结束时间
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date pushEndTime;
        //执行状态(0:未执行;1:已执行)
        private Integer executeState;

        private String executeTargetAttachId;
    }



}
