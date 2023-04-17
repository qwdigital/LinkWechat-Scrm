package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.vo.department.WeDeptIdVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptInfoVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.wecom.client.WeDeptClient;
import com.linkwechat.wecom.service.IQwDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author leejoker
 * @version 1.0
 * @date 2022/3/23 21:11
 */
@Service
public class QwDeptServiceImpl implements IQwDeptService {
    @Resource
    private WeDeptClient client;

    @Override
    public WeDeptVo getDeptList(WeDeptQuery query) {
        return client.getDeptList(query);
    }

    @Override
    public WeDeptIdVo getDeptSimpleList(WeDeptQuery query) {
        return client.getDeptSimpleList(query);
    }

    @Override
    public WeDeptInfoVo getDeptDetail(WeDeptQuery query) {
        return client.getDeptDetail(query);
    }
}
