package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.WeKfPool;
import com.linkwechat.domain.kf.query.WeAddKfInfoQuery;
import com.linkwechat.domain.kf.query.WeAddKfServicerQuery;
import com.linkwechat.domain.kf.query.WeAddKfWelcomeQuery;
import com.linkwechat.domain.kf.query.WeKfListQuery;
import com.linkwechat.domain.kf.vo.QwKfListVo;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;

import java.util.List;

/**
 * 客服信息表(WeKfInfo)
 *
 * @author danmo
 * @since 2022-04-15 15:53:35
 */
public interface IWeKfInfoService extends IService<WeKfInfo> {

    /**
     * 创建客服
     * @param query
     */
    Long addAccount(WeAddKfInfoQuery query);

    /**
     * 创建客服欢迎语
     * @param query
     */
    void addAccountWelcome(WeAddKfWelcomeQuery query);

    /**
     * 添加客服接待人员
     * @param query
     */
    void addAccountServicer(WeAddKfServicerQuery query);

    /**
     * 客服账号修改
     * @param query
     */
    void editAccountInfo(WeAddKfInfoQuery query);

    /**
     * 客服欢迎语修改
     * @param query
     */
    void editAccountWelcome(WeAddKfWelcomeQuery query);

    /**
     * 客服接待人员修改
     * @param query
     */
    void editAccountServicer(WeAddKfServicerQuery query);

    /**
     * 删除客服
     * @param id 客服id
     */
    void delKfInfo(Long id);

    /**
     * 客服详情
     * @param id
     * @return
     */
    WeKfInfoVo getKfInfo(Long id);

    /**
     * 客服列表
     * @param query
     * @return
     */
    List<QwKfListVo> getKfList(WeKfListQuery query);

    /**
     * 客服列表(分页)
     * @param query
     * @return
     */
    PageInfo<QwKfListVo> getKfPageList(WeKfListQuery query);

    /**
     * 根据客服账号ID查询客服详情
     *
     * @param corpId
     * @param openKfId 客服账号ID
     * @return
     */
    WeKfInfoVo getKfInfoByOpenKfId(String corpId, String openKfId);

    /**
     * 根据客服账号ID查询客服详情
     *
     * @param corpId
     * @param openKfId 客服账号ID
     * @return
     */
    WeKfInfo getKfDetailByOpenKfId(String corpId, String openKfId);

    /**
     * 异步同步客服列表
     */
    void asyncKfList();

    void synchKfAccountHandler(String msg);

    /**
     * 发送结束语
     * @param code
     * @param weKfInfo
     * @param weKfPoolInfo
     */
    void sendEndMsg(String code, WeKfInfo weKfInfo, WeKfPool weKfPoolInfo);


}
