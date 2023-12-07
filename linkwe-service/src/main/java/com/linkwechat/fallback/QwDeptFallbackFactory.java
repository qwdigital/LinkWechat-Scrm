package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysDept;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.vo.department.WeDeptIdVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptInfoVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.fegin.QwDeptClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author leejoker
 * @date 2022/3/29 23:00
 */
@Component
@Slf4j
public class QwDeptFallbackFactory implements QwDeptClient {
    @Override
    public AjaxResult<WeDeptVo> getDeptList(WeDeptQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeDeptInfoVo> getDeptDetail(WeDeptQuery query) {
        return null;
    }

}
