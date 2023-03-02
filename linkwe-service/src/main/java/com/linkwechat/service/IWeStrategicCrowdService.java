package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeStrategicCrowd;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.strategic.crowd.query.WeAddStrategicCrowdQuery;
import com.linkwechat.domain.strategic.crowd.query.WeStrategicCrowdListQuery;
import com.linkwechat.domain.strategic.crowd.query.WeStrategicCrowdQuery;
import com.linkwechat.domain.strategic.crowd.vo.WeStrategicCrowdAnalyzelVo;
import com.linkwechat.domain.strategic.crowd.vo.WeStrategicCrowdDetailVo;

import java.util.List;
import java.util.Map;

/**
 * 策略人群信息表(WeStrategicCrowd)
 *
 * @author danmo
 * @since 2022-07-05 15:33:28
 */
public interface IWeStrategicCrowdService extends IService<WeStrategicCrowd> {

    /**
     * 新增人群
     * @param query
     */
    void add(WeAddStrategicCrowdQuery query);

    /**
     * 修改人群
     * @param query
     */
    void update(WeAddStrategicCrowdQuery query);


    /**
     * 更新（批量更新）人群
     * @param ids 人群ID
     */
    void renew(List<Long> ids);

    /**
     * 人群详情
     * @param id
     * @return
     */
    WeStrategicCrowdDetailVo getDetail(Long id);

    /**
     * 删除人群
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 人群列表
     * @param query
     * @return
     */
    List<WeStrategicCrowd> getList(WeStrategicCrowdListQuery query);

    /**
     * 获取策略下拉选项
     * @param enumNames
     * @return
     */
    Map<String, Object> getDownOptions(List<String> enumNames);

    List<WeStrategicCrowd> getListByIds(List<Long> ids);

    /**
     * 更新状态
     * @param status 状态
     * @param idList 人群ID
     */
    void updateStatusByIds(Integer status, List<Long> idList);

    /**
     * 获取人群分析
     * @param query
     */
    WeStrategicCrowdAnalyzelVo getAnalyze(WeStrategicCrowdQuery query);

    /**
     * 获取人群客户列表
     * @param query
     * @return
     */
    List<WeCustomersVo> getCustomerList(WeStrategicCrowdQuery query);

    /**
     * 根据多个人群id批量获取客户列表
     * @param crowdIds
     * @return
     */
    List<WeCustomer> getCustomerListByCrowdIds(List<String> crowdIds);

    List<WeStrategicCrowd> getListIgnoreTenant(WeStrategicCrowdQuery query);

    void categoryDel(Long[] ids);
}
