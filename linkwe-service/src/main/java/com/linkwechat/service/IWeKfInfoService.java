package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfInfo;
import com.linkwechat.domain.kf.query.WeAddKfInfoQuery;
import com.linkwechat.domain.kf.query.WeKfListQuery;
import com.linkwechat.domain.kf.vo.WeKfInfoVo;
import com.linkwechat.domain.kf.vo.QwKfListVo;

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
    void addAccount(WeAddKfInfoQuery query);

    /**
     * 客服账号修改
     * @param query
     */
    void editAccountInfo(WeAddKfInfoQuery query);

    /**
     * 客服欢迎语修改
     * @param query
     */
    void editAccountWelcome(WeAddKfInfoQuery query);

    /**
     * 客服接待规则修改
     * @param query
     */
    void editAccountReception(WeAddKfInfoQuery query);

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
     * 根据客服账号ID查询客服详情
     *
     * @param corpId
     * @param openKfId 客服账号ID
     * @return
     */
    WeKfInfoVo getKfInfoByOpenKfId(String corpId, String openKfId);

    /**
     * 异步同步客服列表
     */
    void asyncKfList();

    void synchKfAccountHandler(String msg);
}
