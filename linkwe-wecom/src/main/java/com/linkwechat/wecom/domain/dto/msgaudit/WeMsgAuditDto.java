package com.linkwechat.wecom.domain.dto.msgaudit;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 会话存档
 * @date 2020/12/2 16:47
 **/
@Data
public class WeMsgAuditDto extends WeResultDto {

    /**
     * 设置在开启范围内的成员的userid列表
     */
    private List<String> ids;

    /**
     * 同意情况
     */
    private List<AgreeInfo> agreeInfo;

    /**
     * roomid对应的群名称
     */
    private String roomName;

    /**
     * roomid对应的群创建者，userid
     */
    private String creator;

    /**
     * roomid对应的群创建时间
     */
    @JSONField(format = "unixtime")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date roomCreateTime;

    /**
     * roomid对应的群公告
     */
    private String notice;

    /**
     * roomid对应的群成员列表
     */
    private List<Member> members;

    private class Member {
        /**
         * roomid群成员的id，userid
         */
        private String memberId;

        /**
         * roomid群成员的入群时间
         */
        private String joinTime;
    }

    private class AgreeInfo {
        /**
         * 同意状态改变的具体时间，utc时间
         */
        private String statusChangeTime;

        /**
         * 成员Id
         */
        private String userId;

        /**
         * 群内外部联系人的externalopenid
         */
        private String exteranaLopenId;

        /**
         * 同意:”Agree”，不同意:”Disagree”，默认同意:”Default_Agree”
         */
        private String agreeSagreeStatustatus;
    }
}
