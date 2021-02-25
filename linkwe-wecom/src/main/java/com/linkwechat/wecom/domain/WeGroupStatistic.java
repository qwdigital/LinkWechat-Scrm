package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 群聊数据统计数据
 * 对象 we_group_statistic
 *
 * @author ruoyi
 * @date 2021-02-24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("we_group_statistic")
public class WeGroupStatistic implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 群主ID
     */
    @Excel(name = "群ID")
    private String chatId;

    /**
     * 数据日期
     */
    @Excel(name = "数据日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date statTime;

    /**
     * 新增客户群数量
     */
    @Excel(name = "新增客户群数量")
    private Integer newChatCnt;

    /**
     * 截至当天客户群总数量
     */
    @Excel(name = "截至当天客户群总数量")
    private Integer chatTotal;

    /**
     * 截至当天有发过消息的客户群数量
     */
    @Excel(name = "截至当天有发过消息的客户群数量")
    private Integer chatHasMsg;

    /**
     * 客户群新增群人数
     */
    @Excel(name = "客户群新增群人数")
    private Integer newMemberCnt;

    /**
     * 截至当天客户群总人数
     */
    @Excel(name = "截至当天客户群总人数")
    private Integer memberTotal;

    /**
     * 截至当天有发过消息的群成员数
     */
    @Excel(name = "截至当天有发过消息的群成员数")
    private Integer memberHasMsg;

    /**
     * 截至当天客户群消息总数
     */
    @Excel(name = "截至当天客户群消息总数")
    private Integer msgTotal;

    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
