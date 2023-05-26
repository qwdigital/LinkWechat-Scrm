package com.linkwechat.domain.wecom.query.msgaudit;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author danmo
 * @description 会话存档
 * @date 2020/12/2 16:47
 **/
@Data
public class WeMsgAuditQuery extends WeBaseQuery {

    /**
     * 拉取对应版本的开启成员列表。1表示办公版；2表示服务版；3表示企业版。非必填，不填写的时候返回全量成员列表。
     */
    private Integer type;

    /**
     * 待查询的会话信息，数组
     */
    private List<MsgAuditInfo> info;

    /**
     * 	待查询的roomid
     */
    private String roomid;


    @Data
    public static class MsgAuditInfo{

        /**
         * 内部成员的userid
         */
        private String userid;

        /**
         * 外部成员的exteranalopenid
         */
        private String exteranalopenid;
    }

}
