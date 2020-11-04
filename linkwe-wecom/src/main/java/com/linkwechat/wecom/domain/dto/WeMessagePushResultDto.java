package com.linkwechat.wecom.domain.dto;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.utils.ReflectUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Optional;

/**
 * @description: 发送应用消息响应应用结果
 * @author: KeWen
 * @create: 2020-10-27 22:46
 **/
@Data
public class WeMessagePushResultDto extends WeResultDto {

    /**
     * 无效用户
     */
    private String invaliduser;

    /**
     * 无效单位
     */
    private String invalidparty;

    /**
     * 无效标签
     */
    private String invalidtag;

}
