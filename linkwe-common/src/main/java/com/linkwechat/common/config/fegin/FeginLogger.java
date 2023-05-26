package com.linkwechat.common.config.fegin;

import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static feign.Util.decodeOrDefault;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author danmo
 * @description fegin日志统一输出
 * @date 2022/03/12 11:06
 **/
@Slf4j
public class FeginLogger extends feign.Logger {

    @Override
    protected Response logAndRebufferResponse(String configKey,
                                              Level logLevel,
                                              Response response,
                                              long elapsedTime) throws IOException {

        Request request = response.request();
        String requestMsg = request.httpMethod().name() + " " + request.url() + " HTTP/1.1";
        boolean hasReqBody = request.body() != null;
        String bodyMsg = hasReqBody ? new String(request.body()) : "";
        if(requestMsg.contains("/file/upload")){
            bodyMsg = "";
        }
        String responseMsg = "";
        int status = response.status();
        boolean hasResBody = response.body() != null && !(status == 204 || status == 205);
        if (hasResBody) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            response = response.toBuilder().body(bodyData).build();
            responseMsg = decodeOrDefault(bodyData, UTF_8, "");
        }
        log(configKey, "request【%s】， body【%s】, response【%s】", requestMsg, bodyMsg, responseMsg);
        return response;
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        log(configKey, "<--- ERROR %s: %s (%sms)", ioe.getClass().getSimpleName(), ioe.getMessage(),
                elapsedTime);
        return ioe;
    }


    @Override
    protected void log(String configKey, String format, Object... args) {
        if (log.isInfoEnabled()) {
            log.info(String.format(methodTag(configKey) + format, args));
        }
    }
}
