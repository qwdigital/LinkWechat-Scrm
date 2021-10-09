package com.linkwechat.wecom.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @description: 统一返回体
 * @author: HaoN
 * @create: 2020-08-27 15:59
 **/
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeResultDto {
    /**
     * 泛指各种返回id
     */
    private Long id;

    /**
     * 接口返回错误码
     */
    private Integer errcode;

    /**
     * 接口返回错误信息
     */
    private String errmsg;
}
