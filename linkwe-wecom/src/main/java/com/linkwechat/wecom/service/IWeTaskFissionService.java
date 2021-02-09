package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeTaskFission;
import com.linkwechat.wecom.domain.dto.WeChatUserDTO;
import com.linkwechat.wecom.domain.dto.WeTaskFissionPosterDTO;

import java.util.List;

/**
 * 任务宝Service接口
 *
 * @author leejoker
 * @date 2021-01-20
 */
public interface IWeTaskFissionService {
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
    public int insertWeTaskFission(WeTaskFission weTaskFission);

    /**
     * 修改任务宝
     *
     * @param weTaskFission 任务宝
     * @return 结果
     */
    public int updateWeTaskFission(WeTaskFission weTaskFission);

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
     * @param id 任务id
     */
    public void sendWeTaskFission(Long id);

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
}
