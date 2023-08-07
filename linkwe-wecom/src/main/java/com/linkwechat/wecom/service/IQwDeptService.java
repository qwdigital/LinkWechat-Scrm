package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.vo.department.WeDeptIdVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptInfoVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/3/23 21:10
 */
public interface IQwDeptService {
    /**
     * 获取部门列表
     *
     * @param query
     * @return
     */
    WeDeptVo getDeptList(WeDeptQuery query);

    WeDeptIdVo getDeptSimpleList(WeDeptQuery query);

    WeDeptInfoVo getDeptDetail(WeDeptQuery query);
}
