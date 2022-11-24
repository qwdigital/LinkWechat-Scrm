package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.vo.SysAreaVo;
import com.linkwechat.fegin.QwSysAreaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author danmo
 * @date 2022/4/29 22:59
 */
@Component
@Slf4j
public class QwSysAreaFallbackFactory implements QwSysAreaClient {



    @Override
    public AjaxResult<List<SysAreaVo>> getChildListById(Integer id) {
        return null;
    }

    @Override
    public AjaxResult<SysAreaVo> getAreaById(Integer id) {
        return null;
    }
}
