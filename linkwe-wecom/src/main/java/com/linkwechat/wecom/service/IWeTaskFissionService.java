package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeTaskFission;
import com.linkwechat.wecom.domain.dto.WeChatUserDTO;
import com.linkwechat.wecom.domain.dto.WeTaskFissionPosterDTO;
import com.linkwechat.wecom.domain.vo.WeTaskFissionProgressVO;
import com.linkwechat.wecom.domain.vo.WeTaskFissionStatisticVO;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 任务宝Service接口
 *
 * @author leejoker
 * @date 2021-01-20
 */
public interface IWeTaskFissionService extends IService<WeTaskFission> {
    /**
     * 查询任务宝
     *
     * @param id 任务宝ID
     * @return 任务宝
     */
    public WeTaskFission selectWeTaskFissionById(Long id);

    /**
     * 查询任务宝列表
     *
     * @param weTaskFission 任务宝
     * @return 任务宝集合
     */
    public List<WeTaskFission> selectWeTaskFissionList(WeTaskFission weTaskFission);

    /**
     * 新增任务宝
     *
     * @param weTaskFission 任务宝
     * @return 结果
     */
    public void insertWeTaskFission(WeTaskFission weTaskFission);

    /**
     * 修改任务宝
     *
     * @param weTaskFission 任务宝
     * @return 结果
     */
    public Long updateWeTaskFission(WeTaskFission weTaskFission);

    /**
     * 批量删除任务宝
     *
     * @param ids 需要删除的任务宝ID
     * @return 结果
     */
    public int deleteWeTaskFissionByIds(Long[] ids);

    /**
     * 删除任务宝信息
     *
     * @param id 任务宝ID
     * @return 结果
     */
    public int deleteWeTaskFissionById(Long id);

    /**
     * 发送任务
     *
     * @param weTaskFission 任务
     */
    public void sendWeTaskFission(WeTaskFission weTaskFission) throws Exception;

    /**
     * 生成裂变任务海报
     *
     * @param weTaskFissionPosterDTO 裂变任务客户信息
     * @return
     */
    public String fissionPosterGenerate(WeTaskFissionPosterDTO weTaskFissionPosterDTO);

    /**
     * 添加裂变完成记录
     *
     * @param taskFissionId
     * @param taskFissionRecordId
     */
    public void completeFissionRecord(Long taskFissionId, Long taskFissionRecordId, WeChatUserDTO weChatUserDTO);

    List<WeCustomer> getCustomerListById(String unionId, String fissionId);

    WeTaskFissionStatisticVO taskFissionStatistic(Long taskFissionId, Date startTime, Date endTime);

    WeTaskFissionProgressVO getCustomerTaskProgress(WeTaskFission taskFission, String eid);

    /**
     * 更新过期任务
     * @return
     */
    public void updateExpiredWeTaskFission();

    /**
     * 根据群活码id 查询任务列表
     * @param groupCodeId 群活码id
     * @return
     */
    List<WeTaskFission> getTaskFissionListByGroupCodeId(Long groupCodeId);
}
