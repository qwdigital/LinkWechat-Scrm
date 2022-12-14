package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.fegin.QwSysDeptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author danmo
 * @date 2022/11/29 22:59
 */
@Component
@Slf4j
public class QwSysDeptFallbackFactory implements QwSysDeptClient {


    @Override
    public AjaxResult<List<SysDeptVo>> getListByDeptIds(SysDeptQuery query) {
        return null;
    }
}
