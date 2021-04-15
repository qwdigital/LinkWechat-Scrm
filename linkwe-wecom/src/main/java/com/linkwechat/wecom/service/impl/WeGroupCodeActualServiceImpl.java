package com.linkwechat.wecom.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.config.ServerConfig;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeGroupCodeActualMapper;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.service.IWeGroupCodeActualService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 实际群码Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
@Service
public class WeGroupCodeActualServiceImpl extends ServiceImpl<WeGroupCodeActualMapper,WeGroupCodeActual> implements IWeGroupCodeActualService
{
    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private WeGroupCodeActualMapper weGroupCodeActualMapper;

    /**
     * 查询实际群码
     *
     * @param id 实际群码ID
     * @return 实际群码
     */
    @Override
    public WeGroupCodeActual selectWeGroupCodeActualById(Long id)
    {
        return weGroupCodeActualMapper.selectWeGroupCodeActualById(id);
    }

    /**
     * 根据群聊id获取对应群二维码
     *
     * @param chatId 群聊id
     * @return 结果
     */
    @Override
    public WeGroupCodeActual selectActualCodeByChatId(String chatId) {
        return weGroupCodeActualMapper.selectWeGroupCodeActualByChatId(chatId);
    }

    /**
     * 查询实际群码列表
     *
     * @param weGroupCodeActual 实际群码
     * @return 实际群码
     */
    @Override
    public List<WeGroupCodeActual> selectWeGroupCodeActualList(WeGroupCodeActual weGroupCodeActual)
    {
        return weGroupCodeActualMapper.selectWeGroupCodeActualList(weGroupCodeActual);
    }

    /**
     * 新增实际群码
     *
     * @param weGroupCodeActual 实际群码
     * @return 结果
     */
    @Override
    public int insertWeGroupCodeActual(WeGroupCodeActual weGroupCodeActual)
    {
        return weGroupCodeActualMapper.insertWeGroupCodeActual(weGroupCodeActual);
    }

    /**
     * 修改实际群码
     *
     * @param weGroupCodeActual 实际群码
     * @return 结果
     */
    @Override
    public int updateWeGroupCodeActual(WeGroupCodeActual weGroupCodeActual)
    {
        return this.baseMapper.updateWeGroupCodeActual(weGroupCodeActual);
    }

    /**
     * 批量删除实际群码
     *
     * @param ids 需要删除的实际群码ID
     * @return 结果
     */
    @Override
    public int deleteWeGroupCodeActualByIds(Long[] ids)
    {
        return weGroupCodeActualMapper.deleteWeGroupCodeActualByIds(ids);
    }

//    /**
//     * 删除实际群码信息
//     *
//     * @param id 实际群码ID
//     * @return 结果
//     */
//    @Override
//    public int deleteWeGroupCodeActualById(Long id)
//    {
//        return weGroupCodeActualMapper.deleteWeGroupCodeActualById(id);
//    }

    /**
     * 检测实际码chatId是否唯一
     *
     * @param weGroupCodeActual 实际码
     * @return 结果
     */
    @Override
    public boolean checkChatIdUnique(WeGroupCodeActual weGroupCodeActual) {
        // 不指定群聊时，不用检测唯一性
        String chatId = weGroupCodeActual.getChatId();
        if (StringUtils.isNull(chatId) | StringUtils.isEmpty(chatId))
        {
            return true;
        }
        int rows = weGroupCodeActualMapper.checkChatIdUnique(chatId);
        return rows == 0;
    }

    /**
     * 通过群id递增其实际群活码扫码次数
     *
     * @param chatId
     */
    @Override
    public void updateScanTimesByChatId(String chatId) {
        weGroupCodeActualMapper.updateScanTimesByChatId(chatId);
    }
}
