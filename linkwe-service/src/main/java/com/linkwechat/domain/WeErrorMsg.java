package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 企业微信响应错误参数记录表
 * @TableName we_error_msg
 */
@TableName(value ="we_error_msg")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeErrorMsg extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 请求url
     */
    private String url;

    /**
     * 错误码
     */
    private Long errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 请求参数
     */
    private String weParams;


}