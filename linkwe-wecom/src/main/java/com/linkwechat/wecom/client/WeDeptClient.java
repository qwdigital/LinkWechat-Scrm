package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.domain.wecom.query.department.WeAddDeptQuery;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.department.WeAddDeptVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptIdVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptInfoVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.wecom.interceptor.WeAddressBookAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAppAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @author danmo
 * @Description 部门接口
 * @date 2021/12/8 14:52
 **/

@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeAppAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeDeptClient {
    /**
     * 创建成员
     *
     * @param query
     * @return WeAddDeptVo
     */
    @Request(url = "/department/create", type = "POST")
    WeAddDeptVo addDept(@JSONBody WeAddDeptQuery query);

    /**
     * 更新部门
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/department/update", type = "POST")
    WeResultVo updateDept(@JSONBody WeAddDeptQuery query);

    /**
     * 删除部门
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/department/delete?id=${query.id}", type = "GET")
    WeResultVo delDept(@Var("query") WeDeptQuery query);

    /**
     * 获取部门列表
     *
     * @param query
     * @return WeDeptVo
     */
    @Request(url = "/department/list", type = "GET")
    WeDeptVo getDeptList(@Var("query") WeDeptQuery query);


    @Request(url = "/department/simplelist", type = "GET")
    WeDeptIdVo getDeptSimpleList(WeDeptQuery query);


    @Get(url = "/department/get?id=${query.id}")
    WeDeptInfoVo getDeptDetail(@Var("query") WeDeptQuery query);
}
