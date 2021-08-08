package com.linkwechat.wecom.domain.vo;


import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群活码简略信息
 * @Author Hang
 * @Date 2021/3/26 14:21
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WeGroupCodeVo {

    private Long id;

    private String codeUrl;
}