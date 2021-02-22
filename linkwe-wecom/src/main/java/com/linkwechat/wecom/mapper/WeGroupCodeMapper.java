package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroupCode;

/**
 * 客户群活码Mapper接口
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
public interface WeGroupCodeMapper  extends BaseMapper<WeGroupCode>
{
    /**
     * 查询客户群活码
     * 
     * @param id 客户群活码ID
     * @return 客户群活码
     */
    public WeGroupCode selectWeGroupCodeById(Long id);

    /**
     * 查询客户群活码列表
     * 
     * @param weGroupCode 客户群活码
     * @return 客户群活码集合
     */
    public List<WeGroupCode> selectWeGroupCodeList(WeGroupCode weGroupCode);

    /**
     * 根据群活码id查询群活码列表
     * @param ids id列表
     * @return 结果
     */
    public List<WeGroupCode> selectWeGroupCodeListByIds(List<String> ids);

    /**
     * 新增客户群活码
     * 
     * @param weGroupCode 客户群活码
     * @return 结果
     */
    public int insertWeGroupCode(WeGroupCode weGroupCode);

    /**
     * 修改客户群活码
     * 
     * @param weGroupCode 客户群活码
     * @return 结果
     */
    public int updateWeGroupCode(WeGroupCode weGroupCode);

    /**
     * 删除客户群活码
     * 
     * @param id 客户群活码ID
     * @return 结果
     */
    public int deleteWeGroupCodeById(Long id);

    /**
     * 批量删除客户群活码
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeGroupCodeByIds(Long[] ids);

    /**
     * 检测活码名称是否唯一
     * @param activityName 活码名称
     * @return 结果
     */
    public int checkActivityNameUnique(String activityName);
}
