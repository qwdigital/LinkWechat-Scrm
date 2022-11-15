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
public class WeSendGroupSopContentVo {


    //群名
    private String groupName;
    //群id
    private  String chatId;
    //群创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date addTime;

    //所有需要执行的sop
    private List<WeGroupSop> weGroupSops;





    @Data
    @Builder
    public static class WeGroupSop{

        //sop主键
        private String sopBaseId;

        //sop名称
        private String sopName;

        //sop执行内容
        private List<WeGroupSopContent> weGroupSopContents;

    }

    //客群sop内容
    @Data
    @Builder
    public static class WeGroupSopContent{
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
