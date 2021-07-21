package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroup;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import org.springframework.stereotype.Repository;

/**
 * 实际群码Mapper接口
 *
 * @author ruoyi
 * @date 2020-10-07
 */
@Repository
public interface WeGroupCodeActualMapper extends BaseMapper<WeGroupCodeActual> {
    /**
     * 查询实际群码
     *
     * @param id 实际群码ID
     * @return 实际群码
     */
    WeGroupCodeActual selectWeGroupCodeActualById(Long id);

    /**
     * 根据群聊id获取群实际码
     * @param chatId 群聊id
     * @return 结果
     */
    WeGroupCodeActual selectWeGroupCodeActualByChatId(String chatId);

    /**
     * 查询实际群码列表
     *
     * @param weGroupCodeActual 实际群码
     * @return 实际群码集合
     */
    List<WeGroupCodeActual> selectWeGroupCodeActualList(WeGroupCodeActual weGroupCodeActual);

    /**
     * 新增实际群码
     *
     * @param weGroupCodeActual 实际群码
     * @return 结果
     */
    int insertWeGroupCodeActual(WeGroupCodeActual weGroupCodeActual);

    /**
     * 修改实际群码
     *
     * @param weGroupCodeActual 实际群码
     * @return 结果
     */
    int updateWeGroupCodeActual(WeGroupCodeActual weGroupCodeActual);

    /**
     * 删除实际群码
     *
     * @param id 实际群码ID
     * @return 结果
     */
    int deleteWeGroupCodeActualById(Long id);

    /**
     * 批量删除实际群码
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteWeGroupCodeActualByIds(Long[] ids);

    /**
     * 通过群活码id查询实际码列表
     *
     * @param groupCodeId 群活码id
     * @return 结果
     */
    List<WeGroupCodeActual> selectActualList(Long groupCodeId);

    /**
     * 通过群活码id删除实际码
     * @param groupCodeIds 群活码id列表
     * @return 结果
     */
    int deleteActualListByGroupCodeIds(Long[] groupCodeIds);

    /**
     * 检测chatId是否唯一
     * @param chatId 群聊id
     * @return 结果
     */
    int checkChatIdUnique(String chatId);

    /**
     * 通过群id递增其实际群活码扫码次数
     * @param chatId
     */
    void updateScanTimesByChatId(String chatId);
}
