package com.linkwechat.wecom.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 会话存档入参实体
 * @date 2020/12/19 12:38
 **/
@Data
public class WeMsgAuditVo {
    /**
     * 拉取对应版本的开启成员列表。1表示办公版；2表示服务版；3表示企业版。
     * 非必填，不填写的时候返回全量成员列表
     */
    private Integer type;

    /** 待查询的roomid */
    private String roomid;

    /** 待查询的会话信息，数组 */
    private List<Info> info;

    @Data
    private class Info {
        /**
         * 内部成员的userid
         */
        private String userid;
        /**
         * 外部成员的externalopenid
         */
        private String exteranalopenid;
    }
}
