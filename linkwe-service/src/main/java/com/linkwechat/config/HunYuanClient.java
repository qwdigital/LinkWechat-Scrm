package com.linkwechat.config;
/*
 * Copyright (c) 2017-2018 THL A29 Limited, a Tencent company. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tencentcloudapi.common.AbstractClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.SSEResponseModel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.hunyuan.v20230901.models.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class HunYuanClient extends AbstractClient {
    private static String endpoint = "hunyuan.tencentcloudapi.com";
    private static String service = "hunyuan";
    private static String version = "2023-09-01";

    public HunYuanClient(Credential credential, String region) {
        this(credential, region, new ClientProfile());
    }

    public HunYuanClient(Credential credential, String region, ClientProfile profile) {
        super(HunYuanClient.endpoint, HunYuanClient.version, credential, region, profile);
    }

    /**
     * 腾讯混元大模型高级版是由腾讯研发的大语言模型，具备强大的中文创作能力，复杂语境下的逻辑推理能力，以及可靠的任务执行能力。本接口为SSE协议。
     * <p>
     * 1.本接口暂不支持返回图片内容。
     * 2.默认单账号限制并发数为5路，如您有提高并发限制的需求请 [联系我们](https://cloud.tencent.com/act/event/Online_service) 。
     *
     * @param req ChatProRequest
     * @return ChatProResponse
     * @throws TencentCloudSDKException
     */
    public ChatProResponse ChatPro(ChatProRequest req) throws TencentCloudSDKException {
        req.setSkipSign(false);
        return this.internalRequest(req, "ChatPro", ChatProResponse.class);
    }

    /**
     * 腾讯混元大模型标准版是由腾讯研发的大语言模型，具备强大的中文创作能力，复杂语境下的逻辑推理能力，以及可靠的任务执行能力。本接口为SSE协议。
     * <p>
     * 1.本接口暂不支持返回图片内容。
     * 2.默认单账号限制并发数为5路，如您有提高并发限制的需求请 [联系我们](https://cloud.tencent.com/act/event/Online_service) 。
     *
     * @param req ChatStdRequest
     * @return ChatStdResponse
     * @throws TencentCloudSDKException
     */
    public ChatStdResponse ChatStd(ChatStdRequest req) throws TencentCloudSDKException {
        req.setSkipSign(false);
        return this.internalRequest(req, "ChatStd", ChatStdResponse.class);
    }

    /**
     * 该接口用于计算文本对应Token数、字符数。
     *
     * @param req GetTokenCountRequest
     * @return GetTokenCountResponse
     * @throws TencentCloudSDKException
     */
    public GetTokenCountResponse GetTokenCount(GetTokenCountRequest req) throws TencentCloudSDKException {
        req.setSkipSign(false);
        return this.internalRequest(req, "GetTokenCount", GetTokenCountResponse.class);
    }

    public void sendMsg(Message[] msgList, Consumer<String> consumer) {
        ChatStdRequest chatStdRequest = new ChatStdRequest();
        chatStdRequest.setMessages(msgList);
        try {
            ChatStdResponse stdResponse = ChatStd(chatStdRequest);
            for (SSEResponseModel.SSE next : stdResponse) {
                log.info("stdResponse:{}",next.Data);
                consumer.accept(next.Data);
            }
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }

}