package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfServicer;
import com.linkwechat.domain.kf.WeKfUser;
import com.linkwechat.domain.kf.query.WeKfServicerListQuery;
import com.linkwechat.domain.kf.vo.WeKfServicerListVo;
import com.linkwechat.domain.kf.vo.WeKfUpgradeServiceConfigVO;
import com.linkwechat.domain.wecom.query.kf.WeUpgradeServiceQuery;

import java.util.List;

/**
 * 客服接待人员表(WeKfServicer)
 *
 * @author danmo
 * @since 2022-04-15 15:53:39
 */
public interface IWeKfServicerService extends IService<WeKfServicer> {
    /**
     * 根据客服id查询接待人员
     *
     * @param kfId 客服id
     * @return
     */
    List<WeKfUser> getServicerByKfId(Long kfId);

    /**
     * 通过客服id删除接待人员信息
     *
     * @param kfId
     */
    void delServicerByKfId(Long kfId);


    /**
     * 修改接待人员状态
     *
     * @param corpId
     * @param openKfId       客服账号id
     * @param servicerUserId 接待人员id
     * @param status         状态
     */
    void updateServicerStatus(String corpId, String openKfId, String servicerUserId, Integer status);

    /**
     * 修改客服接待人员
     *
     * @param id               客服主键id
     * @param openKfId         客服id
     * @param userIds          接待人员
     * @param departmentIdList
     */
    void updateServicer(Long id, String openKfId, List<String> userIds, List<Long> departmentIdList);

    /**
     * 客服接待人员列表
     *
     * @param query
     * @return
     */
    List<WeKfServicerListVo> getKfServicerList(WeKfServicerListQuery query);

    List<WeKfUser> getKfUserIdList(Long kfId);

    /**
     * 获取客服升级服务
     *
     * @return {@link WeKfUpgradeServiceConfigVO}
     * @author WangYX
     * @date 2023/08/25 13:48
     */
    WeKfUpgradeServiceConfigVO getWeUpgradeServiceConfig();

    /**
     * 为客户升级为专员或客户群服务
     *
     * @param query
     * @author WangYX
     * @date 2023/08/25 13:51
     */
    void upgradeService(WeUpgradeServiceQuery query);

}
