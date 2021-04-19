package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 实际群码Service接口
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
public interface IWeGroupCodeActualService extends IService<WeGroupCodeActual>
{
    /**
     * 查询实际群码
     *
     * @param id 实际群码ID
     * @return 实际群码
     */
    public WeGroupCodeActual selectWeGroupCodeActualById(Long id);

    /**
     * 根据群聊id获取对应群二维码
     * @param chatId 群聊id
     * @return 结果
     */
    public WeGroupCodeActual selectActualCodeByChatId(String chatId);

    /**
     * 查询实际群码列表
     *
     * @param weGroupCodeActual 实际群码
     * @return 实际群码集合
     */
    public List<WeGroupCodeActual> selectWeGroupCodeActualList(WeGroupCodeActual weGroupCodeActual);

    /**
     * 新增实际群码
     *
     * @param weGroupCodeActual 实际群码
     * @return 结果
     */
    public int insertWeGroupCodeActual(WeGroupCodeActual weGroupCodeActual);

    /**
     * 修改实际群码
     *
     * @param weGroupCodeActual 实际群码
     * @return 结果
     */
    public int updateWeGroupCodeActual(WeGroupCodeActual weGroupCodeActual);

    /**
     * 批量删除实际群码
     *
     * @param ids 需要删除的实际群码ID
     * @return 结果
     */
    public int deleteWeGroupCodeActualByIds(Long[] ids);

//    /**
//     * 删除实际群码信息
//     *
//     * @param id 实际群码ID
//     * @return 结果
//     */
//    public int deleteWeGroupCodeActualById(Long id);

    /**
     * 检测实际码chatId是否唯一
     * @param weGroupCodeActual 实际码
     * @return 结果
     */
    public boolean checkChatIdUnique(WeGroupCodeActual weGroupCodeActual);

    /**
     * 通过群id递增其实际群活码扫码次数
     * @param chatId
     */
    void updateScanTimesByChatId(String chatId);
}
