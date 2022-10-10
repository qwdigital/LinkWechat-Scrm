package com.linkwechat.wecom.client;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.user.tag.WeAddTagUsersQuery;
import com.linkwechat.domain.wecom.query.user.tag.WeAddUserTagQuery;
import com.linkwechat.domain.wecom.query.user.tag.WeUserTagQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.user.tag.WeUserTagDetailVo;
import com.linkwechat.domain.wecom.vo.user.tag.WeUserTagListVo;
import com.linkwechat.domain.wecom.vo.user.tag.WeUserTagVo;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAddressBookAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeAppAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;
import com.dtflys.forest.annotation.*;

/**
 * @author danmo
 * @Description 成员标签管理接口
 * @date 2021/12/7 15:28
 **/
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeAppAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeUserTagClient {
    /**
     * 创建标签
     *
     * @param query
     * @return WeUserTagVo
     */
    @Request(url = "/tag/create", type = "POST")
    WeUserTagVo addUserTag(@JSONBody WeAddUserTagQuery query);

    /**
     * 更新标签名字
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/tag/update", type = "POST")
    WeResultVo updateUserTag(@JSONBody WeAddUserTagQuery query);

    /**
     * 删除标签
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/tag/delete?tagid=${query.tagid}", type = "GET")
    WeResultVo delUserTag(@Var("query") WeUserTagQuery query);


    /**
     * 获取标签成员
     *
     * @param query
     * @return WeUserTagDetailVo
     */
    @Request(url = "/tag/get?tagid=${query.tagid}", type = "GET")
    WeUserTagDetailVo getUserTag(@Var("query") WeUserTagQuery query);


    /**
     * 增加标签成员
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/tag/addtagusers", type = "POST")
    WeResultVo addTagUsers(@JSONBody WeAddTagUsersQuery query);

    /**
     * 删除标签成员
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/tag/deltagusers", type = "POST")
    WeResultVo delTagUsers(@JSONBody WeAddTagUsersQuery query);

    /**
     * 获取标签列表
     *
     * @param query
     * @return WeUserTagListVo
     */
    @Request(url = "/tag/list", type = "GET")
    WeUserTagListVo getUserTagList(WeBaseQuery query);
}
