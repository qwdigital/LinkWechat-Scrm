package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 群发消息 原始数据信息表 we_customer_messageOriginal
 *
 * @author kewen
 * @date 2020-12-05
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_customer_messageOriginal")
public class WeCustomerMessageOriginal extends BaseEntity {

    /**
     * 主键id
     */
    private Long messageOriginalId;

    /**
     * 群发类型 0 发给客户 1 发给客户群
     */
    private String pushType;


    /**
     * 消息类型 0 文本消息  1 图片消息 2 语音消息  3 视频消息    4 文件消息 5 文本卡片消息 6 图文消息\r\n7 图文消息（mpnews） 8 markdown消息 9 小程序通知消息 10 任务卡片消息
     */
    private String messageType;

    /**
     * 员工id
     */
    private String staffId;

    /**
     * 客户标签id列表
     */
    private String tag;

    /**
     * 部门id
     */
    private String department;


    /**
     * 消息范围 0 全部客户  1 指定客户
     */
    private String pushRange;


    /**
     * 删除标识位 0 未删除  1 已删除
     */
    private int delFlag;

}
