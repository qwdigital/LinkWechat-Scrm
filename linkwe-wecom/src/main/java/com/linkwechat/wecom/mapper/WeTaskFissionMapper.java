package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeTaskFission;

import java.util.List;

/**
 * 任务宝Mapper接口
 *
 * @author leejoker
 * @date 2021-01-20
 */
public interface WeTaskFissionMapper extends BaseMapper<WeTaskFission> {
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
     * 删除任务宝
     *
     * @param id 任务宝ID
     * @return 结果
     */
    public int deleteWeTaskFissionById(Long id);

    /**
     * 批量删除任务宝
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeTaskFissionByIds(Long[] ids);

    /**
     * 更新过期任务
     * @return
     */
    public void updateExpiredWeTaskFission();
}
