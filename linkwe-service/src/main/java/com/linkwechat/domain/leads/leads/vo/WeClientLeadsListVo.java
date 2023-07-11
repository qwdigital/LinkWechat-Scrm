package com.linkwechat.domain.leads.leads.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 线索
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/04/04 16:57
 */
@Data
public class WeClientLeadsListVo {

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 表头
     */
    private List<Header> header;

    /**
     * 列表数据
     */
    private List<Map<String, Object>> rows;

    /**
     * 总记录数
     */
    private long total;

    @Data
    public static class Header {
        private String key;
        private String title;
    }

    /**
     * 自定义字段属性
     */
    @Data
    public static class Properties {
        private Long id;
        private String key;
        private String keyEn;
        private String value;
    }


}
