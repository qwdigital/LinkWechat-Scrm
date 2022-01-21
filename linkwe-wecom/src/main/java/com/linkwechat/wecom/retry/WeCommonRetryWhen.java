package com.linkwechat.wecom.retry;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.json.JSONUtil;
import com.dtflys.forest.callback.RetryWhen;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.linkwechat.common.config.WeComeConfig;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description token请求重试
 * @date 2021/9/30 10:21
 **/
@Slf4j
@Component
public class WeCommonRetryWhen implements RetryWhen {

    @Autowired
    private WeComeConfig weComeConfig;
    /**
     * 请求重试条件
     * @param forestRequest Forest请求对象
     * @param forestResponse Forest响应对象
     * @return true 重试，false 不重试  image/jpeg
     */
    @SneakyThrows
    @Override
    public boolean retryWhen(ForestRequest forestRequest, ForestResponse forestResponse) {

        WeResultDto weResultDto = JSONUtil.toBean(forestResponse.getContent(), WeResultDto.class);

        if (null != weResultDto.getErrcode() && weComeConfig.getWeNeedRetryErrorCodes().contains(weResultDto.getErrcode())){
            log.info("重试start: url:" + forestRequest.getUrl() + "------------result:" + forestResponse.getContent());
            return true;
        }
        return false;
    }
}
