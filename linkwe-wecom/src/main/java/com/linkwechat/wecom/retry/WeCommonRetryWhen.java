package com.linkwechat.wecom.retry;

import cn.hutool.json.JSONUtil;
import com.dtflys.forest.callback.RetryWhen;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author danmo
 * @description token请求重试
 * @date 2021/9/30 10:21
 **/
@Slf4j
@Component
public class WeCommonRetryWhen implements RetryWhen {

    /**
     * 请求重试条件
     * @param forestRequest Forest请求对象
     * @param forestResponse Forest响应对象
     * @return true 重试，false 不重试
     */
    @Override
    public boolean retryWhen(ForestRequest forestRequest, ForestResponse forestResponse) {

      try {
          WeResultVo WeResultVo = JSONUtil.toBean(forestResponse.getContent(), WeResultVo.class);
          if (null != WeResultVo.getErrCode()){
              log.info("重试start: url:" + forestRequest.getUrl() + "------------result:" + forestResponse.getContent());
              return true;
          }
          return false;
      }catch (Exception e){
          return false;
      }

    }
}
