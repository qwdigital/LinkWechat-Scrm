package com.linkwechat.wecom.client.kf;

import com.dtflys.forest.annotation.*;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import com.linkwechat.wecom.domain.dto.kf.*;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @author danmo
 * @description 客服相关接口
 * @date 2021/10/9 14:09
 **/
@BaseRequest(baseURL = "${weComServerUrl}${weComePrefix}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeKfClient {

    /**
     * 添加客服
     *
     * @param dto in {@link WeKfDto}
     * @return {@link WeKfDto}
     */
    @Request(url = "/kf/account/add", type = "POST")
    public WeKfDto addAccount(@JSONBody WeKfDto dto);

    /**
     * 删除客服
     *
     * @param dto in {@link WeKfDto}
     * @return {@link WeResultDto}
     */
    @Request(url = "/kf/account/del", type = "POST")
    public WeResultDto deleteAccount(@JSONBody WeKfDto dto);

    /**
     * 修改客服信息
     *
     * @param dto in {@link WeKfDto}
     * @return {@link WeResultDto}
     */
    @Request(url = "/kf/account/update", type = "POST")
    public WeResultDto updateAccount(@JSONBody WeKfDto dto);

    /**
     * 获取客服帐号列表
     *
     * @return {@link WeKfListDto}
     */
    @Request(url = "/kf/account/list", type = "GET")
    public WeKfListDto list();

    /**
     * 获取客服帐号链接
     *
     * @param dto in {@link WeKfDto}
     * @return {@link WeKfDto}
     */
    @Request(url = "/kf/add_contact_way", type = "POST")
    public WeKfDto addContactWay(@JSONBody WeKfDto dto);

    /**
     * 添加接待人员
     *
     * @param dto in {@link WeKfDto}
     * @return {@link WeKfUserListDto}
     */
    @Request(url = "/kf/servicer/add", type = "POST")
    public WeKfUserListDto addServicer(@JSONBody WeKfDto dto);

    /**
     * 删除接待人员
     *
     * @param dto in {@link WeKfDto}
     * @return {@link WeKfUserListDto}
     */
    @Request(url = "/kf/servicer/del", type = "POST")
    public WeKfUserListDto delServicer(@JSONBody WeKfDto dto);

    /**
     * 获取接待人员列表
     *
     * @param openKfId 客服id
     * @return {@link WeKfUserListDto}
     */
    @Request(url = "/kf/servicer/list", type = "GET")
    public WeKfUserListDto servicerList(@Query("open_kfid") String openKfId);


    /**
     * 获取会话状态
     *
     * @param dto in {@link WeKfDto}
     * @return {@link WeKfDto}
     */
    @Request(url = "/kf/service_state/get", type = "POST")
    public WeKfDto getServiceState(@JSONBody WeKfDto dto);

    /**
     * 变更会话状态
     *
     * @param dto in {@link WeKfDto}
     * @return {@link WeKfDto}
     */
    @Request(url = "/kf/service_state/trans", type = "POST")
    public WeKfDto transServiceState(@JSONBody WeKfDto dto);


    /**
     * 发送消息
     *
     * @param dto in {@link WeKfMsgDto}
     * @return {@link WeKfMsgDto}
     */
    @Request(url = "/kf/send_msg", type = "POST")
    public WeKfMsgDto sendMsg(@JSONBody WeKfMsgDto dto);

    /**
     * 读取消息
     *
     * @param dto in {@link WeKfGetMsgDto}
     * @return {@link WeKfGetMsgDto}
     */
    @Request(url = "/kf/sync_msg", type = "POST")
    public WeKfGetMsgDto syncMsg(@JSONBody WeKfGetMsgDto dto);

    /**
     * 客户基本信息获取
     *
     * @param dto in {@link WeKfDto}
     * @return {@link WeKfCustomerInfoDto}
     */
    @Request(url = "/kf/customer/batchget", type = "POST")
    public WeKfCustomerInfoDto getCustomerInfoBatch(@JSONBody WeKfDto dto);
}
